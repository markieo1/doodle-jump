package com.anthony.marco.doodlejump;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

/**
 * Created by marco on 3-1-2017.
 */

public class GameThread extends Thread {
    private final String TAG = "GameThread";
    private DoodleSurfaceView doodleSurfaceView;

    private boolean isRunning;

    public GameThread(DoodleSurfaceView doodleSurfaceView) {
        this.doodleSurfaceView = doodleSurfaceView;
    }

    @Override
    public void run() {
        while (isRunning) {
            // 1. Handle Input
            // 2. Update
            // 3. Draw
            
            Canvas canvas = doodleSurfaceView.getHolder().lockCanvas();

            if (canvas != null) {
                synchronized (doodleSurfaceView.getHolder()) {
                    // Draw here using the local canvas variable
                    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                    paint.setColor(Color.YELLOW);
                    paint.setStyle(Paint.Style.FILL);
                    canvas.drawCircle(50, 50, 20, paint);
                }

                doodleSurfaceView.getHolder().unlockCanvasAndPost(canvas);
            }

            // Sleep
            try {
                sleep(1000 / 60);
            } catch (InterruptedException ex) {
                Log.e(TAG, ex.getLocalizedMessage());
            }
        }
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }
}
