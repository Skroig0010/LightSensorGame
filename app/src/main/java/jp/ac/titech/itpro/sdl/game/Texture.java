package jp.ac.titech.itpro.sdl.game;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;

public class Texture {
    public final int width;
    public final int height;
    private int[] texture;

    private boolean isPowerOf2(int v) {
        for(int i = 1; i!=0; i<<=1) if(i>=v)return true;
        return false;
    }


    public Texture(Bitmap bitmap) throws Exception {
        this.width = bitmap.getWidth();
        this.height = bitmap.getHeight();
        texture = new int[1];
        GLES20.glGenTextures(1, texture, 0);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture[0]);
        // TODO:フィルタ設定をする
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

        if(!(isPowerOf2(bitmap.getWidth()) && isPowerOf2(bitmap.getHeight()))){
            throw new Exception();
        }
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
    }

    public void useTexture(){
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture[0]);
    }



    public void dispose(){
        GLES20.glDeleteTextures(1, texture, 0);
    }
}
