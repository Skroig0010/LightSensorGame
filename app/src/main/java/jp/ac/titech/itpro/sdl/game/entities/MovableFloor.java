package jp.ac.titech.itpro.sdl.game.entities;

import jp.ac.titech.itpro.sdl.game.R;
import jp.ac.titech.itpro.sdl.game.component.ColliderComponent;
import jp.ac.titech.itpro.sdl.game.component.NormalUpdatableComponent;
import jp.ac.titech.itpro.sdl.game.component.SimpleRenderableComponent;
import jp.ac.titech.itpro.sdl.game.component.SpriteComponent;
import jp.ac.titech.itpro.sdl.game.component.TransformComponent;
import jp.ac.titech.itpro.sdl.game.math.Vector2;
import jp.ac.titech.itpro.sdl.game.stage.RenderingLayers;
import jp.ac.titech.itpro.sdl.game.stage.Stage;

public class MovableFloor extends Entity {
    public MovableFloor(Stage stage, Vector2 position) {
        super(stage);
        final TransformComponent transform = new TransformComponent(position, this);
        SpriteComponent sprite = new SpriteComponent(R.drawable.movablefloor,  16, 16, this);
        addComponent(transform);
        addComponent(sprite);
        addComponent(new NormalUpdatableComponent(this) {
            @Override
            public void update() {
                transform.setGlobal(transform.getGlobal().add(0, 0.1f));
            }
        });
        addComponent(new SimpleRenderableComponent(transform, sprite, RenderingLayers.LayerType.CHARACTER_UNDER, stage, this));
        addComponent(new ColliderComponent(new Vector2(4, 4), new Vector2(8, 8), this){
            @Override
            public void enterCollide(ColliderComponent other){
                if(other.getParent().hasComponent("jp.ac.titech.itpro.sdl.game.component.FallComponent")){
                    // 相対位置を取得
                    other.getTransform().setLocal(other.getGlobal().sub(transform.getGlobal()));
                    // 親に設定
                    other.getTransform().setTransformParent(transform);
                }
            }

            @Override
            public void exitCollide(ColliderComponent other) {
                if (other.getParent().hasComponent("jp.ac.titech.itpro.sdl.game.component.FallComponent")) {
                    // 親から解除
                    other.getTransform().setTransformParent(null);
                    // 絶対位置を取得
                    other.getTransform().setLocal(other.getGlobal().add(transform.getGlobal()));
                }
            }
        });
    }
}
