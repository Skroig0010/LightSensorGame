package jp.ac.titech.itpro.sdl.game;

import android.opengl.GLES20;
import android.opengl.Matrix;

import java.nio.FloatBuffer;

public abstract class Sprite {
    private float vertices[] = {
            0.0f, 0.0f, 0.0f,
            1.0f, 0.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            1.0f, 1.0f, 0.0f
    };

    private float uvs[] = {
            0.0f, 0.0f,
            1.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f
    };
    private FloatBuffer vertexBuffer;
    private FloatBuffer uvBuffer;

    protected Shader shaderProgram;

    private int positionAttribute;
    private int uvAttribute;

    public final int width = 160;
    public final int height = 240;

    protected float screenMatrix[] = {
            2.0f / width, 0, 0, 0,
            0, -2.0f / height, 0, 0,
            0, 0, 1, 0,
            -1, 1, 0, 1
    };

    public Sprite(String shaderName) {
        // シェーダーの読み込み
        shaderProgram = new Shader(shaderName);

        // 頂点、UVのバッファ確保
        vertexBuffer = BufferUtil.convert(vertices);
        uvBuffer = BufferUtil.convert(uvs);

        // Attribute変数
        positionAttribute = shaderProgram.getAttributeLocation("vPosition");
        GLES20.glEnableVertexAttribArray(positionAttribute);
        GLES20.glVertexAttribPointer(positionAttribute, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer);
        uvAttribute = shaderProgram.getAttributeLocation("vUv");
        GLES20.glEnableVertexAttribArray(uvAttribute);
        GLES20.glVertexAttribPointer(uvAttribute, 2, GLES20.GL_FLOAT, false, 0, uvBuffer);

        // アルファブレンド有効化
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

    }

    public float[] getScaleMoveMatrix(float x, float y, float sizeX, float sizeY){
        // サイズをテクスチャサイズに合わせて移動
        float[] transMatrix = new float[32];
        float[] retMatrix = new float[16];
        Matrix.setIdentityM(retMatrix, 0);
        Matrix.setIdentityM(transMatrix, 0);
        Matrix.setIdentityM(transMatrix, 16);
        Matrix.scaleM(transMatrix, 0, sizeX, sizeY, 0);
        Matrix.translateM(transMatrix, 16, x, y, 0);
        Matrix.multiplyMM(retMatrix, 0, transMatrix, 16, transMatrix, 0);
        return retMatrix;
    }

    public float[] getUvScaleMoveMatrix(Rect texRext, Texture texture) {
        float[] uvTransMatrix = new float[32];
        float[] retMatrix = new float[16];
        float uvWidth = (float) texRext.width / texture.width;
        float uvHeight = (float) texRext.height / texture.height;
        float uvX = (float) texRext.x / texture.width;
        float uvY = (float) texRext.y / texture.height;
        Matrix.setIdentityM(retMatrix, 0);
        Matrix.setIdentityM(uvTransMatrix, 0);
        Matrix.setIdentityM(uvTransMatrix, 16);
        Matrix.translateM(uvTransMatrix, 0, uvX, uvY, 0);
        Matrix.scaleM(uvTransMatrix, 16, uvWidth, uvHeight, 0);
        Matrix.multiplyMM(retMatrix, 0, uvTransMatrix, 0, uvTransMatrix, 16);
        return retMatrix;
    }


    public abstract void render(float x, float y, Texture texture, Rect texRext);

    public abstract void render(float x, float y, Texture texture, Rect texRext, float sizeX, float sizeY);
}
