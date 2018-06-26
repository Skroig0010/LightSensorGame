package jp.ac.titech.itpro.sdl.game.math;

public class Vector2 {
    public final float x, y;

    public Vector2(float x, float y){
        this.x = x;
        this.y = y;
    }
    public Vector2(){
        this(0, 0);
    }

    public Vector2 add(Vector2 other){
        return new Vector2(x + other.x, y + other.y);
    }

    public Vector2 scale(float scalar){
        return new Vector2(x * scalar, y * scalar);
    }
}
