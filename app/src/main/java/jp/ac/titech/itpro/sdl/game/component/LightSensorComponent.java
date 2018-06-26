package jp.ac.titech.itpro.sdl.game.component;

import jp.ac.titech.itpro.sdl.game.MainActivity;
import jp.ac.titech.itpro.sdl.game.entities.Entity;

public class LightSensorComponent implements IComponent {
    public Entity parent;

    public LightSensorComponent(Entity parent){
        this.parent = parent;
    }

    public float getBrightness(){
        return MainActivity.instance.getBrightness();
    }

    @Override
    public Entity getParent() {
        return parent;
    }
}
