package jp.ac.titech.itpro.sdl.game.component;

import java.util.ArrayList;
import java.util.List;

import jp.ac.titech.itpro.sdl.game.entities.Entity;
import jp.ac.titech.itpro.sdl.game.math.Vector2;

public class TransformComponent implements IComponent, ITransformable{
    private Vector2 position;
    private Entity parent;
    private ITransformable transformParent = null;
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

    public void setLocal(float x, float y){
        setLocal(new Vector2(x, y));
    }

    @Override
    public Entity getParent() {
        return parent;
    }

    @Override
    public void setLocal(Vector2 position) {
        this.position = position;
    }

    public void setGlobal(float x, float y){
        setGlobal(new Vector2(x, y));
    }

    @Override
    public void setGlobal(Vector2 position) {
        if(transformParent != null) {
            this.position = position.sub(transformParent.getGlobal());
        }else{
            this.position = position;
        }
    }

    @Override
    public void setTransformParent(ITransformable parent) {
        transformParent = parent;
    }

    @Override
    public Vector2 getGlobal() {
        if(transformParent != null) {
            return position.add(transformParent.getGlobal());
        }else {
            return position;
        }
    }

    @Override
    public Vector2 getLocal() {
        return position;
    }
}
