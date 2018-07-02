package jp.ac.titech.itpro.sdl.game.stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.ac.titech.itpro.sdl.game.component.IRenderableComponent;

public class RenderingLayers {
    public enum LayerType{
        BACK_GROUND,
        CHARACTER_UNDER,
        CHARACTER,
        CHARACTER_OVER,
        FORE_GROUND
    }
    private Map<LayerType, List<IRenderableComponent>> layers = new HashMap<LayerType, List<IRenderableComponent>>();

    public RenderingLayers(){
        for(LayerType type : LayerType.values()){
            layers.put(type, new ArrayList<IRenderableComponent>());
        }
    }

    public void add(LayerType type, IRenderableComponent renderableComponent){
        layers.get(type).add(renderableComponent);
    }

    public List<IRenderableComponent> get(LayerType type){
        return layers.get(type);
    }

    public void remove(LayerType type, IRenderableComponent renderableComponent){
        layers.get(type).remove(renderableComponent);
    }
}
