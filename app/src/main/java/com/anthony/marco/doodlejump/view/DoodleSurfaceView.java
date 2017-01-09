package com.anthony.marco.doodlejump.view;

import android.content.Context;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.anthony.marco.doodlejump.listener.DoodleListener;
import com.anthony.marco.doodlejump.logic.GameThread;

/**
 * Created by marco on 3-1-2017.
 */

public class DoodleSurfaceView extends SurfaceView implements SurfaceHolder.Callback, SensorEventListener {
    private static final String TAG = "DoodleSurfaceView";
    private SurfaceHolder surfaceHolder;
    private GameThread gameThread;
    private static final int FROM_RADS_TO_DEGS = -57;

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


    private void initialize() {
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
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        // Stop Thread
        this.gameThread.setRunning(false);
        try {
            this.gameThread.join();
        } catch (InterruptedException e) {

        }
    }

    public void startGame(DoodleListener doodleListener) {
        this.gameThread.startGame(doodleListener);
    }

    public void stopGame() {
        this.gameThread.stopGame();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.gameThread.screenTouched(event.getX(), event.getY());
        return super.onTouchEvent(event);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float[] rotationMatrix = new float[9];
        SensorManager.getRotationMatrixFromVector(rotationMatrix, sensorEvent.values);
        int worldAxisX = SensorManager.AXIS_X;
        int worldAxisZ = SensorManager.AXIS_Z;
        float[] adjustedRotationMatrix = new float[9];
        SensorManager.remapCoordinateSystem(rotationMatrix, worldAxisX, worldAxisZ, adjustedRotationMatrix);
        float[] orientation = new float[3];
        SensorManager.getOrientation(adjustedRotationMatrix, orientation);

        float roll = orientation[2] * FROM_RADS_TO_DEGS;

        if (roll >= -90 && roll <= 90) {
            Log.i("DoodleSurfaceView", "Rotation = " + roll);
            this.gameThread.screenRotated(roll);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
