package jp.ac.titech.itpro.sdl.game.stage;

import java.util.ArrayList;
import java.util.List;

import jp.ac.titech.itpro.sdl.game.Sprite;
import jp.ac.titech.itpro.sdl.game.component.ColliderComponent;
import jp.ac.titech.itpro.sdl.game.component.IComponent;
import jp.ac.titech.itpro.sdl.game.component.IRenderableComponent;
import jp.ac.titech.itpro.sdl.game.component.IUpdatableComponent;
import jp.ac.titech.itpro.sdl.game.entities.Floor;
import jp.ac.titech.itpro.sdl.game.entities.Entity;
import jp.ac.titech.itpro.sdl.game.entities.Player;
import jp.ac.titech.itpro.sdl.game.entities.Wall;
import jp.ac.titech.itpro.sdl.game.math.Vector2;

public class Stage {
    private List<Entity> entities = new ArrayList<Entity>();
    private List<IUpdatableComponent> updatables = new ArrayList<IUpdatableComponent>();
    private List<ColliderComponent> collidables = new ArrayList<ColliderComponent>();
    private RenderingLayers layers = new RenderingLayers();
    private Player player;
    private StageMap map;

    private int width = 10;
    private int height = 10;
    private int[] mapdata = {
            0, 1, 1, 1, 1, 1, 1, 1, 0, 0,
            0, 1, 0, 0, 0, 0, 0, 1, 0, 0,
            0, 1, 1, 1, 1, 1, 0, 1, 0, 0,
            0, 0, 0, 0, 0, 1, 0, 1, 0, 0,
            0, 1, 1, 1, 1, 1, 0, 1, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 1, 0, 0,
            0, 1, 1, 1, 1, 1, 1, 1, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            1, 0, 1, 0, 1, 0, 1, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0
    };

    public Stage(){
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
                }
            }
        }
    }

    /**
     * コンポーネント追加
     * @param component
     */
    public void addComponent(IComponent component){
        if (component instanceof IRenderableComponent) {
            IRenderableComponent renderable = (IRenderableComponent)component;
            layers.add(renderable.getLayerType(), renderable);
        }
        if(component instanceof IUpdatableComponent){
            updatables.add((IUpdatableComponent)component);
        }
        if(component instanceof ColliderComponent){
            collidables.add((ColliderComponent)component);
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

    /**
     * ステージ更新処理
     */
    public void update() {
        // 全UpdatableComponentの更新
        for (IUpdatableComponent updatable:updatables ) {
            updatable.update();
        }

        // 衝突判定
        // TODO:O(n^2)なのよろしくないので(重くなったら)修正する
        for (ColliderComponent collidable1 : collidables){
            for (ColliderComponent collidable2 : collidables){
                if(collidable1 != collidable2 && collidable1.on(collidable2)){
                    // 衝突解消前の値を渡す
                    collidable1.onCollide(collidable2);
                    collidable2.onCollide(collidable1);
                    // 両方共実態を持っているなら衝突解消を行う
                    if(!(collidable1.isTrigger || collidable2.isTrigger)){
                        collidable1.resolveCollision(collidable2);
                    }
                }
            }
        }
    }

    /**
     * ステージ描画処理
     * @param sprite
     */
    public void render(Sprite sprite) {
        // 順序が指定できればいいのに
        renderLayer(sprite, RenderingLayers.LayerType.BACK_GROUND);
        renderLayer(sprite, RenderingLayers.LayerType.CHARACTER);
        renderLayer(sprite, RenderingLayers.LayerType.FORE_GROUND);
    }

    private void renderLayer(Sprite sprite, RenderingLayers.LayerType type){
        for (IRenderableComponent renderable : layers.get(type) ) {
            renderable.render(sprite);
        }
    }
}
