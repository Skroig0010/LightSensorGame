package jp.ac.titech.itpro.sdl.game.graphics.sprite;

import android.opengl.GLES20;

import jp.ac.titech.itpro.sdl.game.Rect;
import jp.ac.titech.itpro.sdl.game.graphics.Texture;
import jp.ac.titech.itpro.sdl.game.math.Vector2;
import jp.ac.titech.itpro.sdl.game.view.View;

public class NormalSprite extends Sprite {

    private int transMatrixLocation;
    private int viewportMatrixLocation;
    private int textureLocation;
    private int uvTransMatrixLocation;

    public NormalSprite(){
        super("main");

        // Uniform変数
        viewportMatrixLocation = shaderProgram.getUniformLocation("viewportMatrix");
        transMatrixLocation = shaderProgram.getUniformLocation("transMatrix");
        textureLocation = shaderProgram.getUniformLocation("texture");
        uvTransMatrixLocation = shaderProgram.getUniformLocation("uvTransMatrix");
    }

    @Override
    public void render(float x, float y, Texture texture, Rect texRext) {
        render(x, y, texture, texRext, texRext.width, texRext.height);
    }

    @Override
    public void render(float x, float y, Texture texture, Rect texRext, float sizeX, float sizeY) {
        shaderProgram.useProgram();

        // サイズをテクスチャサイズに合わせて移動
        // カメラの位置を取得
        Vector2 view = View.getViewPosition();
        float[] transMatrix = getScaleMoveMatrix(x - view.x, y - view.y, sizeX, sizeY);

        // UVを正しい位置に移動させる行列
        float[] uvTransMatrix = getUvScaleMoveMatrix(texRext, texture);

        // Uniform変数
        texture.useTexture();
        GLES20.glUniform1i(textureLocation, 0);
        GLES20.glUniformMatrix4fv(viewportMatrixLocation, 1, false, screenMatrix, 0);
        GLES20.glUniformMatrix4fv(transMatrixLocation, 1, false, transMatrix, 0);
        GLES20.glUniformMatrix4fv(uvTransMatrixLocation, 1, false, uvTransMatrix, 0);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
    }
}
