package jp.ac.titech.itpro.sdl.game.component;

import jp.ac.titech.itpro.sdl.game.entities.Entity;

public abstract class NormalUpdatableComponent implements IUpdatableComponent {
    protected Entity parent;

    public NormalUpdatableComponent(Entity parent){
        this.parent = parent;
    }

    @Override
    public Entity getParent() {
        return parent;
    }
}
