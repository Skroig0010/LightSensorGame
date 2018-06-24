package jp.ac.titech.itpro.sdl.game.component;

import jp.ac.titech.itpro.sdl.game.MainActivity;
import jp.ac.titech.itpro.sdl.game.math.Vector2;

import static jp.ac.titech.itpro.sdl.game.component.IControllerComponent.*;
import static jp.ac.titech.itpro.sdl.game.component.IControllerComponent.Input.*;

public class TouchControllerComponent implements IControllerComponent {

    private final float FLICK_THRESHOLD_VELOCITY = 32;

    private Vector2 veloc = new Vector2();

    public TouchControllerComponent(){
        MainActivity.instance.setTccCallBack(this);
    }

    @Override
    public boolean getInput(Input input) {
        switch (input){
            case RIGHT:
                return veloc.x > FLICK_THRESHOLD_VELOCITY;
            case LEFT:
                return veloc.x < -FLICK_THRESHOLD_VELOCITY;
            case UP:
                return veloc.y < -FLICK_THRESHOLD_VELOCITY;
            case DOWN:
                return veloc.y > FLICK_THRESHOLD_VELOCITY;
            default:
                return false;
        }
    }

    @Override
    public Vector2 getDirection() {
        return veloc;
    }

    public void setTouchEvent(Vector2 veloc){
        this.veloc = veloc;
    }
}
