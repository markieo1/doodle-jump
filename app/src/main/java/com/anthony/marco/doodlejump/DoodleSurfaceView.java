package com.anthony.marco.doodlejump;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

/**
 * Created by marco on 3-1-2017.
 */

public class DoodleSurfaceView extends SurfaceView implements SurfaceHolder.Callback, SensorEventListener {
    private SurfaceHolder surfaceHolder;
    private GameThread gameThread;

    public DoodleSurfaceView(Context context) {
        super(context);
        initialize();
    }

    public DoodleSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public DoodleSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }


    private void initialize(){
        this.gameThread = new GameThread(this);
        this.gameThread.setRunning(true);
        this.surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Rect surfaceFrame = surfaceHolder.getSurfaceFrame();
        this.gameThread.setScreenSize(surfaceFrame.width(), surfaceFrame.height());
        gameThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) { }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        // Stop Thread
        this.gameThread.setRunning(false);
        try {
            this.gameThread.join();
        } catch (InterruptedException e) {

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.gameThread.onScreenTouched(event.getX(), event.getY());
        return super.onTouchEvent(event);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float accelartionX = sensorEvent.values[0];
        float accelartionY = sensorEvent.values[1];
        float accelartionZ = sensorEvent.values[2];

        Log.i("DoodleSurfaceView", "Sensor changed X: " +  accelartionX);
        Log.i("DoodleSurfaceView", "Sensor changed Y: " +  accelartionY);
        Log.i("DoodleSurfaceView", "Sensor changed Z: " +  accelartionZ);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
