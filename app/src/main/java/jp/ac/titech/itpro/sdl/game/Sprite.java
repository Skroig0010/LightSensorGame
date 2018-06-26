package jp.ac.titech.itpro.sdl.game;

import android.opengl.GLES20;
import android.opengl.Matrix;

import java.nio.FloatBuffer;

public class Sprite {
    // TODO:ファイルから読み込む
    public final String vertexShaderCode =
            "attribute  vec4 vPosition;" +
                    "attribute  vec2 vUv;" +
                    "uniform mat4 viewportMatrix;" +
                    "uniform mat4 transMatrix;" +
                    "uniform mat4 uvTransMatrix;" +
                    "varying vec2 uv;" +
                    "void main() {" +
                    "  uv = (uvTransMatrix * vec4(vUv, 0, 1)).xy;" +
                    "  gl_Position = viewportMatrix * transMatrix * vPosition;" +
                    "}";
    //シンプル色は自分で指定(R,G,B ALPHA)指定
    public final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform sampler2D texture;" +
                    "varying vec2 uv;" +
                    "void main() {" +
                    "  gl_FragColor = texture2D(texture, uv);" +
                    "}";

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

    private int loadShader(int type, String shaderCode) {
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }

    private Shader shaderProgram;

    private int positionAttribute;
    private int uvAttribute;
    private int transMatrixLocation;
    private int viewportMatrixLocation;
    private int textureLocation;
    private int uvTransMatrixLocation;

    private int width = 160;
    private int height = 240;

    private float screenMatrix[] = {
            2.0f / width, 0, 0, 0,
            0, -2.0f / height, 0, 0,
            0, 0, 1, 0,
            -1, 1, 0, 1
    };

    public Sprite() {
        // シェーダーの読み込み
        shaderProgram = new Shader("main");

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

        // Uniform変数
        viewportMatrixLocation = shaderProgram.getUniformLocation("viewportMatrix");
        transMatrixLocation = shaderProgram.getUniformLocation("transMatrix");
        textureLocation = shaderProgram.getUniformLocation("texture");
        uvTransMatrixLocation = shaderProgram.getUniformLocation("uvTransMatrix");
    }

    public void draw(float x, float y, Texture texture, Rect texRext, float mag) {
        shaderProgram.useProgram();

        // サイズをテクスチャサイズに合わせて移動
        float[] transMatrix = new float[48];
        Matrix.setIdentityM(transMatrix, 0);
        Matrix.setIdentityM(transMatrix, 16);
        Matrix.setIdentityM(transMatrix, 32);
        Matrix.scaleM(transMatrix, 16, texRext.width * mag, texRext.height * mag, 0);
        Matrix.translateM(transMatrix, 32, x, y, 0);
        Matrix.multiplyMM(transMatrix, 0, transMatrix, 32, transMatrix, 16);

        // UVを正しい位置に移動させる行列
        float[] uvTransMatrix = new float[48];
        float uvWidth = (float)texRext.width / texture.width;
        float uvHeight = (float)texRext.height / texture.height;
        float uvX = (float)texRext.x / texture.width;
        float uvY = (float)texRext.y / texture.height;
        Matrix.setIdentityM(uvTransMatrix, 0);
        Matrix.setIdentityM(uvTransMatrix, 16);
        Matrix.setIdentityM(uvTransMatrix, 32);
        Matrix.translateM(uvTransMatrix, 16, uvX, uvY, 0);
        Matrix.scaleM(uvTransMatrix, 32, uvWidth, uvHeight, 0);
        Matrix.multiplyMM(uvTransMatrix, 0, uvTransMatrix, 16, uvTransMatrix, 32);

        // Uniform変数
        texture.useTexture();
        GLES20.glUniform1i(textureLocation, 0);
        GLES20.glUniformMatrix4fv(viewportMatrixLocation, 1, false, screenMatrix, 0);
        GLES20.glUniformMatrix4fv(transMatrixLocation, 1, false, transMatrix, 0);
        GLES20.glUniformMatrix4fv(uvTransMatrixLocation, 1, false, uvTransMatrix, 0);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, vertices.length/3);
    }
}
