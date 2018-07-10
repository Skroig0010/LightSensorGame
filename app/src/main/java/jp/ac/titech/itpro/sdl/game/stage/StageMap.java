package jp.ac.titech.itpro.sdl.game.stage;

import java.util.ArrayList;
import java.util.List;

import jp.ac.titech.itpro.sdl.game.entities.Entity;

public class StageMap<T>{
    private List<List<T>> data;

    public final int width;
    public final int height;

    public StageMap(int width, int height){
        this.width = width;
        this.height = height;
        data = new ArrayList<List<T>>(height);
        for(int i = 0; i < height; i++){
            data.add(new ArrayList<T>(width));
            for(int j = 0; j < width; j++)data.get(i).add(null);
        }
    }

    public T get(int x, int y){
        return data.get(y).get(x);
    }

    public void set(int x, int y, T data){
        this.data.get(y).set(x, data);
    }
}
