package jp.ac.titech.itpro.sdl.game.component;

import java.util.ArrayList;
import java.util.List;

import jp.ac.titech.itpro.sdl.game.entities.Entity;
import jp.ac.titech.itpro.sdl.game.math.Vector2;

public class TransformComponent implements IComponent {
    private Vector2 position;
    private Entity parent;
    private List<ITransformable> transformCallbacks = new ArrayList<ITransformable>();
    public TransformComponent(Vector2 position, Entity parent){
        this.position = position;
        this.parent = parent;
    }
    public TransformComponent(float x, float y, Entity parent){
        this.position = new Vector2(x, y);
        this.parent = parent;
    }
    public TransformComponent(Entity parent){
        this(0, 0, parent);
    }

    public void setPosition(float x, float y){
        setPosition(new Vector2(x, y));
    }
    public void setPosition(Vector2 position){
        this.position = position;
        for(ITransformable transform : transformCallbacks){
            transform.transform(position);
        }
    }

    public void setTransformCallbacks(ITransformable transformable){
        transformCallbacks.add(transformable);
    }

    public void removeTransformCallBacks(ITransformable transformable){
        transformCallbacks.remove(transformable);
    }

    public Vector2 getPosition() {
        return position;
    }

    @Override
    public Entity getParent() {
        return parent;
    }
}
