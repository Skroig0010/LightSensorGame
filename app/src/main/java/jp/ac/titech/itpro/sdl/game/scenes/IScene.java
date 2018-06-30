package jp.ac.titech.itpro.sdl.game.scenes;

import jp.ac.titech.itpro.sdl.game.Sprite;

public interface IScene {
    void update();
    void render(Sprite sprite);
}
