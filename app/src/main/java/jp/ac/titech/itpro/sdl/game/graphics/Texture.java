package jp.ac.titech.itpro.sdl.game.graphics;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;

public class Texture {

    public enum FilterType{
        LINEAR,
        NEAREST
    }
    public final int width;
    public final int height;
    private int[] texture;
    private static int prevTexture = -1;

    public Texture(Bitmap bitmap){
        this.width = bitmap.getWidth();
        this.height = bitmap.getHeight();
        texture = new int[1];
        GLES20.glGenTextures(1, texture, 0);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture[0]);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
    }

    // 空のテクスチャの作成(フレームバッファ用)
    public Texture(int width, int height) throws Exception {
        this.width = width;
        this.height = height;
        texture = new int[1];
        GLES20.glGenTextures(1, texture, 0);
        // TODO:↓これ1回でいい
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture[0]);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, width, height, 0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null);
        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
    }

    public void setFilter(FilterType type){
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture[0]);
        switch (type){
            case NEAREST:
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
                break;
            case LINEAR:
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
                break;
        }
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
    }

    public void useTexture(){
        if(texture[0] != prevTexture){
            prevTexture = texture[0];
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture[0]);
        }
    }

    public void unbindTexture(){
        prevTexture = -1;
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
    }

    public int getTextureID(){
        return texture[0];
    }

    public void dispose(){
        GLES20.glDeleteTextures(1, texture, 0);
    }
}
