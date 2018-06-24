package jp.ac.titech.itpro.sdl.game;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

import jp.ac.titech.itpro.sdl.game.component.TouchControllerComponent;
import jp.ac.titech.itpro.sdl.game.math.Vector2;

public class MainActivity extends Activity {

    private GLSurfaceView glSurfaceView;
    private GLRenderer mRenderer;

    public static MainActivity instance;

    private List<TouchControllerComponent> tccCallbacks = new ArrayList<TouchControllerComponent>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        instance = this;
        super.onCreate(savedInstanceState);

        glSurfaceView = new GLSurfaceView(this);
        glSurfaceView.setEGLContextClientVersion(2);
        try {
            mRenderer = new GLRenderer(this);
        }catch (Exception e){
            Log.e("error!だよ！",e.toString());
            return;
        }
        glSurfaceView.setRenderer(mRenderer);
        glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        setContentView(glSurfaceView);

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

}

