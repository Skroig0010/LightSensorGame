package jp.ac.titech.itpro.sdl.game.component;

import jp.ac.titech.itpro.sdl.game.graphics.sprite.Sprite;
import jp.ac.titech.itpro.sdl.game.stage.RenderingLayers;

public interface IRenderableComponent extends IComponent {
    RenderingLayers.LayerType getLayerType();
    TransformComponent getTransform();
    void setLayerType(RenderingLayers.LayerType type);
    void render(Sprite sprite);
}
