package jp.ac.titech.itpro.sdl.game.math;

public class Vector2 {
    public final float x, y;

    public Vector2(float x, float y){
        this.x = x;
        this.y = y;
    }

    public float lengthSquared(){
        return x * x + y * y;
    }

    public float length(){
        return (float)Math.sqrt(x * x + y * y);
    }

    public Vector2 normalize(){
        float len = length();
        if(len != 0) {
            return new Vector2(x / len, y / len);
        }else{
            return new Vector2(0, 0);
        }
    }

    public Vector2(){
        this(0, 0);
    }

    public Vector2 add(Vector2 other){
        return new Vector2(x + other.x, y + other.y);
    }

    public Vector2 add(float x, float y){
        return new Vector2(this.x + x, this.y + y);
    }

    public Vector2 sub(Vector2 other){
        return new Vector2(x - other.x, y - other.y);
    }

    public Vector2 sub(float x, float y){
        return new Vector2(this.x - x, this.y - y);
    }

    public float dot(Vector2 other){
        return x * other.x + y * other.y;
    }

    public float dot(float x, float y){
        return this.x * x + this.y * y;
    }

    public float cross(Vector2 other){
        return x * other.y - y - other.x;
    }

    public float cross(float x, float y){
        return this.x * y - this.y - x;
    }

    public Vector2 scale(float scalar){
        return new Vector2(x * scalar, y * scalar);
    }
}
