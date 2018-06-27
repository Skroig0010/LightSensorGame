package jp.ac.titech.itpro.sdl.game.math;

public class Line {
    public final Vector2 s;
    public final Vector2 e;

    public Line(Vector2 s, Vector2 e){
        this.s = s;
        this.e = e;
    }

    public Line(){
        this(new Vector2(0, 0), new Vector2(0, 0));
    }

    public Vector2 nearestPoint(Vector2 other){
        Vector2 diff = e.sub(s);
        float d2 = diff.dot(other);

        if(d2 < 0)return s;
        if(d2 > diff.lengthSquared())return e;
        return diff.normalize().scale(d2 / diff.length());
    }
}
