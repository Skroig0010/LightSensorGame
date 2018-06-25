package jp.ac.titech.itpro.sdl.game.stage;

import java.util.ArrayList;
import java.util.List;

import jp.ac.titech.itpro.sdl.game.Sprite;
import jp.ac.titech.itpro.sdl.game.component.IComponent;
import jp.ac.titech.itpro.sdl.game.component.IRenderableComponent;
import jp.ac.titech.itpro.sdl.game.component.IUpdatableComponent;
import jp.ac.titech.itpro.sdl.game.entities.Floor;
import jp.ac.titech.itpro.sdl.game.entities.Entity;
import jp.ac.titech.itpro.sdl.game.entities.Player;
import jp.ac.titech.itpro.sdl.game.math.Vector2;

public class Stage {
    private List<Entity> entities = new ArrayList<Entity>();
    private List<IUpdatableComponent> updatables = new ArrayList<IUpdatableComponent>();
    private RenderingLayers layers = new RenderingLayers();
    private Player player;
    private StageMap map;

    private int width = 10;
    private int height = 10;
    private int[] mapdata = {
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
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
                    map.set(x, y, new Floor(this, new Vector2(x * 64, y * 64)));
                }
            }
        }
    }

    public void addComponent(IComponent component){
        if (component instanceof IRenderableComponent) {
            IRenderableComponent renderable = (IRenderableComponent)component;
            layers.add(renderable.getLayerType(), renderable);
        }
        if(component instanceof IUpdatableComponent){
            updatables.add((IUpdatableComponent)component);
        }
    }

    public void removeComponent(IComponent component){
        if (component instanceof IRenderableComponent) {
            IRenderableComponent renderable = (IRenderableComponent)component;
            layers.remove(renderable.getLayerType(), renderable);
        }
        if(component instanceof IUpdatableComponent){
            updatables.remove(component);
        }
    }

    public void update() {
        for (IUpdatableComponent updatable:updatables ) {
            updatable.update();
        }
    }

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
