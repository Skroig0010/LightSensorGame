package jp.ac.titech.itpro.sdl.game.component;

import jp.ac.titech.itpro.sdl.game.MainActivity;
import jp.ac.titech.itpro.sdl.game.entities.Entity;

public abstract class LightSensorComponent implements IComponent {
    public Entity parent;

    public LightSensorComponent(Entity parent){
        this.parent = parent;
    }

    public abstract void onBrightnessChanged(boolean isBright);

    @Override
    public Entity getParent() {
        return parent;
    }
}
