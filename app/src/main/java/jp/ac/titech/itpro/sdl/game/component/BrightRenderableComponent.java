package jp.ac.titech.itpro.sdl.game.component;

import jp.ac.titech.itpro.sdl.game.Sprite;
import jp.ac.titech.itpro.sdl.game.entities.Entity;
import jp.ac.titech.itpro.sdl.game.stage.RenderingLayers;

public class BrightRenderableComponent extends SimpleRenderableComponent {
    public BrightRenderableComponent(TransformComponent transform, SpriteComponent sprite, RenderingLayers.LayerType type, Entity parent){
        super(transform, sprite, type, parent);
    }
}
