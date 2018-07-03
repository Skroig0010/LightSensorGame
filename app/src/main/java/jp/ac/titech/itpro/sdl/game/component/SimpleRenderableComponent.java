package jp.ac.titech.itpro.sdl.game.component;

import jp.ac.titech.itpro.sdl.game.graphics.sprite.Sprite;
import jp.ac.titech.itpro.sdl.game.entities.Entity;
import jp.ac.titech.itpro.sdl.game.stage.RenderingLayers;
import jp.ac.titech.itpro.sdl.game.stage.Stage;

public class SimpleRenderableComponent implements IRenderableComponent {
    private TransformComponent transform;
    private SpriteComponent sprite;
    private RenderingLayers.LayerType type;
    private Entity parent;
    private Stage stage;
    public SimpleRenderableComponent(TransformComponent transform, SpriteComponent sprite, RenderingLayers.LayerType type, Stage stage, Entity parent){
        this.transform = transform;
        this.sprite = sprite;
        this.type = type;
        this.stage = stage;
        this.parent = parent;
    }

    @Override
    public RenderingLayers.LayerType getLayerType() {
        return type;
    }

    @Override
    public TransformComponent getTransform() {
        return transform;
    }

    @Override
    public void setLayerType(RenderingLayers.LayerType type) {
        if(this.type != type) {
            stage.changeRnderingLayer(this, type);
            this.type = type;
        }
    }


    @Override
    public void render(Sprite sprite) {
        sprite.render(
                transform.getGlobal().x,
                transform.getGlobal().y,
                this.sprite.getTexture(),
                this.sprite.rect
        );

    }

    @Override
    public Entity getParent() {
        return parent;
    }
}
