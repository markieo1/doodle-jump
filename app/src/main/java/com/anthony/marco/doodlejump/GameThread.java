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
    private DoodleListener doodleListener;

    private boolean isRunning;

    public GameThread(DoodleSurfaceView doodleSurfaceView) {
        this.doodleSurfaceView = doodleSurfaceView;
        this.doodleGame = new DoodleGame();
        this.doodleListener = doodleGame;
    }

    @Override
    public void run() {
        // should be in doodlegame.start?
        this.doodleGame.generatePlatforms();

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

    /**
     * The screen touched callback
     * @param xPosition The x position of the touch
     * @param yPosition The y position of the touch
     */
    public void onScreenTouched(float xPosition, float yPosition){
        this.doodleListener.screenTouched(xPosition, yPosition);
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public void setScreenSize(int screenWidth, int screenHeight){
        this.doodleListener.screenSizeChanged(screenWidth, screenHeight);
    }
}
