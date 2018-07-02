package jp.ac.titech.itpro.sdl.game.stage;

import android.opengl.GLES20;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import jp.ac.titech.itpro.sdl.game.component.LightSensorComponent;
import jp.ac.titech.itpro.sdl.game.entities.SolarPanel;
import jp.ac.titech.itpro.sdl.game.graphics.FrameBuffer;
import jp.ac.titech.itpro.sdl.game.GLRenderer;
import jp.ac.titech.itpro.sdl.game.graphics.sprite.GaussSprite;
import jp.ac.titech.itpro.sdl.game.graphics.sprite.LightSprite;
import jp.ac.titech.itpro.sdl.game.MainActivity;
import jp.ac.titech.itpro.sdl.game.Rect;
import jp.ac.titech.itpro.sdl.game.graphics.sprite.Sprite;
import jp.ac.titech.itpro.sdl.game.component.ColliderComponent;
import jp.ac.titech.itpro.sdl.game.component.IComponent;
import jp.ac.titech.itpro.sdl.game.component.IRenderableComponent;
import jp.ac.titech.itpro.sdl.game.component.IUpdatableComponent;
import jp.ac.titech.itpro.sdl.game.component.MessageReceiverComponent;
import jp.ac.titech.itpro.sdl.game.entities.BrightWall;
import jp.ac.titech.itpro.sdl.game.entities.Floor;
import jp.ac.titech.itpro.sdl.game.entities.Entity;
import jp.ac.titech.itpro.sdl.game.entities.MovableBox;
import jp.ac.titech.itpro.sdl.game.entities.Player;
import jp.ac.titech.itpro.sdl.game.entities.Switch;
import jp.ac.titech.itpro.sdl.game.entities.VanishingWall;
import jp.ac.titech.itpro.sdl.game.entities.Wall;
import jp.ac.titech.itpro.sdl.game.graphics.sprite.EmoSprite;
import jp.ac.titech.itpro.sdl.game.math.Vector2;
import jp.ac.titech.itpro.sdl.game.messages.Message;
import jp.ac.titech.itpro.sdl.game.view.View;

public class Stage {
    private List<Entity> entities = new ArrayList<>();
    private List<IUpdatableComponent> updatables = new ArrayList<>();
    private List<ColliderComponent> collidables = new ArrayList<>();
    private List<LightSensorComponent> lightSensors = new ArrayList<>();
    private List<MessageReceiverComponent> receivers = new ArrayList<>();
    private List<MessageReceiverComponent> notified = new ArrayList<>();
    private Player player;
    private StageMap map;// 使ってない

    private int width = 10;
    private int height = 10;
    private int[] mapdata = {
            0, 1, 1, 1, 1, 1, 1, 1, 0, 0,
            0, 2, 0, 0, 0, 0, 0, 1, 0, 0,
            0, 6, 2, 2, 1, 2, 0, 1, 0, 0,
            0, 4, 0, 0, 0, 2, 0, 1, 0, 0,
            0, 0, 1, 2, 2, 2, 0, 1, 0, 0,
            3, 0, 0, 0, 0, 0, 0, 1, 0, 0,
            5, 0, 1, 2, 2, 2, 2, 1, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 1, 0, 1, 0, 1, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0
    };


    // 描画関連
    private RenderingLayers layers = new RenderingLayers();
    private EmoSprite emoSprite;
    // ぼかし用フレームバッファ
    private FrameBuffer gaussianFrameBuffer1, gaussianFrameBuffer2;
    private LightSprite lSprite;
    private GaussSprite gaussSprite;
    // 最終描画用フレームバッファ
    private FrameBuffer postFrameBuffer;
    private final int fbSize = 128;

    public Stage(){
        emoSprite = new EmoSprite();
        lSprite = new LightSprite();
        gaussSprite = new GaussSprite();
        player = new Player(this);
        entities.add(player);

        // マップの設定
        map = new StageMap(width, height);
        for(int y = 0; y < height; y++){
            for (int x = 0; x < width; x++){
                switch(mapdata[y * width + x]) {
                    case 0:
                        map.set(x, y, new Floor(this, new Vector2(x * 16, y * 16)));
                        break;
                    case 1:
                        map.set(x, y, new Wall(this, new Vector2(x * 16, y * 16)));
                        break;
                    case 2:
                        map.set(x, y, new BrightWall(this, new Vector2(x * 16, y * 16)));
                        break;
                    case 3:
                        map.set(x, y, new Switch(this, new Vector2(x * 16, y * 16), true,0));
                        map.set(x, y, new Floor(this, new Vector2(x * 16, y * 16)));
                        break;
                    case 4:
                        map.set(x, y, new VanishingWall(this, new Vector2(x * 16, y * 16), new int[]{0}, 0, 1));
                        break;
                    case 5:
                        map.set(x, y, new MovableBox( new Vector2(x * 16, y * 16), this));
                        map.set(x, y, new Floor(this, new Vector2(x * 16, y * 16)));
                        break;
                    case 6:
                        map.set(x, y, new SolarPanel( this, new Vector2(x * 16, y * 16), 0));
                        break;
                }
            }
        }
        gaussianFrameBuffer1 = new FrameBuffer(fbSize, fbSize);
        gaussianFrameBuffer2 = new FrameBuffer(fbSize, fbSize);
        postFrameBuffer = new FrameBuffer(fbSize, fbSize);
    }

    /**
     * コンポーネント追加
     * @param component
     */
    public void addComponent(IComponent component){
        if (component instanceof IRenderableComponent) {
            IRenderableComponent renderable = (IRenderableComponent)component;
            layers.add(renderable.getLayerType(), renderable);
        }else if(component instanceof IUpdatableComponent){
            updatables.add((IUpdatableComponent)component);
        }else if(component instanceof ColliderComponent){
            collidables.add((ColliderComponent)component);
        }else if(component instanceof MessageReceiverComponent){
            receivers.add((MessageReceiverComponent)component);
        }else if(component instanceof LightSensorComponent){
            lightSensors.add((LightSensorComponent)component);
        }
    }

    /**
     * コンポーネント削除
     * @param component
     */
    public void removeComponent(IComponent component){
        if (component instanceof IRenderableComponent) {
            IRenderableComponent renderable = (IRenderableComponent)component;
            layers.remove(renderable.getLayerType(), renderable);
        }
        if(component instanceof IUpdatableComponent){
            updatables.remove(component);
        }
        if(component instanceof ColliderComponent){
            collidables.remove((ColliderComponent)component);
        }
    }

    private boolean isBright = false;
    private void setBrightness(boolean isBright){
        for(LightSensorComponent lightSencor : lightSensors){
            lightSencor.onBrightnessChanged(isBright);
        }
    }

    /**
     * ステージ更新処理
     */
    public void update() {
        // 状態変化によるコールバック呼び出し
        // 明るさの変化
        if(MainActivity.instance.getBrightness() > 300 && !isBright){
            isBright = true;
            setBrightness(true);
        }else if(MainActivity.instance.getBrightness() < 100 && isBright){
            isBright = false;
            setBrightness(false);
        }

        // メッセージを処理
        for(MessageReceiverComponent receiver : notified){
            receiver.processMessages();
        }
        notified.clear();

        // 全UpdatableComponentの更新
        for (IUpdatableComponent updatable:updatables ) {
            updatable.update();
        }

        // 衝突判定
        // TODO:O(n^2)なのよろしくないので(重くなったら)修正する
        int size = collidables.size();
        for(int i = 0; i < size; i++){
            for (int j = 0; j < size; j++){
                if(i <= j)break;
                ColliderComponent collidable1 = collidables.get(i);
                ColliderComponent collidable2 = collidables.get(j);
                if(collidable1.on(collidable2)){
                    // 衝突解消前の値を渡す
                    collidable1.onCollide(collidable2);
                    collidable2.onCollide(collidable1);
                    // 両方共実態を持っているなら衝突解消を行う
                    if(!(collidable1.isTrigger || collidable2.isTrigger)){
                        collidable1.resolveCollision(collidable2);
                    }else{
                        if(!collidable1.prevCollided.contains(collidable2)){
                            // 前回衝突していなかったらEnterを呼ぶ
                            collidable1.enterCollide(collidable2);
                            collidable2.enterCollide(collidable1);
                            collidable1.prevCollided.add(collidable2);
                            collidable2.prevCollided.add(collidable1);
                        }
                    }
                }else if((collidable1.isTrigger || collidable2.isTrigger) && collidable1.prevCollided.contains(collidable2)){
                    // どっちかがトリガーで前回衝突していたら
                    collidable1.exitCollide(collidable2);
                    collidable2.exitCollide(collidable1);
                    collidable1.prevCollided.remove(collidable2);
                    collidable2.prevCollided.remove(collidable1);
                }
            }
        }
    }

    // 全体通知
    public void notifyAll(Message message){
        // 全てのMessageQueueComponentに通知
        for(MessageReceiverComponent receiver : receivers){
            receiver.notify(message);
        }
    }

    public void processNotified(){
        for (MessageReceiverComponent receiver : notified){
            receiver.processMessages();
        }
        notified.clear();
    }

    // MessageQueueComponentだけが使う
    public void setNotified(MessageReceiverComponent receiver){
        notified.add(receiver);
    }

    /**
     * ステージ描画処理
     * @param sprite
     */
    public void render(Sprite sprite) {
        // Viewの座標を更新
        View.update();
        // 白黒画像をそのまま描画
        gaussianFrameBuffer1.bindFrameBuffer();
        GLES20.glViewport(0, 0, gaussianFrameBuffer1.width, gaussianFrameBuffer1.height);
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        // enumの順序が上から下とは限らないので直接指定
        lSprite.setDarkValue(Math.min(1f,Math.max(MainActivity.instance.getBrightness() / 500, 0.5f)));
        lSprite.setLightValue(1f);
        lSprite.setIsBright(false);
        renderLayer(lSprite, RenderingLayers.LayerType.BACK_GROUND);
        renderLayer(lSprite, RenderingLayers.LayerType.CHARACTER_UNDER);
        renderLayer(lSprite, RenderingLayers.LayerType.CHARACTER);
        renderLayer(lSprite, RenderingLayers.LayerType.CHARACTER_OVER);
        renderLayer(lSprite, RenderingLayers.LayerType.FORE_GROUND);

        // ぼかして描画
        gaussianFrameBuffer2.bindFrameBuffer();
        GLES20.glViewport(0, 0, gaussianFrameBuffer2.width, gaussianFrameBuffer2.height);
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        // Uniformを渡す
        gaussSprite.setHorizontal(false);
        gaussSprite.render(0, 0, gaussianFrameBuffer1.getTexture(), new Rect(0, 0, fbSize, fbSize), 160, 240);

        postFrameBuffer.bindFrameBuffer();
        GLES20.glViewport(0, 0, postFrameBuffer.width, postFrameBuffer.height);
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        // Uniformを渡す
        gaussSprite.setHorizontal(true);
        gaussSprite.render(0, 0, gaussianFrameBuffer2.getTexture(), new Rect(0, 0, fbSize, fbSize), 160, 240);

        postFrameBuffer.unbindFrameBuffer();
        GLES20.glViewport(0, 0, GLRenderer.getWidth(), GLRenderer.getHeight());
        GLES20.glClearColor(0.0f, 0.0f, 1.0f, 1);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        // レイヤーの描画
        renderLayer(sprite, RenderingLayers.LayerType.BACK_GROUND);
        renderLayer(sprite, RenderingLayers.LayerType.CHARACTER_UNDER);
        renderLayer(sprite, RenderingLayers.LayerType.CHARACTER);
        renderLayer(sprite, RenderingLayers.LayerType.CHARACTER_OVER);
        renderLayer(sprite, RenderingLayers.LayerType.FORE_GROUND);
        GLES20.glBlendFunc(GLES20.GL_ZERO, GLES20.GL_SRC_COLOR);
        // Uniformを渡す
        emoSprite.render(0, 240, postFrameBuffer.getTexture(), new Rect(0, 0, fbSize, fbSize), 160, -240);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
    }

    private void renderLayer(Sprite sprite, RenderingLayers.LayerType type){
        for (IRenderableComponent renderable : layers.get(type) ) {
            renderable.render(sprite);
        }
    }
}
