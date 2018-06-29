package jp.ac.titech.itpro.sdl.game;

import android.opengl.GLES20;

public class GaussSprite extends Sprite {
    private int transMatrixLocation;
    private int viewportMatrixLocation;
    private int textureLocation;
    private int uvTransMatrixLocation;
    private int horizontalLocation;
    private int weightLocation;
    private boolean horizontal;
    private float[] weight;

    public GaussSprite(){
        super("gaussian");

        // Uniform変数
        viewportMatrixLocation = shaderProgram.getUniformLocation("viewportMatrix");
        transMatrixLocation = shaderProgram.getUniformLocation("transMatrix");
        textureLocation = shaderProgram.getUniformLocation("texture");
        uvTransMatrixLocation = shaderProgram.getUniformLocation("uvTransMatrix");
        horizontalLocation = shaderProgram.getUniformLocation("horizontal");
        weightLocation = shaderProgram.getUniformLocation("weight");

        setWeight(7);
    }

    public void setHorizontal(boolean horizontal){
        this.horizontal = horizontal;
    }

    private void setWeight(double value){
        // gaussianフィルタの重み係数を算出
        weight = new float[10];
        double t = 0.0;
        double d = value * value;
        for(int i = 0; i < weight.length; i++){
            double r = 1.0 + 2.0 * i;
            double w = Math.exp(-0.5f * (r * r) / d);
            weight[i] = (float)w;
            if(i > 0){w *= 2.0;}
            t += w;
        }
        for(int i = 0; i < weight.length; i++){
            weight[i] /= t;
        }
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
        GLES20.glUniform1i(horizontalLocation, (horizontal) ? 1 : 0);
        GLES20.glUniform1fv(weightLocation, 10, weight, 0);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
    }
}
