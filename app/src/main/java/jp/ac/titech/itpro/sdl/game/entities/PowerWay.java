package jp.ac.titech.itpro.sdl.game.entities;

import jp.ac.titech.itpro.sdl.game.R;
import jp.ac.titech.itpro.sdl.game.component.MessageReceiverComponent;
import jp.ac.titech.itpro.sdl.game.component.SimpleRenderableComponent;
import jp.ac.titech.itpro.sdl.game.component.SpriteComponent;
import jp.ac.titech.itpro.sdl.game.component.TransformComponent;
import jp.ac.titech.itpro.sdl.game.math.Vector2;
import jp.ac.titech.itpro.sdl.game.messages.Message;
import jp.ac.titech.itpro.sdl.game.stage.RenderingLayers;
import jp.ac.titech.itpro.sdl.game.stage.Stage;

public class PowerWay extends Entity {
    public enum Direction{
        UP_DOWN(0),
        LEFT_RIGHT(5),
        UP_RIGHT(10),
        UP_LEFT(15),
        DOWN_RIGHT(20),
        DOWN_LEFT(25);

        private int frameId;

        private Direction(int frameId){
            this.frameId = frameId;
        }

        public int getFrameId() {
            return frameId;
        }
    }
    public PowerWay(Stage stage, Vector2 position, final int powerId, Direction type) {
        super(stage);
        TransformComponent transform = new TransformComponent(position, this);
        final SpriteComponent sprite = new SpriteComponent(R.drawable.powerway,  16, 16, this);
        sprite.controller.addAnimation(sprite.controller.new AnimationData(type.getFrameId(), type.getFrameId() + 3, 100, true), "on");
        sprite.controller.addAnimation(sprite.controller.new AnimationData(type.getFrameId() + 4), "off");
        addComponent(transform);
        addComponent(sprite);
        addComponent(new SimpleRenderableComponent(transform, sprite, RenderingLayers.LayerType.BACK_GROUND, stage, this));
        addComponent(new MessageReceiverComponent(this, stage){

            @Override
            protected void processMessage(Message msg){
                int msgID;
                switch (msg){
                    case POWER_SUPPLY:
                        msgID = (int)msg.getArgs()[0];
                        if(msgID == powerId){
                            sprite.controller.setCurrentAnimation("on");
                        }
                        break;
                    case POWER_STOP:
                        msgID = (int)msg.getArgs()[0];
                        if(msgID == powerId){
                            sprite.controller.setCurrentAnimation("off");
                        }
                        break;
                }
            }
        });
    }
}
