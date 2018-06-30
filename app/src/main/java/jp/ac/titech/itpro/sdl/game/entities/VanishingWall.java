package jp.ac.titech.itpro.sdl.game.entities;

import jp.ac.titech.itpro.sdl.game.R;
import jp.ac.titech.itpro.sdl.game.Rect;
import jp.ac.titech.itpro.sdl.game.component.ColliderComponent;
import jp.ac.titech.itpro.sdl.game.component.MessageReceiverComponent;
import jp.ac.titech.itpro.sdl.game.component.SimpleRenderableComponent;
import jp.ac.titech.itpro.sdl.game.component.SpriteComponent;
import jp.ac.titech.itpro.sdl.game.component.TransformComponent;
import jp.ac.titech.itpro.sdl.game.component.parameters.WallParameterComponent;
import jp.ac.titech.itpro.sdl.game.math.Vector2;
import jp.ac.titech.itpro.sdl.game.messages.Message;
import jp.ac.titech.itpro.sdl.game.stage.RenderingLayers;
import jp.ac.titech.itpro.sdl.game.stage.Stage;

public class VanishingWall extends Entity {
    public VanishingWall(Stage stage, Vector2 position, int id, int nSwitchRequired) {
        super(stage);
        TransformComponent transform = new TransformComponent(position, this);
        SpriteComponent sprite = new SpriteComponent(R.drawable.wall, new Rect(0, 0, 16, 16), this);
        addComponent(transform);
        final ColliderComponent collider = new ColliderComponent(new Vector2(16,16), false, 0, this);
        final WallParameterComponent param = new WallParameterComponent(id, nSwitchRequired, this);
        addComponent(sprite);
        addComponent(new SimpleRenderableComponent(transform, sprite, RenderingLayers.LayerType.FORE_GROUND, this));
        addComponent(collider);
        addComponent(param);
        addComponent(new MessageReceiverComponent(this, stage){
            private int pressedSwitchNum = 0;

            @Override
            protected void afterProcess(){
                // 必要数以上ボタンが押されていたら通り抜けられるようにする
                if(pressedSwitchNum >= param.nSwitchRequired){
                    collider.isTrigger = true;
                }else{
                    collider.isTrigger = false;
                }
            }

            @Override
            protected void processMessage(Message msg){
                int id;
                switch (msg){
                    case SWITCH_PRESSED:
                        id = (int)msg.getArgs()[0];
                        if(id == param.id){
                            pressedSwitchNum++;
                        }
                        break;
                    case SWITCH_RELEASED:
                        id = (int)msg.getArgs()[0];
                        if(id == param.id){
                            pressedSwitchNum--;
                        }
                        break;
                }
            }
        });
    }
}
