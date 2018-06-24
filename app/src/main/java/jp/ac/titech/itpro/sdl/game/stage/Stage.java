package jp.ac.titech.itpro.sdl.game.stage;

import java.util.ArrayList;
import java.util.List;

import jp.ac.titech.itpro.sdl.game.Sprite;
import jp.ac.titech.itpro.sdl.game.entities.IEntity;
import jp.ac.titech.itpro.sdl.game.entities.Player;

public class Stage {
    private List<IEntity> entities = new ArrayList<IEntity>();
    private Player player;

    public Stage(){
        player = new Player();
        entities.add(player);
    }

    public void update() {
        for (IEntity entity:entities ) {
            entity.update();
        }
    }

    public void render(Sprite sprite) {
        for (IEntity entity:entities ) {
            entity.render(sprite);
        }
    }
}
