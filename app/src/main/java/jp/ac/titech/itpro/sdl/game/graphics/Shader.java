package jp.ac.titech.itpro.sdl.game.graphics;

import android.opengl.GLES20;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.microedition.khronos.opengles.GL;

import jp.ac.titech.itpro.sdl.game.MainActivity;

public class Shader {
    private int shaderProgram;
    private static int prevProgram = 0;

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
        if(shaderProgram != prevProgram){
            prevProgram = shaderProgram;
            GLES20.glUseProgram(shaderProgram);
        }
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
        final int[] comled = new int[1];
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, comled, 0);
        String s = GLES20.glGetShaderInfoLog(shader);
        Log.e("shader error", s);
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
            br.close();
            return source;
        }catch (IOException e){
            Toast.makeText(MainActivity.instance, filename + "が見つかりません", Toast.LENGTH_LONG);
            return null;
        }
    }
}
