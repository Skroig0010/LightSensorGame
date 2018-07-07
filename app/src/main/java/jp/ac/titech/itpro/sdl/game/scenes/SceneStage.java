package jp.ac.titech.itpro.sdl.game.scenes;

import jp.ac.titech.itpro.sdl.game.graphics.sprite.Sprite;
import jp.ac.titech.itpro.sdl.game.math.Vector2;
import jp.ac.titech.itpro.sdl.game.stage.Stage;

public class SceneStage implements IScene {

    private Stage stage;
    private boolean isRendering;
    private Vector2 respawnPoint = null;
    private boolean respawn = false;
    public SceneStage(){
        stage = new Stage(this, respawnPoint);
    }

    public void update(){
        stage.update();
    }
    public void render(Sprite sprite){
        isRendering = true;
        stage.render(sprite);
        if(respawn){
            stage = new Stage(this, respawnPoint);
            respawn = false;
        }
        isRendering = false;
    }

    public void respawn(Vector2 respawnPoint){
        this.respawnPoint = respawnPoint;
        respawn = true;
    }

    @Override
    public boolean isRendering() {
        return isRendering;
    }
}
