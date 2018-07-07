package jp.ac.titech.itpro.sdl.game.component;

import jp.ac.titech.itpro.sdl.game.entities.Entity;
import jp.ac.titech.itpro.sdl.game.math.Vector2;

public class FallComponent implements IComponent {
    private Entity parent;
    private Vector2 respawnPosition;
    private TransformComponent transform;

    public FallComponent(Vector2 respawnPosition, TransformComponent transform, Entity parent){
        this.respawnPosition = respawnPosition;
        this.parent = parent;
        this.transform = transform;
    }

    public void fall(){
        transform.setTransformParent(null);
        transform.setGlobal(respawnPosition);
    }

    public void setRespawnPosition(Vector2 respawnPosition){
        this.respawnPosition = respawnPosition;
    }

    @Override
    public Entity getParent() {
        return parent;
    }
}
