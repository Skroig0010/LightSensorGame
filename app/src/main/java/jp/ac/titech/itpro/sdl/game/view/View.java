package jp.ac.titech.itpro.sdl.game.view;

import jp.ac.titech.itpro.sdl.game.math.Vector2;

public class View {
    private static Vector2 targetPosition = new Vector2();
    private static Vector2 viewPosition = new Vector2();
    private static boolean shake = false;

    public static void update(){
        Vector2 shakeDiff;
        if(shake){
            shakeDiff = new Vector2( (float)Math.random() * 5, (float)Math.random() * 5);
        }else{
            shakeDiff = new Vector2();
        }
        viewPosition = targetPosition.add(shakeDiff);
    }

    public static void setShake(boolean _shake) {
        shake = _shake;
    }

    public static Vector2 getViewPosition() {
        return viewPosition;
    }

    public static void setTargetPosition(Vector2 _targetPosition) {
        targetPosition = _targetPosition;
    }
}
