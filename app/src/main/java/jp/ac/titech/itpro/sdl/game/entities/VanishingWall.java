package jp.ac.titech.itpro.sdl.game.entities;

import jp.ac.titech.itpro.sdl.game.R;
import jp.ac.titech.itpro.sdl.game.component.ColliderComponent;
import jp.ac.titech.itpro.sdl.game.component.IRenderableComponent;
import jp.ac.titech.itpro.sdl.game.component.MessageReceiverComponent;
import jp.ac.titech.itpro.sdl.game.component.SimpleRenderableComponent;
import jp.ac.titech.itpro.sdl.game.component.SpriteComponent;
import jp.ac.titech.itpro.sdl.game.component.TransformComponent;
import jp.ac.titech.itpro.sdl.game.component.parameters.WallParameterComponent;
import jp.ac.titech.itpro.sdl.game.math.Vector2;
import jp.ac.titech.itpro.sdl.game.messages.Message;
import jp.ac.titech.itpro.sdl.game.stage.RenderingLayers;
import jp.ac.titech.itpro.sdl.game.stage.Stage;

public class VanishingWall extends Entity {
    public VanishingWall(Stage stage, Vector2 position, int[] switchIds, int powerId) {
        super(stage);
        TransformComponent transform = new TransformComponent(position, this);
        final SpriteComponent sprite = new SpriteComponent(R.drawable.vanishingwall,  16, 16, this);
        sprite.controller.addAnimation(sprite.controller.new AnimationData(0), "on");
        sprite.controller.addAnimation(sprite.controller.new AnimationData(1), "off");
        sprite.controller.addAnimation(sprite.controller.new AnimationData(2), "on-dark");
        sprite.controller.addAnimation(sprite.controller.new AnimationData(3), "off-dark");
        addComponent(transform);
        final ColliderComponent collider = new ColliderComponent(new Vector2(16,16), false, 0, this);
        final WallParameterComponent param = new WallParameterComponent(switchIds, switchIds.length, powerId, this);
        addComponent(sprite);
        final IRenderableComponent renderable = new SimpleRenderableComponent(transform, sprite, RenderingLayers.LayerType.FORE_GROUND, stage, this);
        addComponent(renderable);
        addComponent(collider);
        addComponent(param);
        addComponent(new MessageReceiverComponent(this, stage){
            private int pressedSwitchNum = 0;
            private boolean powerSupplied = false;

            @Override
            protected void afterProcess(){
                // 電源が供給されている、かつ
                // 必要数以上ボタンが押されていたら通り抜けられるようにする
                if (powerSupplied && pressedSwitchNum >= param.nSwitchRequired) {
                    collider.isTrigger = true;
                    sprite.controller.setCurrentAnimation("off");
                    renderable.setLayerType(RenderingLayers.LayerType.BACK_GROUND);
                } else {
                    collider.isTrigger = false;
                    renderable.setLayerType(RenderingLayers.LayerType.FORE_GROUND);
                    sprite.controller.setCurrentAnimation("on");
                    sprite.controller.setCurrentAnimation("on-dark");
                }
            }

            @Override
            protected void processMessage(Message msg) {
                int msgID;
                if (msg instanceof Message.BUTTON_PRESSED) {
                    msgID = (int) msg.getArgs()[0];
                    for (int id : param.switchIds) {
                        if (msgID == id) {
                            pressedSwitchNum++;
                            break;
                        }
                    }
                }else if (msg instanceof Message.BUTTON_RELEASED) {
                    msgID = (int) msg.getArgs()[0];
                    for (int id : param.switchIds) {
                        if (msgID == id) {
                            pressedSwitchNum--;
                            break;
                        }
                    }
                }else if (msg instanceof Message.POWER_SUPPLY) {
                    msgID = (int) msg.getArgs()[0];
                    if (msgID == param.powerId) {
                        powerSupplied = true;
                    }
                }else if (msg instanceof Message.POWER_STOP) {
                    msgID = (int) msg.getArgs()[0];
                    if (msgID == param.powerId) {
                        powerSupplied = false;
                    }
                }
            }
        });
    }
}
