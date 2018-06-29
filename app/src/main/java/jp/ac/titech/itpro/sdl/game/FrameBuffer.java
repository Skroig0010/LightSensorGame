package jp.ac.titech.itpro.sdl.game;

import android.opengl.GLES20;
import android.widget.Toast;

public class FrameBuffer {
    private int[] frameBuffer;
    private int[] renderBuffer;
    private Texture texture;
    public final int width;
    public final int height;

    public FrameBuffer(int width, int height){
        this.width = width;
        this.height = height;
        frameBuffer = new int[1];
        renderBuffer = new int[1];
        GLES20.glGenFramebuffers(1, frameBuffer, 0);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frameBuffer[0]);

        GLES20.glGenRenderbuffers(1, renderBuffer, 0);
        GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER, renderBuffer[0]);

        GLES20.glRenderbufferStorage(GLES20.GL_RENDERBUFFER, GLES20.GL_DEPTH_COMPONENT16, width, height);
        GLES20.glFramebufferRenderbuffer(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_RENDERBUFFER, renderBuffer[0]);

        try {
            texture = new Texture(width,height);
            texture.setFilter(Texture.FilterType.LINEAR);
        }catch (Exception e){
            Toast.makeText(MainActivity.instance, "画像サイズが2べきじゃない", Toast.LENGTH_LONG);
        }
        texture.useTexture();

        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D, texture.getTextureID(), 0);

        texture.unbindTexture();
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frameBuffer[0]);
        GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER, renderBuffer[0]);
    }

    public void bindFrameBuffer(){
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frameBuffer[0]);
    }

    public void unbindFrameBuffer(){
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
    }

    public Texture getTexture() {
        return texture;
    }
}
