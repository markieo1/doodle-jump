package com.anthony.marco.doodlejump;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

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

    public DoodleGame() {
        entities = new ArrayList<>();
        doodleSize = new Point(25, 25);
        isStarted = false;
    }

    public void startGame() {
        Log.i(TAG, "Game started!");
        this.generatePlatforms();
        isStarted = true;
    }

    public void stopGame() {
        Log.i(TAG, "Game stopped!");
        isStarted = false;
    }

    public void generatePlatforms() {
        //entities.clear();
        Bitmap bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.platform);
        /*
        //Vertical
        for (int j = 0; j < 10; j++) {
            //Horizontal
            for (int i = 0; i < 10; i++) {

            }
        }
        */


        if (camera.getTotalDrawnEntities() <6) {
            Random rnd = new Random();

            int x = rnd.nextInt(getScreenWidth() - 100) + 1;
            int doodleInvertedY = ((int)doodle.getY() - getScreenHeight() /2) * -1;


            int y = rnd.nextInt((doodleInvertedY)) + getScreenHeight();

            entities.add(new Entity(x, -y, 10, 100, bitmap));

        }

        camera.setEntities(entities);



        Log.i("DoodleGame", "Total entities" + entities.size());
    }

    public void update() {
        if (isStarted)
            camera.update(doodle);
        generatePlatforms();
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
        camera = new ScrollingCamera(new Rect(0, 0, screenWidth, screenHeight));

        doodle = new Doodle(getScreenWidth() / 2 - 50, -100, doodleSize.x, doodleSize.y, null);
        entities.add(doodle);
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
