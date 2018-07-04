package jp.ac.titech.itpro.sdl.game.scenes;

import jp.ac.titech.itpro.sdl.game.graphics.sprite.Sprite;
import jp.ac.titech.itpro.sdl.game.stage.Stage;

public class SceneStage implements IScene {

    private Stage stage;
    private boolean isRendering;
    public SceneStage(){
        stage = new Stage();
    }

    public void update(){
        stage.update();

    }
    public void render(Sprite sprite){
        isRendering = true;
        stage.render(sprite);
        isRendering = false;
    }

    @Override
    public boolean isRendering() {
        return isRendering;
    }
}
