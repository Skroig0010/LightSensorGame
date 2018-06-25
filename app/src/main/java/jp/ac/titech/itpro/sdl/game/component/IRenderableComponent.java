package jp.ac.titech.itpro.sdl.game.component;

import jp.ac.titech.itpro.sdl.game.Sprite;
import jp.ac.titech.itpro.sdl.game.stage.RenderingLayers;

public interface IRenderableComponent extends IComponent {
    RenderingLayers.LayerType getLayerType();
    void render(Sprite sprite);
}
