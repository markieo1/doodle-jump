package com.anthony.marco.doodlejump.logic;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
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

    /**
     * The start y position for the doodle
     */
    private static final float DOODLE_START_Y = -100;

    /**
     * The doodle width
     */
    private static final float DOODLE_WIDTH = 50;

    /**
     * The doodle height
     */
    private static final float DOODLE_HEIGHT = 50;

    /**
     * The jump size for the doodle
     */
    private static final float DOODLE_JUMPSIZE = 40;

    /**
     * The platform width
     */
    private static final float PLATFORM_WIDTH = 100;

    /**
     * The platform height
     */
    private static final float PLATFORM_HEIGHT = 10;

    /**
     * The min difference from the previous Y generated
     */
    private static final float MIN_DIFFERENCE = 100;

    /**
     * The maximum (exclusive) difference from the previous Y generated
     */
    private static final float MAX_DIFFERENCE = 500;

    /**
     * The amount of entities to generate
     */
    private static final float GENERATION_COUNT = 100;

    /**
     * The threshold when the generation should commence again.
     * This is the lastYGenerated + the threshold, since we are negative on the Y Axis.
     */
    private static final float GENERATION_START_THRESHOLD = 3000;

    /**
     * The interval (MILLISECONDS) in which the score label should be updated
     */
    private static final int SCORE_UPDATE_INTERVAL = 100;

    /**
     * Determines if the Game is started
     */
    private boolean isStarted;

    /**
     * The screen width used for the game
     */
    private int screenWidth;

    /**
     * The screen height used for the game
     */
    private int screenHeight;

    /**
     * The camera used for drawing purposes
     */
    private ScrollingCamera camera;

    /**
     * The Doodle eq. Player
     */
    private Doodle doodle;

    /**
     * All the entities currently in the game
     */
    private ArrayList<Entity> entities;

    /**
     * The y position of the last generated platform
     */
    private float lastYGenerated;

    /**
     * The listener that handles game changes
     */
    private DoodleListener doodleListener;

    /**
     * Bitmap used for the platform
     */
    private Bitmap platformBitmap;

    /**
     * Bitmap used for the doodle
     */
    private Bitmap doodleBitmap;

    private ScheduledExecutorService ses;

    public DoodleGame() {
        entities = new ArrayList<>();
        isStarted = false;
    }

    /**
     * Starts the game
     *
     * @param doodleListener The listener to make callbacks to
     */
    public void startGame(final DoodleListener doodleListener) {
        this.doodleListener = doodleListener;

        ses = Executors.newSingleThreadScheduledExecutor();

        loadResources();

        entities.clear();

        // Setup the camera and entities
        camera = new ScrollingCamera(new Rect(0, 0, getScreenWidth(), getScreenHeight()));
        doodle = new Doodle(getScreenWidth() / 2 - DOODLE_WIDTH, DOODLE_START_Y, DOODLE_WIDTH, DOODLE_HEIGHT, DOODLE_JUMPSIZE, doodleBitmap);
        entities.add(doodle);

        // Reset the last y generated
        lastYGenerated = 0;

        // Add a platform right below the Doodle to stop it from failing the game when started
        Entity platform = new Entity(doodle.getX() - (doodle.getWidth() / 2), doodle.getY() + doodle.getHeight(), PLATFORM_WIDTH, PLATFORM_HEIGHT, platformBitmap);
        entities.add(platform);

        ses.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                // If not running no need to update the score
                if(!isStarted)
                    return;

                // Update the score label
                if (doodleListener != null && doodle != null) {
                    float resultScore = doodle.getHighestY() * -1;
                    doodleListener.scoreChanged(Math.round(resultScore));
                }
            }
        }, 0, SCORE_UPDATE_INTERVAL, TimeUnit.MILLISECONDS);

        isStarted = true;
        Log.i(TAG, "Game started!");
    }

    /**
     * Stops the game
     */
    public void stopGame() {
        Log.i(TAG, "Game stopped!");
        isStarted = false;

        if (ses != null)
            ses.shutdownNow();

        if (doodleListener != null) {
            // Make callback saying the game is over
            float resultScore = doodle.getHighestY() * -1;
            doodleListener.gameOver(Math.round(resultScore));
        }
    }

    /**
     * Updates all entities
     */
    public void update() {
        if (isStarted) {
            doodle.checkCollision(entities);
            camera.update(doodle);

            generatePlatforms();
            cleanupOldPlatforms();

            if (!doodle.isInScreen(camera)) {
                Log.i(TAG, "Doodle left screen");
                stopGame();
            }
        }
    }

    /**
     * Draws the camera using the specified canvas
     *
     * @param canvas The canvas to draw onto
     */
    public void draw(Canvas canvas) {
        if (isStarted)
            camera.draw(canvas);
    }

    /**
     * Generates platforms for the player to jump onto.
     */
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

    /**
     * Cleans up the platforms that are not in screen anymore
     */
    private void cleanupOldPlatforms() {
        float screenBorder = (doodle.getY() + getScreenHeight() / 2);

        ArrayList<Integer> entityIndicesToRemove = new ArrayList<>();
        for (Entity entity : entities) {
            if (entity instanceof Doodle)
                continue;

            // Remove all entities under the screen border
            if (entity.getY() >= screenBorder) {
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

    /**
     * Loads all the resources that are needed
     */
    private void loadResources() {
        Log.i(TAG, "Starting load resources.");
        if (platformBitmap == null)
            platformBitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.platform);

        if (doodleBitmap == null)
            doodleBitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.circle);

        Log.i(TAG, "Done loading resources.");
    }

    @Override
    public void screenTouched(float xPosition, float yPosition) {
        // Check if the game has already started
        if (!isStarted)
            return;

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

    /**
     * Gets the current screenWidth
     *
     * @return The current screenWidth
     */
    public int getScreenWidth() {
        return screenWidth;
    }

    /**
     * Gets the current screenHeight
     *
     * @return The current screenHeight
     */
    public int getScreenHeight() {
        return screenHeight;
    }
}
