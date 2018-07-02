package jp.ac.titech.itpro.sdl.game.component;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.HashMap;
import java.util.Map;

import jp.ac.titech.itpro.sdl.game.MainActivity;
import jp.ac.titech.itpro.sdl.game.Rect;
import jp.ac.titech.itpro.sdl.game.graphics.Texture;
import jp.ac.titech.itpro.sdl.game.entities.Entity;
import jp.ac.titech.itpro.sdl.game.graphics.animation.AnimationController;

public class SpriteComponent implements IComponent {
    private static Map<Integer, Texture> textures = new HashMap<>();
    public Rect rect;
    private Entity parent;
    private int id;
    public final AnimationController controller = new AnimationController(this);

    public SpriteComponent(int id, int width, int height, Entity parent){
        this.id = id;
        if(!textures.containsKey(id)) {
            Resources res = MainActivity.instance.getResources();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;
            Bitmap bmp = BitmapFactory.decodeResource(res, id, options);
            Texture texture = new Texture(bmp);
            texture.setFilter(Texture.FilterType.NEAREST);
            textures.put(id, texture);
        }
        this.rect = new Rect(0, 0, width, height);
    }

    public Texture getTexture(){
        return textures.get(id);
    }

    @Override
    public Entity getParent() {
        return parent;
    }
}
