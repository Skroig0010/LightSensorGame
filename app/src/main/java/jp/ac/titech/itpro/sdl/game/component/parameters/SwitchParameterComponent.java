package jp.ac.titech.itpro.sdl.game.component.parameters;

import jp.ac.titech.itpro.sdl.game.component.IComponent;
import jp.ac.titech.itpro.sdl.game.entities.Entity;

public class SwitchParameterComponent implements IComponent {
    private Entity parent;
    public final boolean canRelease;
    public final int id;

    public SwitchParameterComponent(final boolean canRelease, final int id, Entity parent){
        this.canRelease = canRelease;
        this.id = id;
        this.parent = parent;
    }
    @Override
    public Entity getParent() {
        return null;
    }
}
