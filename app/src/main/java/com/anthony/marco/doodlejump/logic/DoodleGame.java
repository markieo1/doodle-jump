package com.anthony.marco.doodlejump.logic;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

import com.anthony.marco.doodlejump.App;
import com.anthony.marco.doodlejump.model.Doodle;
import com.anthony.marco.doodlejump.listener.DoodleListener;
import com.anthony.marco.doodlejump.model.Entity;
import com.anthony.marco.doodlejump.R;
import com.anthony.marco.doodlejump.listener.ScreenListener;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by marco on 3-1-2017.
 */

public class DoodleGame implements ScreenListener {
    private final String TAG = "DoodleGame";

    private ArrayList<Entity> entities;
    private int screenWidth;
    private int screenHeight;
    private ScrollingCamera camera;
    private Doodle doodle;
    private Point doodleSize;
    private boolean isStarted;
    private Bitmap bitmap;

    private DoodleListener doodleListener;

    private ScheduledExecutorService ses;

    public DoodleGame() {
        entities = new ArrayList<>();
        doodleSize = new Point(25, 25);
        isStarted = false;
        bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.platform);
    }

    public void startGame(final DoodleListener doodleListener) {
        this.doodleListener = doodleListener;
        Log.i(TAG, "Game started!");
        camera = new ScrollingCamera(new Rect(0, 0, screenWidth, screenHeight));

        entities.clear();
        doodle = new Doodle(getScreenWidth() / 2 - 50, -100, doodleSize.x, doodleSize.y, null);
        doodle.setVelocityX(0);
        entities.add(doodle);

        //this.generatePlatforms();

        ses = Executors.newSingleThreadScheduledExecutor();

        ses.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                // code to run
                if (doodleListener != null && doodle != null) {
                    float resultScore = doodle.getHighestY() * -1;
                    doodleListener.scoreChanged(Math.round(resultScore));
                }
            }
        }, 0, 100, TimeUnit.MILLISECONDS);

        isStarted = true;
    }

    public void stopGame() {
        Log.i(TAG, "Game stopped!");

        ses.shutdown();

        isStarted = false;

        if (doodleListener != null) {
            float resultScore = doodle.getHighestY() * -1;
            doodleListener.gameOver(Math.round(resultScore));
        }
    }

    public void generatePlatforms() {

        Random rnd = new Random();

        int x = rnd.nextInt(getScreenWidth() - 100) + 1;

        ArrayList<Integer> entityIndexToRemove = new ArrayList<>();

        for (Entity entity : entities) {
            // Remove all entities under the screen border
            if (entity.getY() > (doodle.getY() + getScreenWidth() / 2)) {
                entityIndexToRemove.add(entities.indexOf(entity));
            }
        }

        for (int i = 0; i < entityIndexToRemove.size(); i++) {
            entities.remove(entityIndexToRemove.get(i));
        }

        if (camera.getTotalDrawnEntities() < 10) {
            int maxY = ((int) doodle.getY() - getScreenHeight() / 2);

            if (maxY < 0) {
                maxY *= -1;
            }

            int y = rnd.nextInt(maxY) + getScreenHeight() / 2;

            if (y + getScreenHeight() > doodle.getY()) {
                entities.add(new Entity(x, -y, 10, 100, bitmap));
            }

            camera.setEntities(entities);
        }

        Log.i(TAG, "Total drawn entities " + camera.getTotalDrawnEntities());
        Log.i(TAG, "Total entities" + entities.size());
    }

    public void update() {
        if (isStarted) {
            camera.update(doodle);
            generatePlatforms();

            if (!doodle.isInScreen(camera)) {
                if (doodleListener != null) {
                    float resultScore = doodle.getHighestY() * -1;
                    doodleListener.gameOver(Math.round(resultScore));
                }
                Log.i(TAG, "Doodle left screen");
                stopGame();
            }
        }
    }

    public void handleInput() {
        if (isStarted) {
            for (Entity entity : entities) {
                entity.handleInput();
            }
        }
    }

    public void draw(Canvas canvas) {
        if (isStarted)
            camera.draw(canvas);
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public void setJumpSize(int jumpSize) {
        if (doodle != null) {
            this.doodle.setJumpSize(jumpSize);
        }
    }

    @Override
    public void screenTouched(float xPosition, float yPosition) {
        Log.i(TAG, "Screen touched, xPosition = " + xPosition + ", yPosition = " + yPosition);
        setJumpSize(40);
    }

    @Override
    public void screenSizeChanged(int width, int height) {
        Log.i(TAG, "Screen size changed, width = " + width + ", height = " + height);
        this.screenWidth = width;
        this.screenHeight = height;
    }

    @Override
    public void rotationChanged(float newRotation) {
        // Roll -90 is device rotated completely to the eg. landscape mode
        // Roll 90 is device rotated to the left eg. landscape mode
        // So we need to flip the roll
        float velocityX = newRotation * -1;

        if (doodle != null)
            doodle.setVelocityX(velocityX);
    }
}
