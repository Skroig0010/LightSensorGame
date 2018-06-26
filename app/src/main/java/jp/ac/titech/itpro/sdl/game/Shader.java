package jp.ac.titech.itpro.sdl.game;

import android.opengl.GLES20;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.microedition.khronos.opengles.GL;

public class Shader {
    private int shaderProgram;

    public Shader(String filename){
        // ファイル読み込み
        String vertexShaderSource = readSource(filename + ".vert");
        String fragmentShaderSource = readSource(filename + ".frag");

        // シェーダーの読み込み
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderSource);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderSource);
        shaderProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(shaderProgram, vertexShader);
        GLES20.glAttachShader(shaderProgram, fragmentShader);
        GLES20.glLinkProgram(shaderProgram);
    }

    public void useProgram(){
        GLES20.glUseProgram(shaderProgram);
    }

    public int getAttributeLocation(String attrName){
        return GLES20.glGetAttribLocation(shaderProgram, attrName);
    }

    public int getUniformLocation(String uniformName){
        return GLES20.glGetUniformLocation(shaderProgram, uniformName);
    }

    private int loadShader(int type, String shaderCode) {
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }

    private String readSource(String filename){
        try {
            InputStream is = MainActivity.instance.getAssets().open(filename);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String source = "";
            String str = br.readLine();
            while(str != null) {
                source += str;
                str = br.readLine();
            }
            return source;
        }catch (IOException e){
            Toast.makeText(MainActivity.instance, filename + "が見つかりません", Toast.LENGTH_LONG);
            return null;
        }
    }
}
