package jp.ac.titech.itpro.sdl.game.component;

import jp.ac.titech.itpro.sdl.game.Sprite;
import jp.ac.titech.itpro.sdl.game.entities.Entity;
import jp.ac.titech.itpro.sdl.game.stage.RenderingLayers;

public class SimpleRenderableComponent implements IRenderableComponent {
    private TransformComponent transform;
    private SpriteComponent sprite;
    private RenderingLayers.LayerType type;
    private Entity parent;

    public SimpleRenderableComponent(TransformComponent transform, SpriteComponent sprite, RenderingLayers.LayerType type, Entity parent){
        this.transform = transform;
        this.sprite = sprite;
        this.type = type;
        this.parent = parent;
    }

    @Override
    public RenderingLayers.LayerType getLayerType() {
        return type;
    }

    @Override
    public void render(Sprite sprite) {
        sprite.render(
                transform.getPosition().x,
                transform.getPosition().y,
                this.sprite.getTexture(),
                this.sprite.rect
        );

    }

    @Override
    public Entity getParent() {
        return parent;
    }
}
