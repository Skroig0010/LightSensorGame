package jp.ac.titech.itpro.sdl.game.component;

import android.widget.Toast;

import java.util.HashSet;
import java.util.Set;

import jp.ac.titech.itpro.sdl.game.MainActivity;
import jp.ac.titech.itpro.sdl.game.entities.Entity;
import jp.ac.titech.itpro.sdl.game.math.Vector2;

// AABBのみ対応
public class ColliderComponent implements IComponent, ITransformable{
    private Entity parent;
    private Vector2 position;
    private Vector2 size;
    private TransformComponent transform;
    public boolean isTrigger;
    public float invMass;
    public final Set<ColliderComponent> prevCollided = new HashSet<>();

    public ColliderComponent(Vector2 size, Entity parent){
        this(size, true, 0, parent);
    }

    public ColliderComponent(Vector2 size, boolean isTrigger, float invMass, Entity parent){
        // TODO:ローカル座標用意して計算
        this.parent = parent;
        this.size = size;
        this.isTrigger = isTrigger;
        this.invMass = invMass;

        // TransformComponentが書き換わったとき自動で位置を変更してくれるようにする
        transform = parent.getComponent("jp.ac.titech.itpro.sdl.game.component.TransformComponent");
        if(transform == null){
            Toast.makeText(MainActivity.instance, "Colliderを登録したEntity内にTransformが見つかりません", Toast.LENGTH_LONG);
            return;
        }
        transform.setTransformCallbacks(this);
        position = transform.getPosition();
    }

    public void onCollide(ColliderComponent other){
        // オーバーライドして処理を決める
    }

    public void enterCollide(ColliderComponent other){
        // オーバーライドして処理を決める
    }

    public void exitCollide(ColliderComponent other){
        // オーバーライドして処理を決める
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getSize() {
        return size;
    }

    // resolveCollisionでしか使わない。Javaはダメ。
    public TransformComponent getTransform(){
        return transform;
    }

    public boolean on(ColliderComponent other){
        return position.x < other.getPosition().x + other.getSize().x
                && position.y < other.getPosition().y + other.getSize().y
                && other.getPosition().x < position.x + size.x
                && other.getPosition().y < position.y + size.y;
    }

    public void resolveCollision(ColliderComponent other){
        Vector2 v1 = position;
        float m1 = invMass;
        Vector2 v2 = other.getPosition();
        float m2 = other.invMass;
        Vector2 size2 = other.getSize();


        Vector2 n = v1.sub(v2).normalize();
        // めり込み距離の計算
        // 全部正値になってる。小さい方を取ってそれを解決するようなnの拡大率を算出し、nに掛ける
        float x1 = v1.x + size.x - v2.x;
        float x2 = v2.x + size2.x - v1.x;
        float y1 = v1.y + size.y - v2.y;
        float y2 = v2.y + size2.y - v1.y;
        float minX = Math.min(x1, x2);
        float minY = Math.min(y1, y2);
        // 押出距離
        float d = 0;
        if(minX < minY){
            // X基準で押出し
            d = Math.abs(minX / n.x);
        }else{
            d = Math.abs(minY / n.y);
        }
        // 加速度も速度も無いので重さで直に位置調整
        if(m1 != 0)transform.setPosition(v1.add(n.scale(d * m1 / (m1 + m2))));
        if(m2 != 0)other.getTransform().setPosition(v2.sub(n.scale(d * m2 / (m1 + m2))));

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
