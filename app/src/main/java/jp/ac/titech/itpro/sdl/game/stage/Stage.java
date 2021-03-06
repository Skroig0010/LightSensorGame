package jp.ac.titech.itpro.sdl.game.stage;

import android.opengl.GLES20;
import android.util.Log;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import jp.ac.titech.itpro.sdl.game.component.FallComponent;
import jp.ac.titech.itpro.sdl.game.component.LightSensorComponent;
import jp.ac.titech.itpro.sdl.game.component.SpriteComponent;
import jp.ac.titech.itpro.sdl.game.entities.Battery;
import jp.ac.titech.itpro.sdl.game.entities.Entity;
import jp.ac.titech.itpro.sdl.game.entities.MovableFloor;
import jp.ac.titech.itpro.sdl.game.entities.PowerWay;
import jp.ac.titech.itpro.sdl.game.entities.PowerWayWall;
import jp.ac.titech.itpro.sdl.game.entities.RespawnFloor;
import jp.ac.titech.itpro.sdl.game.entities.SolarPanel;
import jp.ac.titech.itpro.sdl.game.graphics.FrameBuffer;
import jp.ac.titech.itpro.sdl.game.GLRenderer;
import jp.ac.titech.itpro.sdl.game.graphics.animation.AnimationController;
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
import jp.ac.titech.itpro.sdl.game.entities.MovableBox;
import jp.ac.titech.itpro.sdl.game.entities.Player;
import jp.ac.titech.itpro.sdl.game.entities.Button;
import jp.ac.titech.itpro.sdl.game.entities.VanishingWall;
import jp.ac.titech.itpro.sdl.game.entities.Wall;
import jp.ac.titech.itpro.sdl.game.graphics.sprite.EmoSprite;
import jp.ac.titech.itpro.sdl.game.math.Vector2;
import jp.ac.titech.itpro.sdl.game.messages.Message;
import jp.ac.titech.itpro.sdl.game.scenes.SceneStage;
import jp.ac.titech.itpro.sdl.game.util.JsonReader;
import jp.ac.titech.itpro.sdl.game.util.MapWithProperty;
import jp.ac.titech.itpro.sdl.game.view.View;

public class Stage {
    private List<IUpdatableComponent> updatables = new ArrayList<>();
    private List<ColliderComponent> collidables = new ArrayList<>();
    private List<LightSensorComponent> lightSensors = new ArrayList<>();
    private List<MessageReceiverComponent> receivers = new ArrayList<>();
    private List<MessageReceiverComponent> notified = new ArrayList<>();
    private List<AnimationController> animationControllers = new ArrayList<>();
    private StageMap<ColliderComponent> wallMap;// 衝突判定用
    private StageMap<IRenderableComponent> renderMapForeGround, renderMapBackGround;
    private List<ColliderComponent> collidableMover = new ArrayList<>();
    private Player player;

    private int[][] mapdata;
    private int[][] powerIdMap;
    private int powerId = 1;

    private SceneStage sceneStage;

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

    public Stage(SceneStage sceneStage, Vector2 respawnPosition){
        this.sceneStage = sceneStage;
        emoSprite = new EmoSprite();
        lSprite = new LightSprite();
        gaussSprite = new GaussSprite();

        // マップの設定
        MapWithProperty mapWithProperty = JsonReader.read("map.json");
        mapdata = mapWithProperty.data;
        int width = mapdata[0].length;
        int height = mapdata.length;
        powerIdMap = new int[mapdata.length][mapdata[0].length];
        wallMap = new StageMap<>(width, height);
        renderMapForeGround = new StageMap<>(width, height);
        renderMapBackGround = new StageMap<>(width, height);

        // powerIdMapの作成
        // ソーラーで探索
        for(int y = 0; y < height; y++){
            for (int x = 0; x < width; x++) {
                Vector2 position = new Vector2(x * 16, y * 16);
                if(mapdata[y][x] == 7) {
                    setPowerId(x, y);
                }
            }
        }

        // バッテリーで探索
        for(int y = 0; y < height; y++){
            for (int x = 0; x < width; x++) {
                Vector2 position = new Vector2(x * 16, y * 16);
                if(mapdata[y][x] == 9) {
                    setPowerId(x, y);
                }
            }
        }
        // マップはおそらく正しくできたのでその後何のエラーが起きているか探す

        for(int y = 0; y < height; y++){
            for (int x = 0; x < width; x++){
                Vector2 position = new Vector2(x * 16, y * 16);
                int id1, id2;
                int[] switchIds;
                Entity e;
                int up;
                int down;
                int left;
                int right;
                PowerWay.Direction dir;
                switch(mapdata[y][x]) {
                    case -1:
                        // なにもない
                        wallMap.set(x, y, null);
                        renderMapForeGround.set(x, y, null);
                        renderMapBackGround.set(x, y, null);
                        break;
                    case 0:
                        e = new Floor(this, position);
                        wallMap.set(x, y, (ColliderComponent) e.getComponent("jp.ac.titech.itpro.sdl.game.component.ColliderComponent"));
                        renderMapBackGround.set(x, y, (IRenderableComponent)e.getComponent("jp.ac.titech.itpro.sdl.game.component.IRenderableComponent"));
                        renderMapForeGround.set(x, y, null);
                        break;
                    case 1:
                        e = new Wall(this, position);
                        SpriteComponent sprite = e.getComponent("jp.ac.titech.itpro.sdl.game.component.SpriteComponent");
                        if(y == height - 1 || mapdata[(y + 1)][x] == 1){
                            sprite.controller.setCurrentAnimation("blank");
                        }else if(y == 0 || mapdata[(y - 1)][x] == 1){
                            sprite.controller.setCurrentAnimation("wall");
                        }else{
                            sprite.controller.setCurrentAnimation("block");
                        }
                        wallMap.set(x, y, (ColliderComponent) e.getComponent("jp.ac.titech.itpro.sdl.game.component.ColliderComponent"));
                        renderMapBackGround.set(x, y, null);
                        renderMapForeGround.set(x, y, (IRenderableComponent)e.getComponent("jp.ac.titech.itpro.sdl.game.component.IRenderableComponent"));
                        break;
                    case 2:
                        e = new Floor(this, position);
                        wallMap.set(x, y, (ColliderComponent) e.getComponent("jp.ac.titech.itpro.sdl.game.component.ColliderComponent"));
                        renderMapBackGround.set(x, y, (IRenderableComponent)e.getComponent("jp.ac.titech.itpro.sdl.game.component.IRenderableComponent"));
                        renderMapForeGround.set(x, y, null);
                        if(respawnPosition != null) {
                            player = new Player(this, respawnPosition);
                            ((FallComponent) player.getComponent("jp.ac.titech.itpro.sdl.game.component.FallComponent")).setRespawnPosition(respawnPosition);
                        }else{
                            player = new Player(this, position);
                        }
                        e = player;
                        collidableMover.add((ColliderComponent) e.getComponent("jp.ac.titech.itpro.sdl.game.component.ColliderComponent"));
                        break;
                    case 3:
                        e = new BrightWall(this, position);
                        wallMap.set(x, y, (ColliderComponent) e.getComponent("jp.ac.titech.itpro.sdl.game.component.ColliderComponent"));
                        renderMapBackGround.set(x, y, (IRenderableComponent)e.getComponent("jp.ac.titech.itpro.sdl.game.component.IRenderableComponent"));
                        renderMapForeGround.set(x, y, null);
                        break;
                    case 4:
                        e = new Floor(this, position);
                        wallMap.set(x, y, (ColliderComponent) e.getComponent("jp.ac.titech.itpro.sdl.game.component.ColliderComponent"));
                        renderMapBackGround.set(x, y, (IRenderableComponent)e.getComponent("jp.ac.titech.itpro.sdl.game.component.IRenderableComponent"));
                        renderMapForeGround.set(x, y, null);
                        switchIds = mapWithProperty.properties.get(x + y * width).ids;
                        boolean canRelease = mapWithProperty.properties.get(x + y * width).canRelease;
                        e = new Button(this, position, canRelease, switchIds[0]);
                        collidableMover.add((ColliderComponent) e.getComponent("jp.ac.titech.itpro.sdl.game.component.ColliderComponent"));
                        break;
                    case 5:
                        id1 = getPowerId(x, y, false);
                        switchIds = mapWithProperty.properties.get(x + y * width).ids;
                        e = new VanishingWall(this, position, switchIds, id1);
                        wallMap.set(x, y, (ColliderComponent) e.getComponent("jp.ac.titech.itpro.sdl.game.component.ColliderComponent"));
                        renderMapBackGround.set(x, y, null);
                        renderMapForeGround.set(x, y, (IRenderableComponent)e.getComponent("jp.ac.titech.itpro.sdl.game.component.IRenderableComponent"));
                        break;
                    case 6:
                        e = new Floor(this, position);
                        wallMap.set(x, y, (ColliderComponent) e.getComponent("jp.ac.titech.itpro.sdl.game.component.ColliderComponent"));
                        renderMapBackGround.set(x, y, (IRenderableComponent)e.getComponent("jp.ac.titech.itpro.sdl.game.component.IRenderableComponent"));
                        renderMapForeGround.set(x, y, null);
                        e = new MovableBox( position, this);
                        collidableMover.add((ColliderComponent) e.getComponent("jp.ac.titech.itpro.sdl.game.component.ColliderComponent"));
                        break;
                    case 7:
                        id1 = getPowerId(x, y, true);
                        e = new SolarPanel( this, position, id1);
                        wallMap.set(x, y, (ColliderComponent) e.getComponent("jp.ac.titech.itpro.sdl.game.component.ColliderComponent"));
                        renderMapBackGround.set(x, y, null);
                        renderMapForeGround.set(x, y, (IRenderableComponent)e.getComponent("jp.ac.titech.itpro.sdl.game.component.IRenderableComponent"));
                        break;
                    case 8:
                        id1 = getPowerId(x, y, true);
                        up = mapdata[y - 1][x];
                        down = mapdata[y + 1][x];
                        left = mapdata[y][x - 1];
                        right = mapdata[y][x + 1];
                        dir = PowerWay.Direction.UP_DOWN;
                        if(relatedOnPower(up) && relatedOnPower(down))dir = PowerWay.Direction.UP_DOWN;
                        if(relatedOnPower(left) && relatedOnPower(right))dir = PowerWay.Direction.LEFT_RIGHT;
                        if(relatedOnPower(up) && relatedOnPower(left))dir = PowerWay.Direction.UP_LEFT;
                        if(relatedOnPower(up) && relatedOnPower(right))dir = PowerWay.Direction.UP_RIGHT;
                        if(relatedOnPower(down) && relatedOnPower(left))dir = PowerWay.Direction.DOWN_LEFT;
                        if(relatedOnPower(down) && relatedOnPower(right))dir = PowerWay.Direction.DOWN_RIGHT;
                        e = new PowerWay(this, position, id1, dir);
                        wallMap.set(x, y, (ColliderComponent) e.getComponent("jp.ac.titech.itpro.sdl.game.component.ColliderComponent"));
                        renderMapBackGround.set(x, y, (IRenderableComponent)e.getComponent("jp.ac.titech.itpro.sdl.game.component.IRenderableComponent"));
                        renderMapForeGround.set(x, y, null);
                        break;
                    case 9:
                        id1 = getPowerId(x, y, true);
                        id2 = getPowerId(x, y, false);
                        e = new Battery(this, position, id2, id1);
                        wallMap.set(x, y, (ColliderComponent) e.getComponent("jp.ac.titech.itpro.sdl.game.component.ColliderComponent"));
                        renderMapBackGround.set(x, y, null);
                        renderMapForeGround.set(x, y, (IRenderableComponent)e.getComponent("jp.ac.titech.itpro.sdl.game.component.IRenderableComponent"));
                        break;
                    case 10:
                        id1 = getPowerId(x, y, true);
                        up = mapdata[y - 1][x];
                        down = mapdata[y + 1][x];
                        left = mapdata[y][x - 1];
                        right = mapdata[y][x + 1];
                        dir = PowerWay.Direction.UP_DOWN;
                        if(relatedOnPower(up) && relatedOnPower(down))dir = PowerWay.Direction.UP_DOWN;
                        if(relatedOnPower(left) && relatedOnPower(right))dir = PowerWay.Direction.LEFT_RIGHT;
                        if(relatedOnPower(up) && relatedOnPower(left))dir = PowerWay.Direction.UP_LEFT;
                        if(relatedOnPower(up) && relatedOnPower(right))dir = PowerWay.Direction.UP_RIGHT;
                        if(relatedOnPower(down) && relatedOnPower(left))dir = PowerWay.Direction.DOWN_LEFT;
                        if(relatedOnPower(down) && relatedOnPower(right))dir = PowerWay.Direction.DOWN_RIGHT;
                        e = new PowerWayWall(this, position, id1, dir);
                        wallMap.set(x, y, (ColliderComponent) e.getComponent("jp.ac.titech.itpro.sdl.game.component.ColliderComponent"));
                        renderMapForeGround.set(x, y, (IRenderableComponent)e.getComponent("jp.ac.titech.itpro.sdl.game.component.IRenderableComponent"));
                        renderMapBackGround.set(x, y, null);
                        break;
                    case 11:
                        // リス地点
                        e = new RespawnFloor(this, position);
                        wallMap.set(x, y, (ColliderComponent) e.getComponent("jp.ac.titech.itpro.sdl.game.component.ColliderComponent"));
                        renderMapBackGround.set(x, y, (IRenderableComponent)e.getComponent("jp.ac.titech.itpro.sdl.game.component.IRenderableComponent"));
                        renderMapForeGround.set(x, y, null);
                        break;
                }
            }
        }

        // プロパティの中から動く床を取得
        for (MapWithProperty.Property property : mapWithProperty.properties.values()) {
            if(property.polyLine != null){
                // 動く床の作成
                Entity e = new MovableFloor(this, property.polyLine);
                collidableMover.add((ColliderComponent) e.getComponent("jp.ac.titech.itpro.sdl.game.component.ColliderComponent"));
            }
        }
        gaussianFrameBuffer1 = new FrameBuffer(fbSize, fbSize);
        gaussianFrameBuffer2 = new FrameBuffer(fbSize, fbSize);
        postFrameBuffer = new FrameBuffer(fbSize, fbSize);
    }

    private boolean relatedOnPower(int id){
        return id == 5 || id == 7 || id == 8 || id == 9 || id == 10;
    }

    private int getPowerId(int x, int y, boolean isOutput){
        if(x < 0 || x >= powerIdMap[0].length || y < 0 || y >= powerIdMap.length)return 0;
        if(isOutput){
            if(powerIdMap[y][x] != 0){
                // すでに番号が決まっているはずなのでそれを返す
                return powerIdMap[y][x];
            }else{
                Log.e("Unexpected Error", "(" + x + ", " + y + ")先にpowerIdMapを作ってください");
                return 0;
            }
        }else{
            // 周囲にpowerIdがあるならそれを返す
            int currId = powerIdMap[y][x];
            if(x > 0 && powerIdMap[y][x - 1] != 0 && powerIdMap[y][x - 1] != currId)return powerIdMap[y][x - 1];
            if(x < powerIdMap[0].length - 1 && powerIdMap[y][x + 1] != 0 && powerIdMap[y][x + 1] != currId)return powerIdMap[y][x + 1];
            if(y > 0 && powerIdMap[y - 1][x] != 0 && powerIdMap[y - 1][x] != currId)return powerIdMap[y - 1][x];
            if(y < powerIdMap.length - 1 && powerIdMap[y + 1][x] != 0 && powerIdMap[y + 1][x] != currId)return powerIdMap[y + 1][x];
            // 周囲にpowerIdが無いならID付けつつ電源探索
            Log.e("Unexpected Error", "(" + x + ", " + y + ")先にpowerIdMapを作ってください");
            return 0;
        }
    }

    private void setPowerId(int x, int y){
        if(powerIdMap[y][x] != 0){
            return;
        }else{
            // 番号が決まっていないなら道もID付けつつ探索する
            Queue<int[]> placeQueue = new ArrayDeque();
            placeQueue.add(new int[]{x, y});
            while(placeQueue.size() > 0) {
                int[] place = placeQueue.remove();
                powerIdMap[place[1]][place[0]] = powerId;
                if (x > 0) {
                    if(isUnreachedPowerWay(place[0] - 1, place[1]))placeQueue.add(new int[]{place[0] - 1, place[1]});
                }
                if (x < powerIdMap[0].length - 1) {
                    if(isUnreachedPowerWay(place[0] + 1, place[1]))placeQueue.add(new int[]{place[0] + 1, place[1]});
                }
                if (y > 0) {
                    if(isUnreachedPowerWay(place[0], place[1] - 1))placeQueue.add(new int[]{place[0], place[1] - 1});
                }
                if (y < powerIdMap.length - 1) {
                    if(isUnreachedPowerWay(place[0], place[1] + 1))placeQueue.add(new int[]{place[0], place[1] + 1});
                }
            }
            // 最後にidをインクリメント
            powerId++;
        }
    }

    // バッテリーが合った場合powerIdMapに書き込むという副作用あり
    private boolean isUnreachedPowerWay(int x, int y){
        int mapId = mapdata[y][x];
        if (mapId == 7 || mapId == 8 || mapId == 10) {
            // ソーラーパネルか電源の道なら探索を続ける
            if (powerIdMap[y][x] == 0){
                return true;
            }else if(powerIdMap[y][x] == powerId) {
                // すでに探索済みなので何もしない
            }else{
                // 探索したらID付いているはずなのについてないのでおかしい
                // 唯一バッテリーの可能性がある
                if(mapId != 9)Log.e("マップ構成エラー", "位置(" + x + ", " + y + ")近辺に不具合あり. mapId = " + mapId);
            }
        }
        return false;
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
        }else if(component instanceof SpriteComponent){
            animationControllers.add(((SpriteComponent)((SpriteComponent) component)).controller);
        }
    }

    /**
     * コンポーネント削除
     * @param component
     */
    public void removeComponent(IComponent component){
        if (component instanceof IRenderableComponent) {
            IRenderableComponent renderable = (IRenderableComponent)component;
            layers.remove(renderable);
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
        float brightRatio = MainActivity.instance.getBrightness() / MainActivity.instance.getInitialBrightness();
        if(brightRatio > 0.8 && !isBright){
            isBright = true;
            setBrightness(true);
        }else if(brightRatio < 0.6 && isBright){
            isBright = false;
            setBrightness(false);
        }

        // BackKeyが押されたらリスポーンする
        if(MainActivity.instance.getBackKey()){
            notifyAll(new Message().new RESET_STAGE(new Object[]{((FallComponent)player.getComponent("jp.ac.titech.itpro.sdl.game.component.FallComponent")).getRespawnPosition()}));
        }

        // メッセージを処理
        isProcessingNotified = true;
        processNotified();
        isProcessingNotified = false;
        notified.clear();
        notified.addAll(delayNotified);
        delayNotified.clear();
        for(Message msg : delayMessageQueue) {
            notifyAll(msg);
        }
        delayMessageQueue.clear();

        // 全UpdatableComponentの更新
        for (IUpdatableComponent updatable:updatables ) {
            updatable.update();
        }

        // 衝突判定
        // Mover同士の衝突判定
        int size = collidableMover.size();
        for(int i = 0; i < size; i++){
            for (int j = 0; j < size; j++){
                if(i <= j)break;
                ColliderComponent collidable1 = collidableMover.get(i);
                ColliderComponent collidable2 = collidableMover.get(j);
                if(collidable1.on(collidable2)){
                    onCollide(collidable1, collidable2);
                }else if((collidable1.isTrigger || collidable2.isTrigger) && collidable1.prevCollided.contains(collidable2)){
                    // どっちかがトリガーで前回衝突していたら
                    whenGetOut(collidable1, collidable2);
                }
            }
        }
        // マップとの衝突判定
        for(ColliderComponent mover : collidableMover){
            int moverX = (int)(mover.getGlobal().x + 8) / 16;
            int moverY = (int)(mover.getGlobal().y + 8) / 16;
            for (int y = moverY - 1; y <= moverY + 1; y++) {
                for (int x = moverX - 1; x <= moverX + 1; x++) {
                    if(x < 0 || y < 0 || x > wallMap.width - 1 || y > wallMap.height - 1)continue;
                    if(wallMap.get(x, y) == null)continue;
                    ColliderComponent collider = wallMap.get(x, y);
                    if(collider != null) {
                        if (mover.on(collider)) {
                            onCollide(mover, collider);
                        } else if ((mover.isTrigger || collider.isTrigger) && mover.prevCollided.contains(collider)) {
                            // どっちかがトリガーで前回衝突していたら
                            whenGetOut(mover, collider);
                        }
                    }
                }
            }
        }
    }

    private void whenGetOut(ColliderComponent collidable1, ColliderComponent collidable2) {
        collidable1.exitCollide(collidable2);
        collidable2.exitCollide(collidable1);
        collidable1.prevCollided.remove(collidable2);
        collidable2.prevCollided.remove(collidable1);
    }

    private void onCollide(ColliderComponent collidable1, ColliderComponent collidable2){
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
        // 衝突解消後の値を渡す
        collidable1.onCollide(collidable2);
        collidable2.onCollide(collidable1);
    }


    // notified処理中はnotifyできない。1F遅らせる
    private boolean isProcessingNotified = false;
    private List<MessageReceiverComponent> delayNotified = new ArrayList<>();
    private Queue<Message> messageQueue = new ArrayDeque<>();
    private Queue<Message> delayMessageQueue = new ArrayDeque<>();

    // 全体通知
    public void notifyAll(Message message){
        if(isProcessingNotified){
            delayMessageQueue.add(message);
        }else {
            // ステージ自身もメッセージを受け取る
            messageQueue.add(message);

            // 全てのMessageQueueComponentに通知
            for (MessageReceiverComponent receiver : receivers) {
                receiver.notify(message);
            }
        }
    }

    public void processNotified(){
        // ステージ自身が受け取ったメッセージを処理
        for(Message msg : messageQueue){
            processMessage(msg);
        }
        messageQueue.clear();
        // 各receiverのメッセージを処理
        for (MessageReceiverComponent receiver : notified){
            receiver.processMessages();
        }
        notified.clear();
    }

    private void processMessage(Message msg){
        if(msg instanceof Message.RESET_STAGE){
            Vector2 respawnPosition = (Vector2)msg.getArgs()[0];
            sceneStage.respawn(respawnPosition);
        }
    }

    // MessageQueueComponentだけが使う
    public void setNotified(MessageReceiverComponent receiver){
        if(isProcessingNotified){
            delayNotified.add(receiver);
        }else {
            notified.add(receiver);
        }
    }

    // RenderableComponentだけが使う
    public void changeRnderingLayer(IRenderableComponent renderable, RenderingLayers.LayerType type){
        layers.remove(renderable);
        layers.add(type, renderable);

        int x = (int)(renderable.getTransform().getGlobal().x + 8) / 16;
        int y = (int)(renderable.getTransform().getGlobal().y + 8) / 16;
        switch (renderable.getLayerType()){
            case BACK_GROUND:
                if(type == RenderingLayers.LayerType.FORE_GROUND){
                    renderMapBackGround.set(x, y, null);
                    renderMapForeGround.set(x, y, renderable);
                }else if(type == RenderingLayers.LayerType.BACK_GROUND){
                    // 何もしない
                }else{
                    renderMapBackGround.set(x, y, null);
                }
                break;
            case FORE_GROUND:
                if(type == RenderingLayers.LayerType.BACK_GROUND){
                    renderMapForeGround.set(x, y, null);
                    renderMapBackGround.set(x, y, renderable);
                }else if(type == RenderingLayers.LayerType.FORE_GROUND){
                    // 何もしない
                }else{
                    renderMapBackGround.set(x, y, null);
                }
                break;
        }
    }

    /**
     * ステージ描画処理
     * @param sprite
     */
    public void render(Sprite sprite) {
        // Viewの座標を更新
        View.update();
        // アニメーションの更新
        for(AnimationController animContoroler : animationControllers){
            animContoroler.update();
        }
        // 白黒画像をそのまま描画
        gaussianFrameBuffer1.bindFrameBuffer();
        float darkValue = Math.min(1f,
                Math.max(MainActivity.instance.getBrightness() / MainActivity.instance.getInitialBrightness(), 0.5f));

        GLES20.glViewport(0, 0, gaussianFrameBuffer1.width, gaussianFrameBuffer1.height);
        GLES20.glClearColor(darkValue, darkValue, darkValue, 1);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        // enumの順序が上から下とは限らないので直接指定
        lSprite.setDarkValue(darkValue);
        lSprite.setLightValue(1f);
        lSprite.setIsBright(false);
        renderMap(lSprite, RenderingLayers.LayerType.BACK_GROUND);

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
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        // レイヤーの描画
        renderMap(sprite, RenderingLayers.LayerType.BACK_GROUND);
        renderLayer(sprite, RenderingLayers.LayerType.CHARACTER_UNDER);
        renderLayer(sprite, RenderingLayers.LayerType.CHARACTER);
        renderLayer(sprite, RenderingLayers.LayerType.CHARACTER_OVER);
        renderMap(sprite, RenderingLayers.LayerType.FORE_GROUND);
        GLES20.glBlendFunc(GLES20.GL_ZERO, GLES20.GL_SRC_COLOR);
        renderMap(lSprite, RenderingLayers.LayerType.BACK_GROUND);
        // Uniformを渡す
        emoSprite.render(0, 240, postFrameBuffer.getTexture(), new Rect(0, 0, fbSize, fbSize), 160, -240);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

    }

    private void renderMap(Sprite sprite, RenderingLayers.LayerType type){
        Vector2 viewPosition = View.getViewPosition();
        int vx = Math.max(0, Math.min((int)viewPosition.x / 16, mapdata[0].length - 11));
        int vy = Math.max(0, Math.min((int)viewPosition.y / 16, mapdata.length - 16));

        StageMap<IRenderableComponent> map;
        switch (type){
            case BACK_GROUND:
                map = renderMapBackGround;
                break;
            case FORE_GROUND:
                map = renderMapForeGround;
                break;
            default:
                return;
        }
        for (int y = vy; y < vy + 16; y++) {
            for (int x = vx; x < vx + 11; x++) {
                IRenderableComponent r = map.get(x, y);
                if(r != null)r.render(sprite);
            }
        }
    }

    private void renderLayer(Sprite sprite, RenderingLayers.LayerType type){
        Vector2 viewPosition = View.getViewPosition();
        for (IRenderableComponent renderable : layers.get(type) ) {
            Vector2 position = renderable.getTransform().getGlobal();
            if(position.x > viewPosition.x - 16 && position.x < viewPosition.x + 160
                    && position.y > viewPosition.y - 16 && position.y < viewPosition.y + 240) {
                renderable.render(sprite);
            }
        }
    }
}
