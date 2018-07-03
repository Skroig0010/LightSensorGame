package jp.ac.titech.itpro.sdl.game;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import jp.ac.titech.itpro.sdl.game.scenes.IScene;
import jp.ac.titech.itpro.sdl.game.scenes.SceneStage;
import jp.ac.titech.itpro.sdl.game.graphics.sprite.NormalSprite;
import jp.ac.titech.itpro.sdl.game.graphics.sprite.Sprite;

public class GLRenderer implements GLSurfaceView.Renderer {
    private Sprite sprite;
    private final Context context;

    private static int width;
    private static int height;
    private IScene scene;

    public GLRenderer(final Context context){
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        // スプライト描画クラスの作成
        sprite = new NormalSprite();
        // ここから下に作らないといけない
        scene = new SceneStage();
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        GLRenderer.width = width;
        GLRenderer.height = height;
        GLES20.glViewport(0, 0, width, height);
    }

    public static int getWidth(){
        return width;
    }

    public static int getHeight(){
        return height;
    }

    @Override
    public void onDrawFrame(GL10 unused) {
        scene.update();
        scene.render(sprite);
    }
}
