package jp.ac.titech.itpro.sdl.game.entities;

import jp.ac.titech.itpro.sdl.game.R;
import jp.ac.titech.itpro.sdl.game.component.ColliderComponent;
import jp.ac.titech.itpro.sdl.game.component.SimpleRenderableComponent;
import jp.ac.titech.itpro.sdl.game.component.SpriteComponent;
import jp.ac.titech.itpro.sdl.game.component.TransformComponent;
import jp.ac.titech.itpro.sdl.game.component.parameters.SwitchParameterComponent;
import jp.ac.titech.itpro.sdl.game.math.Vector2;
import jp.ac.titech.itpro.sdl.game.messages.Message;
import jp.ac.titech.itpro.sdl.game.stage.RenderingLayers;
import jp.ac.titech.itpro.sdl.game.stage.Stage;

public class Button extends Entity {
    public Button(final Stage stage, Vector2 position, boolean canRelease, final int id) {
        super(stage);
        TransformComponent transform = new TransformComponent(position, this);
        final SpriteComponent sprite = new SpriteComponent(R.drawable.button,  16, 16, this);
        sprite.controller.addAnimation(sprite.controller.new AnimationData(0), "released");
        sprite.controller.addAnimation(sprite.controller.new AnimationData(1), "pressed");
        final SwitchParameterComponent param = new SwitchParameterComponent(canRelease, id, this);
        addComponent(transform);
        addComponent(sprite);
        addComponent(new SimpleRenderableComponent(transform, sprite, RenderingLayers.LayerType.CHARACTER_UNDER, stage, this));
        addComponent(param);
        addComponent(new ColliderComponent(new Vector2(14,14), this){

            private int colliderNum = 0;

            @Override
            public void enterCollide(ColliderComponent other){
                colliderNum++;
                if(colliderNum == 1) {
                    // プレイヤーに踏まれたら
                    Message msg = Message.BUTTON_PRESSED;
                    msg.setArgs(new Object[]{param.id});
                    stage.notifyAll(msg);
                    sprite.controller.setCurrentAnimation("pressed");
                }
            }

            @Override
            public void exitCollide(ColliderComponent other){
                // プレイヤーが押していたのが離れたら
                if(param.canRelease){
                    colliderNum--;
                    if(colliderNum == 0) {
                        Message msg = Message.BUTTON_RELEASED;
                        msg.setArgs(new Object[]{param.id});
                        stage.notifyAll(msg);
                        sprite.controller.setCurrentAnimation("released");
                    }
                }
            }
        });
    }
}
