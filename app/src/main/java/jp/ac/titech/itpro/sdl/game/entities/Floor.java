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

public class Floor extends Entity {
    public Floor(Stage stage, Vector2 position){
        super(stage);
        TransformComponent transform = new TransformComponent(position, this);
        SpriteComponent sprite = new SpriteComponent(R.drawable.floor,  16, 16, this);
        addComponent(transform);
        addComponent(sprite);
        addComponent(new SimpleRenderableComponent(transform, sprite, RenderingLayers.LayerType.BACK_GROUND, stage, this));
        addComponent(new ColliderComponent(new Vector2(16, 16), this));
    }
}
