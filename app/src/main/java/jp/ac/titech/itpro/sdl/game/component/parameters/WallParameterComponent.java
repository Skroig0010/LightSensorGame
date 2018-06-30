package jp.ac.titech.itpro.sdl.game.component.parameters;

import jp.ac.titech.itpro.sdl.game.component.IComponent;
import jp.ac.titech.itpro.sdl.game.entities.Entity;

public class WallParameterComponent implements IComponent {
    private Entity parent;
    public final int id;
    public final int nSwitchRequired;

    public WallParameterComponent(final int id, final int nSwitchRequired, Entity parent){
        this.id = id;
        this.nSwitchRequired = nSwitchRequired;
        this.parent = parent;
    }

    @Override
    public Entity getParent() {
        return null;
    }
}
