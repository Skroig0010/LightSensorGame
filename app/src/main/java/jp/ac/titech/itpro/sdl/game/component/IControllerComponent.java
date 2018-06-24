package jp.ac.titech.itpro.sdl.game.component;

import jp.ac.titech.itpro.sdl.game.math.Vector2;

public interface IControllerComponent extends IComponent {
    enum Input{
        UP, DOWN, LEFT, RIGHT
    }
    boolean getInput(Input input);
    Vector2 getDirection();
}
