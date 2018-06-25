package jp.ac.titech.itpro.sdl.game.entities;

import java.util.ArrayList;
import java.util.List;

import jp.ac.titech.itpro.sdl.game.MainActivity;
import jp.ac.titech.itpro.sdl.game.R;
import jp.ac.titech.itpro.sdl.game.Rect;
import jp.ac.titech.itpro.sdl.game.Sprite;
import jp.ac.titech.itpro.sdl.game.component.IRenderableComponent;
import jp.ac.titech.itpro.sdl.game.component.IUpdatableComponent;
import jp.ac.titech.itpro.sdl.game.component.NormalUpdatableComponent;
import jp.ac.titech.itpro.sdl.game.component.SimpleRenderableComponent;
import jp.ac.titech.itpro.sdl.game.component.TransformComponent;
import jp.ac.titech.itpro.sdl.game.component.IComponent;
import jp.ac.titech.itpro.sdl.game.component.IControllerComponent;
import jp.ac.titech.itpro.sdl.game.component.SpriteComponent;
import jp.ac.titech.itpro.sdl.game.component.TouchControllerComponent;
import jp.ac.titech.itpro.sdl.game.stage.RenderingLayers;
import jp.ac.titech.itpro.sdl.game.stage.Stage;

public class Player extends Entity{

    TransformComponent transform;
    SpriteComponent sprite;
    TouchControllerComponent touch;
    IUpdatableComponent update;
    IRenderableComponent render;

    public Player(Stage stage){
        super(stage);
        transform = new TransformComponent(this);
        sprite = new SpriteComponent(R.drawable.human, new Rect(0,0,16,16), this);
        touch = new TouchControllerComponent(this);
        final Entity parent = this;
        update = new NormalUpdatableComponent(this) {
            @Override
            public void update() {
                double rot = Math.atan2(touch.getDirection().y, touch.getDirection().x);
                if(rot < 0)rot += 2 * Math.PI;
                if(touch.getDirection().x * touch.getDirection().x + touch.getDirection().y * touch.getDirection().y > 50) {
                    if (rot >= Math.PI / 4 && rot < Math.PI * 3 / 4) {
                        // 下向き
                        // TODO:移動処理が冗長なのを直す
                        transform.setPosition(transform.getPosition().x, transform.getPosition().y + 1);
                    } else if (rot >= Math.PI * 3 / 4 && rot < Math.PI * 5 / 4) {
                        // 左向き
                        transform.setPosition(transform.getPosition().x - 1, transform.getPosition().y);
                    } else if (rot >= Math.PI * 5 / 4 && rot < Math.PI * 7 / 4) {
                        // 上向き
                        transform.setPosition(transform.getPosition().x, transform.getPosition().y - 1);
                    } else {
                        // 右向き
                        transform.setPosition(transform.getPosition().x + 1, transform.getPosition().y);
                    }
                }
            }
        };
        render = new SimpleRenderableComponent(transform, sprite, RenderingLayers.LayerType.CHARACTER, this);
        addComponent(transform);
        addComponent(sprite);
        addComponent(touch);
        addComponent(update);
        addComponent(render);
    }

    public void dispose(){
        removeComponent(transform);
        removeComponent(sprite);
        removeComponent(touch);
        removeComponent(update);
        removeComponent(render);
    }

}
