package jp.ac.titech.itpro.sdl.game.entities;

import jp.ac.titech.itpro.sdl.game.R;
import jp.ac.titech.itpro.sdl.game.Rect;
import jp.ac.titech.itpro.sdl.game.component.BrightRenderableComponent;
import jp.ac.titech.itpro.sdl.game.component.ColliderComponent;
import jp.ac.titech.itpro.sdl.game.component.FallComponent;
import jp.ac.titech.itpro.sdl.game.component.IRenderableComponent;
import jp.ac.titech.itpro.sdl.game.component.IUpdatableComponent;
import jp.ac.titech.itpro.sdl.game.component.NormalUpdatableComponent;
import jp.ac.titech.itpro.sdl.game.component.SimpleRenderableComponent;
import jp.ac.titech.itpro.sdl.game.component.TransformComponent;
import jp.ac.titech.itpro.sdl.game.component.SpriteComponent;
import jp.ac.titech.itpro.sdl.game.component.TouchControllerComponent;
import jp.ac.titech.itpro.sdl.game.graphics.animation.AnimationController;
import jp.ac.titech.itpro.sdl.game.math.Vector2;
import jp.ac.titech.itpro.sdl.game.messages.Message;
import jp.ac.titech.itpro.sdl.game.stage.RenderingLayers;
import jp.ac.titech.itpro.sdl.game.stage.Stage;
import jp.ac.titech.itpro.sdl.game.view.View;

public class Player extends Entity{

    TransformComponent transform;
    SpriteComponent sprite;
    TouchControllerComponent touch;
    IUpdatableComponent update;
    IRenderableComponent render;
    ColliderComponent collider;

    public Player(final Stage stage, Vector2 position){
        super(stage);
        transform = new TransformComponent(position,this);
        sprite = new SpriteComponent(R.drawable.human, 16,16, this);

        // アニメーション設定
        sprite.controller.addAnimation(sprite.controller.new AnimationData(0, 1, 500, true), "front");
        sprite.controller.addAnimation(sprite.controller.new AnimationData(2, 3, 500, true), "back");
        sprite.controller.addAnimation(sprite.controller.new AnimationData(4, 5, 500, true), "right");
        sprite.controller.addAnimation(sprite.controller.new AnimationData(6, 7, 500, true), "left");
        sprite.controller.setCurrentAnimation("front");

        touch = new TouchControllerComponent(this);
        final Entity parent = this;
        update = new NormalUpdatableComponent(this) {
            private final int THRESHOLD_FLICK_VELOCITY = 10;
            private Vector2 to = new Vector2();
            @Override
            public void update() {
                // 極座標系
                double rot = Math.atan2(touch.getDirection().y, touch.getDirection().x);
                float len = touch.getDirection().x * touch.getDirection().x + touch.getDirection().y * touch.getDirection().y;
                Vector2 position = transform.getLocal();
                move(rot, len);

                Vector2 dir = to.sub(position);
                float veloc = len / 50;
                if(veloc > 8)veloc = 8;
                transform.setLocal(position.add(dir.normalize().scale(veloc)));
                View.setTargetPosition(transform.getGlobal().sub(72, 112));
            }

            private void move(double rot, float len){
                if (rot < 0) rot += 2 * Math.PI;
                Vector2 currPos = transform.getLocal();
                // フリック速度が閾値を超えていたら移動
                if(len > THRESHOLD_FLICK_VELOCITY) {
                        if (rot >= Math.PI / 4 && rot < Math.PI * 3 / 4) {
                            // 上向き
                            to = new Vector2((int)(currPos.x + 8) / 16 * 16, (int)(currPos.y + 8 - 32) / 16 * 16);
                            sprite.controller.setCurrentAnimation("back");
                        } else if (rot >= Math.PI * 3 / 4 && rot < Math.PI * 5 / 4) {
                            // 右向き
                            to = new Vector2((int)(currPos.x + 8 + 32) / 16 * 16, (int)(currPos.y + 8) / 16 * 16);
                            sprite.controller.setCurrentAnimation("right");
                        } else if (rot >= Math.PI * 5 / 4 && rot < Math.PI * 7 / 4) {
                            // 下向き
                            to = new Vector2((int)(currPos.x + 8) / 16 * 16, (int)(currPos.y + 8 + 32) / 16 * 16);
                            sprite.controller.setCurrentAnimation("front");
                        } else {
                            // 左向き
                            to = new Vector2((int)(currPos.x + 8 - 32) / 16 * 16, (int)(currPos.y + 8) / 16 * 16);
                            sprite.controller.setCurrentAnimation("left");
                        }
                }else{
                    // 超えてなかったら停止
                    to = currPos;
                }
            }
        };

        render = new SimpleRenderableComponent(transform, sprite, RenderingLayers.LayerType.CHARACTER, stage, this);
        addComponent(transform);
        addComponent(sprite);
        addComponent(touch);
        addComponent(update);
        addComponent(render);
        final FallComponent fall = new FallComponent(position, transform, this);
        addComponent(fall);
        collider = new ColliderComponent(new Vector2(16, 16), false, 1, this){
            private int floorNum = 0;
            @Override
            public void enterCollide(ColliderComponent other){
                if(other.getParent() instanceof Floor
                        || other.getParent() instanceof MovableFloor
                        || other.getParent() instanceof RespawnFloor
                        || other.getParent() instanceof PowerWay
                        || other.getParent() instanceof VanishingWall){
                    floorNum++;
                }
            }

            @Override
            public void exitCollide(ColliderComponent other){
                if(other.getParent() instanceof Floor
                        || other.getParent() instanceof MovableFloor
                        || other.getParent() instanceof RespawnFloor
                        || other.getParent() instanceof PowerWay
                        || other.getParent() instanceof VanishingWall){
                    floorNum--;
                    // 0になったら落ちる
                    if(floorNum <= 0){
                        stage.notifyAll(new Message().new RESET_STAGE(new Object[]{((FallComponent)getComponent("jp.ac.titech.itpro.sdl.game.component.FallComponent")).getRespawnPosition()}));
                    }
                }
            }

            @Override
            public void onCollide(ColliderComponent other){
                View.setTargetPosition(transform.getGlobal().sub(72, 112));
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
