package jp.ac.titech.itpro.sdl.game;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import jp.ac.titech.itpro.sdl.game.component.TouchControllerComponent;
import jp.ac.titech.itpro.sdl.game.math.Vector2;

public class MainActivity extends Activity implements SensorEventListener{

    // OpenGL描画関連
    public GLSurfaceView glSurfaceView;
    private GLRenderer mRenderer;

    // 光センサー関連
    private SensorManager sensorManager;
    private Sensor lightSensor;
    private float brightness = 1;

    // 戻るボタン
    private boolean backKey;

    public static MainActivity instance;

    private List<TouchControllerComponent> tccCallbacks = new ArrayList<TouchControllerComponent>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        instance = this;
        super.onCreate(savedInstanceState);

        // OpenGL初期化
        glSurfaceView = new GLSurfaceView(this);
        glSurfaceView.setEGLContextClientVersion(2);
        mRenderer = new GLRenderer(this);
        glSurfaceView.setRenderer(mRenderer);
        glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        setContentView(glSurfaceView);

        // 光センサー初期化
        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
    }

    public void setTccCallBack(TouchControllerComponent tcc){
        tccCallbacks.add(tcc);
    }

    public void removeTccCallBack(TouchControllerComponent tcc){
        tccCallbacks.remove(tcc);
    }

    private Vector2 prevPoint;
    @Override
    public boolean onTouchEvent(MotionEvent event){
        switch(event.getAction()){
            case MotionEvent.ACTION_UP:
                for(TouchControllerComponent tcc : tccCallbacks){
                    tcc.setTouchEvent(new Vector2());
                }
                return true;
            case MotionEvent.ACTION_DOWN:
                for(TouchControllerComponent tcc : tccCallbacks){
                    tcc.setTouchEvent(new Vector2());
                }
                prevPoint = new Vector2(event.getX(), event.getY());
                return true;
            case MotionEvent.ACTION_MOVE:
                for(TouchControllerComponent tcc : tccCallbacks){
                    tcc.setTouchEvent(new Vector2(event.getX() - prevPoint.x, event.getY() - prevPoint.y));
                }
                prevPoint = new Vector2(event.getX(), event.getY());
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            backKey = true;
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK){
            backKey = false;
            return false;
        }
        return super.onKeyUp(keyCode, event);
    }

    public boolean getBackKey(){
        return backKey;
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean isSensor = sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_GAME);
        if(!isSensor){
            Toast.makeText(this, "端末に光センサーが付いてないのでこのゲームは遊べません。", Toast.LENGTH_LONG);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (lightSensor != null) {
            sensorManager.unregisterListener(this);
        }
    }

    private boolean first = true;

    private float initialBrightness = 1;

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        brightness = sensorEvent.values[0];
        if(first){
            initialBrightness = brightness;
            if(initialBrightness < 0.5f)initialBrightness = 0.5f;
            first = false;
        }
    }

    public float getBrightness(){
        return brightness;
    }

    public float getInitialBrightness(){
        return initialBrightness;
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        // 何もしない
    }
}

