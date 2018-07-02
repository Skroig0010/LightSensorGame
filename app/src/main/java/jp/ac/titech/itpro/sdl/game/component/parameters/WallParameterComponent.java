package jp.ac.titech.itpro.sdl.game.component.parameters;

import jp.ac.titech.itpro.sdl.game.component.IComponent;
import jp.ac.titech.itpro.sdl.game.entities.Entity;

public class WallParameterComponent implements IComponent {
    private Entity parent;
    public final int[] switchIds;
    public final int powerId;
    public final int nSwitchRequired;

    public WallParameterComponent(final int[] switchIds, final int nSwitchRequired, final int powerId, Entity parent){
        this.switchIds = switchIds;
        this.powerId = powerId;
        this.nSwitchRequired = nSwitchRequired;
        this.parent = parent;
    }

    @Override
    public Entity getParent() {
        return null;
    }
}
