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
    private ArrayList<Entity> entities;
    private int screenWidth;
    private int screenHeight;
    private ScrollingCamera camera;
    private Doodle doodle;
    private Point doodleSize;

    public DoodleGame() {
        entities = new ArrayList<>();
        doodleSize = new Point(25, 25);

    }

    public void startGame() {

    }

    public void stopGame() {

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
        if (camera.getTotalDrawnEntities() <10) {
            Random rnd = new Random();

            int x = rnd.nextInt(getScreenWidth() - 100) + 1;


            int maxY = ((int) doodle.getY() - getScreenHeight() / 2);

            if (maxY < 0) {
                maxY *= -1;
            }

            int y = rnd.nextInt(maxY) + getScreenHeight() / 2;

            if (y + getScreenHeight() > doodle.getY()){
                entities.add(new Entity(x, -y, 10, 100, bitmap));
            }

            camera.setEntities(entities);
        }

        Log.i("DoodleGame", "Total drawn entities " +camera.getTotalDrawnEntities());
        Log.i("DoodleGame", "Total entities" + entities.size());
    }

    public void update() {
        camera.update(doodle);
        generatePlatforms();
    }

    public void handleInput() {
        for (Entity entity : entities) {
            entity.handleInput();
        }
    }

    public void draw(Canvas canvas) {
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
        setJumpSize(40);
    }

    @Override
    public void screenSizeChanged(int width, int height) {
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
