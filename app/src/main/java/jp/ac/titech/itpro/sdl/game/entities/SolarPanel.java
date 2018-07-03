package jp.ac.titech.itpro.sdl.game.entities;

import jp.ac.titech.itpro.sdl.game.R;
import jp.ac.titech.itpro.sdl.game.Rect;
import jp.ac.titech.itpro.sdl.game.component.ColliderComponent;
import jp.ac.titech.itpro.sdl.game.component.LightSensorComponent;
import jp.ac.titech.itpro.sdl.game.component.SimpleRenderableComponent;
import jp.ac.titech.itpro.sdl.game.component.SpriteComponent;
import jp.ac.titech.itpro.sdl.game.component.TransformComponent;
import jp.ac.titech.itpro.sdl.game.component.parameters.SolerParameterComponent;
import jp.ac.titech.itpro.sdl.game.graphics.animation.AnimationController;
import jp.ac.titech.itpro.sdl.game.math.Vector2;
import jp.ac.titech.itpro.sdl.game.messages.Message;
import jp.ac.titech.itpro.sdl.game.stage.RenderingLayers;
import jp.ac.titech.itpro.sdl.game.stage.Stage;

public class SolarPanel extends Entity {
    public SolarPanel(final Stage stage, Vector2 position, int id) {
        super(stage);
        TransformComponent transform = new TransformComponent(position, this);
        final SpriteComponent sprite = new SpriteComponent(R.drawable.solar,  16, 16, this);
        sprite.controller.addAnimation(sprite.controller.new AnimationData(0), "bright");
        sprite.controller.addAnimation(sprite.controller.new AnimationData(1), "dark");
        addComponent(transform);
        addComponent(sprite);
        final SolerParameterComponent param = new SolerParameterComponent(id, this);
        addComponent(param);
        addComponent(new SimpleRenderableComponent(transform, sprite, RenderingLayers.LayerType.FORE_GROUND, stage, this));
        addComponent(new ColliderComponent(new Vector2(16,16), false, 0, this));
        // LightSensorComponentを付けて、コールバックでメッセージを吐く
        addComponent(new LightSensorComponent(this) {
            @Override
            public void onBrightnessChanged(boolean isBright) {
                if(isBright) {
                    Message msg = new Message().new POWER_SUPPLY(new Object[]{param.id});
                    stage.notifyAll(msg);
                    sprite.controller.setCurrentAnimation("bright");
                }else{
                    Message msg = new Message().new POWER_STOP(new Object[]{param.id});
                    stage.notifyAll(msg);
                    sprite.controller.setCurrentAnimation("dark");
                }
            }
        });

    }
}
