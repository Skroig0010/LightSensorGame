package jp.ac.titech.itpro.sdl.game.entities;

import jp.ac.titech.itpro.sdl.game.R;
import jp.ac.titech.itpro.sdl.game.component.ColliderComponent;
import jp.ac.titech.itpro.sdl.game.component.MessageReceiverComponent;
import jp.ac.titech.itpro.sdl.game.component.SimpleRenderableComponent;
import jp.ac.titech.itpro.sdl.game.component.SpriteComponent;
import jp.ac.titech.itpro.sdl.game.component.TransformComponent;
import jp.ac.titech.itpro.sdl.game.math.Vector2;
import jp.ac.titech.itpro.sdl.game.messages.Message;
import jp.ac.titech.itpro.sdl.game.stage.RenderingLayers;
import jp.ac.titech.itpro.sdl.game.stage.Stage;

public class PowerWayWall extends Entity {
    public PowerWayWall(Stage stage, Vector2 position, final int powerId, PowerWay.Direction type) {
        super(stage);
        TransformComponent transform = new TransformComponent(position, this);
        final SpriteComponent sprite = new SpriteComponent(R.drawable.powerwaywall,  16, 16, this);
        sprite.controller.addAnimation(sprite.controller.new AnimationData(type.getFrameId(), type.getFrameId() + 3, 100, true), "on");
        sprite.controller.addAnimation(sprite.controller.new AnimationData(type.getFrameId() + 4), "off");
        sprite.controller.setCurrentAnimation("on");
        addComponent(transform);
        addComponent(sprite);
        addComponent(new ColliderComponent(new Vector2(16,16), false, 0, this));
        addComponent(new SimpleRenderableComponent(transform, sprite, RenderingLayers.LayerType.FORE_GROUND, stage, this));
        addComponent(new MessageReceiverComponent(this, stage){

            @Override
            protected void processMessage(Message msg) {
                int msgID;
                if (msg instanceof Message.POWER_SUPPLY){
                    msgID = (int) msg.getArgs()[0];
                    if (msgID == powerId) {
                        sprite.controller.setCurrentAnimation("on");
                    }
                }
                if(msg instanceof Message.POWER_STOP) {
                    msgID = (int) msg.getArgs()[0];
                    if (msgID == powerId) {
                        sprite.controller.setCurrentAnimation("off");
                    }
                }
            }
        });
    }
}
