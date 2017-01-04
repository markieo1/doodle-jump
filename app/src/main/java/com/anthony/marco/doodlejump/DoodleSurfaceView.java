package com.anthony.marco.doodlejump;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

/**
 * Created by marco on 3-1-2017.
 */

public class DoodleSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
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
        this.gameThread.setScreenWidth(surfaceHolder.getSurfaceFrame().width());
        this.gameThread.setScreenHeight(surfaceHolder.getSurfaceFrame().height());
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
        int i =0;
        this.gameThread.onScreenTouched();
        return super.onTouchEvent(event);
    }
}
