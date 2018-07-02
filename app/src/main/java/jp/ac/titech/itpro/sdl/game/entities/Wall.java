package jp.ac.titech.itpro.sdl.game.entities;

import jp.ac.titech.itpro.sdl.game.R;
import jp.ac.titech.itpro.sdl.game.Rect;
import jp.ac.titech.itpro.sdl.game.component.ColliderComponent;
import jp.ac.titech.itpro.sdl.game.component.SimpleRenderableComponent;
import jp.ac.titech.itpro.sdl.game.component.SpriteComponent;
import jp.ac.titech.itpro.sdl.game.component.TransformComponent;
import jp.ac.titech.itpro.sdl.game.math.Vector2;
import jp.ac.titech.itpro.sdl.game.stage.RenderingLayers;
import jp.ac.titech.itpro.sdl.game.stage.Stage;

public class Wall extends Entity {
    public Wall(Stage stage, Vector2 position) {
        super(stage);
        TransformComponent transform = new TransformComponent(position, this);
        SpriteComponent sprite = new SpriteComponent(R.drawable.wall,  16, 16, this);
        sprite.controller.addAnimation(sprite.controller.new AnimationData(0), "wall");
        sprite.controller.addAnimation(sprite.controller.new AnimationData(1), "block");
        sprite.controller.addAnimation(sprite.controller.new AnimationData(2), "blank");
        addComponent(transform);
        addComponent(sprite);
        addComponent(new SimpleRenderableComponent(transform, sprite, RenderingLayers.LayerType.FORE_GROUND, stage, this));
        addComponent(new ColliderComponent(new Vector2(16,16), false, 0, this));
    }
}
