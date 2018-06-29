package jp.ac.titech.itpro.sdl.game.component;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.HashMap;
import java.util.Map;

import jp.ac.titech.itpro.sdl.game.MainActivity;
import jp.ac.titech.itpro.sdl.game.Rect;
import jp.ac.titech.itpro.sdl.game.Texture;
import jp.ac.titech.itpro.sdl.game.entities.Entity;
import jp.ac.titech.itpro.sdl.game.math.Vector2;

public class SpriteComponent implements IComponent {
    private static Map<Integer, Texture> textures = new HashMap<Integer, Texture>();
    public Rect rect;
    private Entity parent;
    private int id;

    public SpriteComponent(int id, Rect rect, Entity parent){
        this.id = id;
        if(!textures.containsKey(id)) {
            Resources res = MainActivity.instance.getResources();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;
            Bitmap bmp = BitmapFactory.decodeResource(res, id, options);
            try {
                Texture texture = new Texture(bmp);
                texture.setFilter(Texture.FilterType.NEAREST);
                textures.put(id, texture);
            } catch (Exception e) {
                System.out.println("Bitmap size must be 2^n.");
            }
        }
        this.rect = rect;
    }

    public Texture getTexture(){
        return textures.get(id);
    }

    @Override
    public Entity getParent() {
        return parent;
    }
}
