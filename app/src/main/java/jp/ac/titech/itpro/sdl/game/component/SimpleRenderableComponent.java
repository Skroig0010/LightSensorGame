package jp.ac.titech.itpro.sdl.game.component;

import jp.ac.titech.itpro.sdl.game.Sprite;
import jp.ac.titech.itpro.sdl.game.stage.RenderingLayers;

public class SimpleRenderableComponent implements IRenderableComponent {
    private TransformComponent transform;
    private SpriteComponent sprite;
    private RenderingLayers.LayerType type;

    public SimpleRenderableComponent(TransformComponent transform, SpriteComponent sprite, RenderingLayers.LayerType type){
        this.transform = transform;
        this.sprite = sprite;
        this.type = type;
    }

    @Override
    public RenderingLayers.LayerType getLayerType() {
        return type;
    }

    @Override
    public void render(Sprite sprite) {
        sprite.draw(
                transform.position.x,
                transform.position.y,
                this.sprite.texture,
                this.sprite.rect,
               4
        );

    }
}
