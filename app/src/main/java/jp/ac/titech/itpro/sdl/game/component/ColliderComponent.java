package jp.ac.titech.itpro.sdl.game.component;

import jp.ac.titech.itpro.sdl.game.entities.Entity;
import jp.ac.titech.itpro.sdl.game.math.Vector2;

// AABBのみ対応
public abstract class ColliderComponent implements IComponent, ITransformable{
    private Entity parent;
    private Vector2 position;
    private Vector2 size;

    public ColliderComponent(Entity parent){
        this.parent = parent;

        // TransformComponentが書き換わったとき自動で位置を変更してくれるようにする
        try {
            Class<?> cls = Class.forName("jp.ac.titech.itpro.sdl.game.component.TransformComponent");
            TransformComponent component = parent.getComponent(cls);
            component.setTransformCallbacks(this);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public abstract void onCollide(ColliderComponent other);

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getSize() {
        return size;
    }

    public boolean on(ColliderComponent other){
        // TODO:ここ書く
        return false;
    }

    @Override
    public Entity getParent() {
        return parent;
    }

    @Override
    public void transform(Vector2 position) {
        this.position = position;
    }

}
