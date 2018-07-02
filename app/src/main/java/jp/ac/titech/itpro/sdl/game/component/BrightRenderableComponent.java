package jp.ac.titech.itpro.sdl.game.component;

import jp.ac.titech.itpro.sdl.game.graphics.sprite.LightSprite;
import jp.ac.titech.itpro.sdl.game.graphics.sprite.Sprite;
import jp.ac.titech.itpro.sdl.game.entities.Entity;
import jp.ac.titech.itpro.sdl.game.stage.RenderingLayers;
import jp.ac.titech.itpro.sdl.game.stage.Stage;

public class BrightRenderableComponent extends SimpleRenderableComponent {
    public BrightRenderableComponent(TransformComponent transform, SpriteComponent sprite, RenderingLayers.LayerType type, Stage stage, Entity parent){
        super(transform, sprite, type, stage, parent);
    }

    @Override
    public void render(Sprite sprite){
        if(sprite instanceof LightSprite) {
            ((LightSprite)sprite).setIsBright(true);
            super.render(sprite);
            ((LightSprite)sprite).setIsBright(false);
        }else{
            super.render(sprite);
        }

    }
}
