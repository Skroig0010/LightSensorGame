package jp.ac.titech.itpro.sdl.game.component;

import jp.ac.titech.itpro.sdl.game.math.Vector2;

public class TransformComponent implements IComponent {
    public Vector2 position;
    public TransformComponent(Vector2 position){
        this.position = position;
    }
    public TransformComponent(float x, float y){
        this.position = new Vector2(x, y);
    }
    public TransformComponent(){
        this(0, 0);
    }
}
