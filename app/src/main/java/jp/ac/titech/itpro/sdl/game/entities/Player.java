package jp.ac.titech.itpro.sdl.game.entities;

import jp.ac.titech.itpro.sdl.game.R;
import jp.ac.titech.itpro.sdl.game.Rect;
import jp.ac.titech.itpro.sdl.game.component.ColliderComponent;
import jp.ac.titech.itpro.sdl.game.component.IRenderableComponent;
import jp.ac.titech.itpro.sdl.game.component.IUpdatableComponent;
import jp.ac.titech.itpro.sdl.game.component.NormalUpdatableComponent;
import jp.ac.titech.itpro.sdl.game.component.SimpleRenderableComponent;
import jp.ac.titech.itpro.sdl.game.component.TransformComponent;
import jp.ac.titech.itpro.sdl.game.component.SpriteComponent;
import jp.ac.titech.itpro.sdl.game.component.TouchControllerComponent;
import jp.ac.titech.itpro.sdl.game.math.Vector2;
import jp.ac.titech.itpro.sdl.game.stage.RenderingLayers;
import jp.ac.titech.itpro.sdl.game.stage.Stage;

public class Player extends Entity{

    TransformComponent transform;
    SpriteComponent sprite;
    TouchControllerComponent touch;
    IUpdatableComponent update;
    IRenderableComponent render;
    ColliderComponent collider;

    public Player(Stage stage){
        super(stage);
        transform = new TransformComponent(this);
        sprite = new SpriteComponent(R.drawable.human, new Rect(0,0,16,16), this);
        touch = new TouchControllerComponent(this);
        final Entity parent = this;
        update = new NormalUpdatableComponent(this) {
            private final int THRESHOLD_FLICK_VELOCITY = 50;
            private Vector2 veloc = new Vector2();
            @Override
            public void update() {
                double rot = Math.atan2(touch.getDirection().y, touch.getDirection().x);
                float len = touch.getDirection().x * touch.getDirection().x + touch.getDirection().y * touch.getDirection().y;
                // TODO:移動処理が冗長なのを直す
                if((int)transform.getPosition().x % 16 == 0 && (int)transform.getPosition().y % 16 == 0){
                    move(rot, len);
                }
                transform.setPosition(transform.getPosition().x + veloc.x, transform.getPosition().y + veloc.y);
            }

            private void move(double rot, float len){
                if (rot < 0) rot += 2 * Math.PI;
                if(len > THRESHOLD_FLICK_VELOCITY) {
                        if (rot >= Math.PI / 4 && rot < Math.PI * 3 / 4) {
                            // 下向き
                            veloc = new Vector2(0, 1);
                        } else if (rot >= Math.PI * 3 / 4 && rot < Math.PI * 5 / 4) {
                            // 左向き
                            veloc = new Vector2(-1, 0);
                        } else if (rot >= Math.PI * 5 / 4 && rot < Math.PI * 7 / 4) {
                            // 上向き
                            veloc = new Vector2(0, -1);
                        } else {
                            // 右向き
                            veloc = new Vector2(1, 0);
                        }
                }else{
                    veloc = new Vector2();
                }
            }
        };

        render = new SimpleRenderableComponent(transform, sprite, RenderingLayers.LayerType.CHARACTER, this);
        addComponent(transform);
        addComponent(sprite);
        addComponent(touch);
        addComponent(update);
        addComponent(render);
        collider = new ColliderComponent(new Vector2(16, 16), this) {
            @Override
            public void onCollide(ColliderComponent other) {
                if(other.getParent() instanceof Wall){
                    transform.setPosition(
                            ((int)(transform.getPosition().x + 8) / 16) * 16,
                            ((int)(transform.getPosition().y + 8) / 16) * 16);
                }
            }
        };
        addComponent(collider);
    }

    public void dispose(){
        removeComponent(transform);
        removeComponent(sprite);
        removeComponent(touch);
        removeComponent(update);
        removeComponent(render);
    }

}
