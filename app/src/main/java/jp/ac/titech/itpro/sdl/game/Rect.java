package jp.ac.titech.itpro.sdl.game;

public class Rect {
    public int x, y, width, height;

    public Rect() {
        this(0, 0, 0, 0);
    }

    public Rect(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

    }
}
