package jp.ac.titech.itpro.sdl.game.component;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import jp.ac.titech.itpro.sdl.game.MainActivity;
import jp.ac.titech.itpro.sdl.game.Rect;
import jp.ac.titech.itpro.sdl.game.Texture;
import jp.ac.titech.itpro.sdl.game.math.Vector2;

public class SpriteComponent implements IComponent {
    public Texture texture;
    public Rect rect;

    public SpriteComponent(int id, Rect rect){
        Resources res = MainActivity.instance.getResources();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        Bitmap bmp = BitmapFactory.decodeResource(res, id, options);
        try {
            texture = new Texture(bmp);
        }catch (Exception e){
            System.out.println("Bitmap size must be 2^n.");
        }
        this.rect = rect;
    }
}
