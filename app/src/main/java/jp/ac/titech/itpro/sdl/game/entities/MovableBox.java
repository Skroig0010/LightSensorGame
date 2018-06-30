package jp.ac.titech.itpro.sdl.game.entities;

import jp.ac.titech.itpro.sdl.game.R;
import jp.ac.titech.itpro.sdl.game.Rect;
import jp.ac.titech.itpro.sdl.game.component.ColliderComponent;
import jp.ac.titech.itpro.sdl.game.component.SimpleRenderableComponent;
import jp.ac.titech.itpro.sdl.game.component.SpriteComponent;
import jp.ac.titech.itpro.sdl.game.component.TransformComponent;
import jp.ac.titech.itpro.sdl.game.component.parameters.WallParameterComponent;
import jp.ac.titech.itpro.sdl.game.math.Vector2;
import jp.ac.titech.itpro.sdl.game.stage.RenderingLayers;
import jp.ac.titech.itpro.sdl.game.stage.Stage;

public class MovableBox extends Entity {
    public MovableBox(Vector2 position, Stage stage) {
        super(stage);
        TransformComponent transform = new TransformComponent(position, this);
        SpriteComponent sprite = new SpriteComponent(R.drawable.wall, new Rect(0, 0, 16, 16), this);
        addComponent(transform);
        addComponent(sprite);
        final ColliderComponent collider = new ColliderComponent(new Vector2(16,16), false, 1, this);
        addComponent(new SimpleRenderableComponent(transform, sprite, RenderingLayers.LayerType.FORE_GROUND, this));
        addComponent(collider);
    }
}
