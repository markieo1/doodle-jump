package com.anthony.marco.doodlejump.logic;

import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;

import com.anthony.marco.doodlejump.listener.DoodleListener;
import com.anthony.marco.doodlejump.listener.ScreenListener;
import com.anthony.marco.doodlejump.view.DoodleSurfaceView;

/**
 * Created by marco on 3-1-2017.
 */

public class GameThread extends Thread {
    private final String TAG = "GameThread";
    private DoodleSurfaceView doodleSurfaceView;
    private DoodleGame doodleGame;
    private ScreenListener screenListener;

    private boolean isRunning;

    public GameThread(DoodleSurfaceView doodleSurfaceView) {
        this.doodleSurfaceView = doodleSurfaceView;
        this.doodleGame = new DoodleGame();
        this.screenListener = doodleGame;
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

    public void startGame(DoodleListener doodleListener) {
        this.doodleGame.startGame(doodleListener);
    }

    public void stopGame() {
        this.doodleGame.stopGame();
    }

    /**
     * The screen touched callback
     *
     * @param xPosition The x position of the touch
     * @param yPosition The y position of the touch
     */
    public void screenTouched(float xPosition, float yPosition) {
        this.screenListener.screenTouched(xPosition, yPosition);
    }

    public void screenRotated(float newRotation) {
        this.screenListener.rotationChanged(newRotation);
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public void setScreenSize(int screenWidth, int screenHeight) {
        this.screenListener.screenSizeChanged(screenWidth, screenHeight);
    }
}
