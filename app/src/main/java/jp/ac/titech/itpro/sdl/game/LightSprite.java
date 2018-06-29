package jp.ac.titech.itpro.sdl.game;

import android.opengl.GLES20;
import android.opengl.Matrix;

public class LightSprite extends Sprite {
    private int transMatrixLocation;
    private int viewportMatrixLocation;
    private int textureLocation;
    private int uvTransMatrixLocation;
    private int lightLocation;
    private int darkLocation;
    private int flagLocation;
    private float lightValue = 0;
    private float darkValue = 0;
    private boolean isBright = false;

    public LightSprite(){
        super("light");

        // Uniform変数
        viewportMatrixLocation = shaderProgram.getUniformLocation("viewportMatrix");
        transMatrixLocation = shaderProgram.getUniformLocation("transMatrix");
        textureLocation = shaderProgram.getUniformLocation("texture");
        uvTransMatrixLocation = shaderProgram.getUniformLocation("uvTransMatrix");
        lightLocation = shaderProgram.getUniformLocation("lightValue");
        darkLocation = shaderProgram.getUniformLocation("darkValue");
        flagLocation = shaderProgram.getUniformLocation("isBright");
    }

    public void setLightValue(float lightValue){
        this.lightValue = lightValue;
    }

    public void setDarkValue(float darkValue){
        this.darkValue = darkValue;
    }
    public void setIsBright(boolean isBright){
        this.isBright = isBright;
    }
    @Override
    public void render(float x, float y, Texture texture, Rect texRext) {
        render(x, y, texture, texRext, texRext.width, texRext.height);
    }

    @Override
    public void render(float x, float y, Texture texture, Rect texRext, float sizeX, float sizeY) {
        shaderProgram.useProgram();

        // サイズをテクスチャサイズに合わせて移動
        float[] transMatrix = getScaleMoveMatrix(x, y, sizeX, sizeY);

        // UVを正しい位置に移動させる行列
        float[] uvTransMatrix = getUvScaleMoveMatrix(texRext, texture);

        // Uniform変数
        texture.useTexture();
        GLES20.glUniform1i(textureLocation, 0);
        GLES20.glUniformMatrix4fv(viewportMatrixLocation, 1, false, screenMatrix, 0);
        GLES20.glUniformMatrix4fv(transMatrixLocation, 1, false, transMatrix, 0);
        GLES20.glUniformMatrix4fv(uvTransMatrixLocation, 1, false, uvTransMatrix, 0);
        GLES20.glUniform1f(lightLocation, lightValue);
        GLES20.glUniform1f(darkLocation, darkValue);
        GLES20.glUniform1f(flagLocation, (isBright) ? 1 : 0);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
    }
}
