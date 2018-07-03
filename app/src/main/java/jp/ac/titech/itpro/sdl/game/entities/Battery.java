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

public class Battery extends Entity {
    public Battery(final Stage stage, Vector2 position, final int powerInId, final int powerOutId) {
        super(stage);
        TransformComponent transform = new TransformComponent(position, this);
        final SpriteComponent sprite = new SpriteComponent(R.drawable.battery,  16, 16, this);
        sprite.controller.addAnimation(sprite.controller.new AnimationData(0), "on");
        sprite.controller.addAnimation(sprite.controller.new AnimationData(1), "off");
        addComponent(transform);
        addComponent(sprite);
        addComponent(new SimpleRenderableComponent(transform, sprite, RenderingLayers.LayerType.FORE_GROUND, stage, this));
        addComponent(new ColliderComponent(new Vector2(16,16), false, 0, this));
        addComponent(new MessageReceiverComponent(this, stage){

            @Override
            protected void processMessage(Message msg){
                int msgID;
                switch (msg){
                    case POWER_SUPPLY:
                        msgID = (int)msg.getArgs()[0];
                        if (msgID == powerInId) {
                            Message msg2 = Message.POWER_STOP;
                            msg2.setArgs(new Object[]{powerOutId});
                            stage.notifyAll(msg2);
                            sprite.controller.setCurrentAnimation("off");
                        }
                        break;
                    case POWER_STOP:
                        msgID = (int)msg.getArgs()[0];
                        if (msgID == powerInId) {
                            Message msg2 = Message.POWER_SUPPLY;
                            msg2.setArgs(new Object[]{powerOutId});
                            stage.notifyAll(msg2);
                            sprite.controller.setCurrentAnimation("on");
                        }
                        break;
                }
            }
        });
    }
}
