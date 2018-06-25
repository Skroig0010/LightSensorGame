package jp.ac.titech.itpro.sdl.game.stage;

import java.util.ArrayList;
import java.util.List;

import jp.ac.titech.itpro.sdl.game.entities.Entity;

public class StageMap{
    private List<List<Entity>> data;

    public StageMap(int width, int height){
        data = new ArrayList<List<Entity>>(height);
        for(int i = 0; i < height; i++){
            data.add(new ArrayList<Entity>(width));
            for(int j = 0; j < width; j++)data.get(i).add(null);
        }
    }

    public Entity get(int x, int y){
        return data.get(y).get(x);
    }

    public void set(int x, int y, Entity entity){
        data.get(y).set(x, entity);
    }
}
