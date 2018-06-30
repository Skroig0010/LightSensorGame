package jp.ac.titech.itpro.sdl.game.scenes;

import jp.ac.titech.itpro.sdl.game.Sprite;
import jp.ac.titech.itpro.sdl.game.stage.Stage;

public class SceneStage implements IScene {

    private Stage stage;
    public SceneStage(){
        stage = new Stage();
    }

    public void update(){
        stage.update();

    }
    public void render(Sprite sprite){
        stage.render(sprite);
    }
}
