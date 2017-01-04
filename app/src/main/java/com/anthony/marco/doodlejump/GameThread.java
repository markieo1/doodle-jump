package com.anthony.marco.doodlejump;

import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;

/**
 * Created by marco on 3-1-2017.
 */

public class GameThread extends Thread {
    private final String TAG = "GameThread";
    private DoodleSurfaceView doodleSurfaceView;
    private DoodleGame doodleGame;

    private boolean isRunning;

    public GameThread(DoodleSurfaceView doodleSurfaceView, int screenWidth, int screenHeight) {
        this.doodleSurfaceView = doodleSurfaceView;
        this.doodleGame = new DoodleGame(screenWidth,screenHeight);
    }

    @Override
    public void run() {
        while (isRunning) {
            // 1. Handle Input
            // 2. Update
            // 3. Draw
            doodleGame.handleInput();
            doodleGame.update();

            Canvas canvas = doodleSurfaceView.getHolder().lockCanvas();

            if (canvas != null) {
                synchronized (doodleSurfaceView.getHolder()) {
                    // Draw here using the local canvas variable
                    canvas.drawColor(Color.BLACK);
                    doodleGame.draw(canvas);
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
