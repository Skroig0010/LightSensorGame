package jp.ac.titech.itpro.sdl.game.component.parameters;

import jp.ac.titech.itpro.sdl.game.component.IComponent;
import jp.ac.titech.itpro.sdl.game.entities.Entity;

public class SolerParameterComponent implements IComponent {
    public final int id;
    private Entity parent;

    public SolerParameterComponent(final int id, Entity parent){
        this.id = id;
        this.parent = parent;
    }
    @Override
    public Entity getParent() {
        return parent;
    }
}
