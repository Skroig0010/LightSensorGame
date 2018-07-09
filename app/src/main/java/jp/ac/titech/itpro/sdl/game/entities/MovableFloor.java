package jp.ac.titech.itpro.sdl.game.entities;

import java.util.List;

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
    public MovableFloor(Stage stage, final List<Vector2> positions) {
        super(stage);
        final TransformComponent transform = new TransformComponent(positions.get(0), this);
        SpriteComponent sprite = new SpriteComponent(R.drawable.movablefloor,  16, 16, this);
        addComponent(transform);
        addComponent(sprite);
        addComponent(new NormalUpdatableComponent(this) {
            /** polylineとカウンタを用いて移動処理を作る;
             *  時間で割って速度を算出;
             *  端点まで来たら折返し;
             */
            private final float VELOCITY = 1;
            Vector2 first = positions.get(0), last = positions.get(1);
            int index = 0;
            float divPoint = 0;
            boolean goToForward = true;
            @Override
            public void update() {
                Vector2 vec = last.sub(first);
                divPoint += VELOCITY / vec.length();
                if(divPoint > 1){
                    // 端点まで来た
                    divPoint = 0;
                    if(goToForward) {
                        index++;
                        if(index > positions.size() - 2){
                            // indexが範囲を超えたら折り返す;
                            goToForward = false;
                            index = positions.size() - 2;
                            first = positions.get(index + 1);
                            last = positions.get(index);
                        }else {
                            first = positions.get(index);
                            last = positions.get(index + 1);
                        }
                    }else{
                        index--;
                        if(index < 0){
                            goToForward = true;
                            index = 0;
                            first = positions.get(index);
                            last = positions.get(index + 1);
                        }else{
                            first = positions.get(index + 1);
                            last = positions.get(index);
                        }
                    }
                }

                transform.setGlobal(first.add(vec.scale(divPoint)));
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
