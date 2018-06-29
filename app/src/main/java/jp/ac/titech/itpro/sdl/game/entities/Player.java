package jp.ac.titech.itpro.sdl.game.entities;

import jp.ac.titech.itpro.sdl.game.R;
import jp.ac.titech.itpro.sdl.game.Rect;
import jp.ac.titech.itpro.sdl.game.component.BrightRenderableComponent;
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
                // 極座標系
                double rot = Math.atan2(touch.getDirection().y, touch.getDirection().x);
                float len = touch.getDirection().x * touch.getDirection().x + touch.getDirection().y * touch.getDirection().y;
                move(rot, len);
                // マスに沿った位置にいるなら移動を許可
                // if((int)transform.getPosition().x % 16 == 0 && (int)transform.getPosition().y % 16 == 0){
                // }
                transform.setPosition(transform.getPosition().add(veloc));
            }

            private void move(double rot, float len){
                if (rot < 0) rot += 2 * Math.PI;
                // フリック速度が閾値を超えていたら移動
                if(len > THRESHOLD_FLICK_VELOCITY) {
                        if (rot >= Math.PI / 4 && rot < Math.PI * 3 / 4) {
                            // 下向き
                            veloc = new Vector2(0, 3);
                        } else if (rot >= Math.PI * 3 / 4 && rot < Math.PI * 5 / 4) {
                            // 左向き
                            veloc = new Vector2(-3, 0);
                        } else if (rot >= Math.PI * 5 / 4 && rot < Math.PI * 7 / 4) {
                            // 上向き
                            veloc = new Vector2(0, -3);
                        } else {
                            // 右向き
                            veloc = new Vector2(3, 0);
                        }
                }else{
                    // 超えてなかったら停止
                    veloc = new Vector2();
                }
            }
        };

        render = new BrightRenderableComponent(transform, sprite, RenderingLayers.LayerType.CHARACTER, this);
        addComponent(transform);
        addComponent(sprite);
        addComponent(touch);
        addComponent(update);
        addComponent(render);
        collider = new ColliderComponent(new Vector2(16, 16), false, 1, this);
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
