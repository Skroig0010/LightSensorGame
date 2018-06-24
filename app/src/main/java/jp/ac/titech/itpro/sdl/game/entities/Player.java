package jp.ac.titech.itpro.sdl.game.entities;

import java.util.ArrayList;
import java.util.List;

import jp.ac.titech.itpro.sdl.game.MainActivity;
import jp.ac.titech.itpro.sdl.game.R;
import jp.ac.titech.itpro.sdl.game.Rect;
import jp.ac.titech.itpro.sdl.game.Sprite;
import jp.ac.titech.itpro.sdl.game.component.TransformComponent;
import jp.ac.titech.itpro.sdl.game.component.IComponent;
import jp.ac.titech.itpro.sdl.game.component.IControllerComponent;
import jp.ac.titech.itpro.sdl.game.component.SpriteComponent;
import jp.ac.titech.itpro.sdl.game.component.TouchControllerComponent;

public class Player implements IEntity{
    private List<IComponent> components = new ArrayList<IComponent>();

    TransformComponent transform;
    SpriteComponent sprite;
    TouchControllerComponent touch;

    public Player(){
        transform = new TransformComponent();
        sprite = new SpriteComponent(R.drawable.human, new Rect(0,0,16,16));
        touch = new TouchControllerComponent();
        components.add(transform);
        components.add(sprite);
        components.add(touch);
    }

    @Override
    public void update(){
        double rot = Math.atan2(touch.getDirection().y, touch.getDirection().x);
        if(touch.getDirection().x * touch.getDirection().x + touch.getDirection().y * touch.getDirection().y > 50) {
            if (rot >= Math.PI / 4 && rot < Math.PI * 3 / 4) {
                // 下向き
                transform.position.y += 1;
            } else if (rot >= Math.PI * 3 / 4 && rot < Math.PI * 5 / 4) {
                // 左向き
                transform.position.x -= 1;
            } else if (rot >= Math.PI * 5 / 4 && rot < Math.PI * 7 / 4) {
                // 上向き
                transform.position.y -= 1;
            } else {
                // 右向き
                transform.position.x += 1;
            }
        }
    }

    @Override
    public void render(Sprite sprite){
        sprite.draw(
                transform.position.x,
                transform.position.y,
                this.sprite.texture,
                this.sprite.rect,
                10
        );
    }

    @Override
    public List<IComponent> getComponents() {
        return components;
    }

    @Override
    public <T extends IComponent> T getComponent(Class<T> tClass) {
        for(IComponent component : components){
            if(tClass.isInstance(component)){
                return (T)component;
            }
        }
        return null;
    }
}
