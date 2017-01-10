package com.anthony.marco.doodlejump.logic;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

import com.anthony.marco.doodlejump.App;
import com.anthony.marco.doodlejump.R;
import com.anthony.marco.doodlejump.listener.DoodleListener;
import com.anthony.marco.doodlejump.listener.ScreenListener;
import com.anthony.marco.doodlejump.model.Doodle;
import com.anthony.marco.doodlejump.model.Entity;

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
    private Bitmap platformBitmap;
    private Bitmap doodleBitmap;

    private DoodleListener doodleListener;

    private ScheduledExecutorService ses;

    private float lastYGenerated;

    /**
     * The min difference from the previous Y generated
     */
    private final float MIN_DIFFERENCE = 100;

    /**
     * The maximum (exclusive) difference from the previous Y generated
     */
    private final float MAX_DIFFERENCE = 500;

    /**
     * The amount of entities to generate
     */
    private final float GENERATION_COUNT = 100;

    /**
     * The threshold when the generation should commence again.
     * This is the lastYGenerated + the threshold, since we are negative on the Y Axis.
     */
    private final float GENERATION_START_THRESHOLD = 3000;


    public DoodleGame() {
        entities = new ArrayList<>();
        doodleSize = new Point(50, 50);
        isStarted = false;
        platformBitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.platform);
        doodleBitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.circle);
    }

    public void startGame(final DoodleListener doodleListener) {
        this.doodleListener = doodleListener;
        Log.i(TAG, "Game started!");
        camera = new ScrollingCamera(new Rect(0, 0, screenWidth, screenHeight));

        entities.clear();
        doodle = new Doodle(getScreenWidth() / 2 - 50, -100, 40, doodleSize.x, doodleSize.y, doodleBitmap);
        doodle.setVelocityX(0);
        entities.add(doodle);
        lastYGenerated = 0;

        // Add a platform right below the Doodle to stop it from failing the game when started
        Entity platform = new Entity(doodle.getX() - (doodle.getWidth() / 2), doodle.getY() + doodle.getHeight(), 10, 100, platformBitmap);
        entities.add(platform);

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

    public void update() {
        if (isStarted) {
            if( doodle.checkCollision(entities)){
                //TODO: update timer..
            }
            camera.update(doodle);

            generatePlatforms();
            cleanupOldPlatforms();

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

    public void draw(Canvas canvas) {
        if (isStarted)
            camera.draw(canvas);
    }

    private void generatePlatforms() {
        if (lastYGenerated < 0 && doodle.getHighestY() > lastYGenerated + GENERATION_START_THRESHOLD)
            return;

        Log.i(TAG, "Generating new platforms");

        Random rnd = new Random();

        for (int i = 0; i < GENERATION_COUNT; i++) {
            int x = rnd.nextInt(getScreenWidth() - 100) + 1;

            float randomY = rnd.nextFloat() * (MAX_DIFFERENCE - MIN_DIFFERENCE) + MIN_DIFFERENCE;
            if (randomY > 0) {
                // Make it negative since we are going up
                randomY *= -1;
            }

            float platformY = lastYGenerated + randomY;

            lastYGenerated = platformY;

            Entity entity = new Entity(x, platformY, 10, 100, platformBitmap);
            entities.add(entity);
        }

        camera.setEntities(entities);

        Log.i(TAG, "New platforms generated!");
    }

    private void cleanupOldPlatforms() {
        float screenBorder = (doodle.getY() + getScreenHeight() / 2);

        ArrayList<Integer> entityIndicesToRemove = new ArrayList<>();
        for (Entity entity : entities) {
            if (entity instanceof Doodle)
                continue;

            // Remove all entities under the screen border
            if (entity.getY() > screenBorder) {
                entityIndicesToRemove.add(entities.indexOf(entity));
            }
        }

        for (int index : entityIndicesToRemove) {
            entities.remove(index);
        }

        if (entityIndicesToRemove.size() > 0) {
            camera.setEntities(entities);
            Log.i(TAG, "Cleaning up, total cleaned up = " + entityIndicesToRemove.size());
        }
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    @Override
    public void screenTouched(float xPosition, float yPosition) {
        Log.i(TAG, "Screen touched, xPosition = " + xPosition + ", yPosition = " + yPosition);

        if (doodle != null)
            doodle.jump();
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
