package jp.ac.titech.itpro.sdl.game.entities;

import java.util.List;

import jp.ac.titech.itpro.sdl.game.R;
import jp.ac.titech.itpro.sdl.game.Rect;
import jp.ac.titech.itpro.sdl.game.Sprite;
import jp.ac.titech.itpro.sdl.game.component.IComponent;
import jp.ac.titech.itpro.sdl.game.component.SimpleRenderableComponent;
import jp.ac.titech.itpro.sdl.game.component.SpriteComponent;
import jp.ac.titech.itpro.sdl.game.component.TransformComponent;
import jp.ac.titech.itpro.sdl.game.math.Vector2;
import jp.ac.titech.itpro.sdl.game.stage.RenderingLayers;
import jp.ac.titech.itpro.sdl.game.stage.Stage;

public class Floor extends Entity {
    SpriteComponent sprite;
    TransformComponent transform;
    SimpleRenderableComponent renderable;
    public Floor(Stage stage, Vector2 position){
        super(stage);
        transform = new TransformComponent(position);
        sprite = new SpriteComponent(R.drawable.floor, new Rect(0, 0, 16, 16));
        renderable = new SimpleRenderableComponent(transform, sprite, RenderingLayers.LayerType.BACK_GROUND);
        addComponent(transform);
        addComponent(sprite);
        addComponent(renderable);
    }
}
