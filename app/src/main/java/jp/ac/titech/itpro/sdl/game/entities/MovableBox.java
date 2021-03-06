package jp.ac.titech.itpro.sdl.game.entities;

import jp.ac.titech.itpro.sdl.game.R;
import jp.ac.titech.itpro.sdl.game.Rect;
import jp.ac.titech.itpro.sdl.game.component.ColliderComponent;
import jp.ac.titech.itpro.sdl.game.component.FallComponent;
import jp.ac.titech.itpro.sdl.game.component.IUpdatableComponent;
import jp.ac.titech.itpro.sdl.game.component.NormalUpdatableComponent;
import jp.ac.titech.itpro.sdl.game.component.SimpleRenderableComponent;
import jp.ac.titech.itpro.sdl.game.component.SpriteComponent;
import jp.ac.titech.itpro.sdl.game.component.TransformComponent;
import jp.ac.titech.itpro.sdl.game.component.parameters.WallParameterComponent;
import jp.ac.titech.itpro.sdl.game.math.Vector2;
import jp.ac.titech.itpro.sdl.game.stage.RenderingLayers;
import jp.ac.titech.itpro.sdl.game.stage.Stage;

public class MovableBox extends Entity {
    public MovableBox(Vector2 position, Stage stage) {
        super(stage);
        final TransformComponent transform = new TransformComponent(position, this);
        SpriteComponent sprite = new SpriteComponent(R.drawable.movablebox, 16, 16, this);
        addComponent(transform);
        addComponent(sprite);
        final FallComponent fall = new FallComponent(position, transform, this);
        addComponent(fall);
        final ColliderComponent collider = new ColliderComponent(new Vector2(16,16), false, 1, this){
            private int floorNum = 0;
            @Override
            public void enterCollide(ColliderComponent other){
                if(other.getParent() instanceof Floor
                        || other.getParent() instanceof MovableFloor
                        || other.getParent() instanceof RespawnFloor
                        || other.getParent() instanceof PowerWay
                        || other.getParent() instanceof VanishingWall){
                    floorNum++;
                }
            }

            @Override
            public void exitCollide(ColliderComponent other){
                if(other.getParent() instanceof Floor
                        || other.getParent() instanceof MovableFloor
                        || other.getParent() instanceof RespawnFloor
                        || other.getParent() instanceof PowerWay
                        || other.getParent() instanceof VanishingWall){
                    floorNum--;
                    // 0になったら落ちる
                    if(floorNum <= 0){
                        fall.fall();
                    }
                }
            }
            @Override
            public void onCollide(ColliderComponent other){
                if(other.getParent() instanceof Player) {
                    Vector2 currPos = transform.getGlobal();
                    Vector2 moveDirection = currPos.sub(other.getTransform().getGlobal());
                    if (Math.abs(moveDirection.x) > Math.abs(moveDirection.y)) {
                        // x方向に動かすのでyのズレを消す
                        transform.setGlobal(currPos.x, (int) (currPos.y + 8) / 16 * 16);
                    } else {
                        // y方向に動かすのでxのズレを消す
                        transform.setGlobal((int) (currPos.x + 8) / 16 * 16, currPos.y);
                    }
                }
            }
        };
        addComponent(new SimpleRenderableComponent(transform, sprite, RenderingLayers.LayerType.CHARACTER, stage, this));
        addComponent(collider);
    }
}
