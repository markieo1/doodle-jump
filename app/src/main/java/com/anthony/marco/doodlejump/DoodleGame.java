package com.anthony.marco.doodlejump;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by marco on 3-1-2017.
 */

public class DoodleGame implements DoodleListener {
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
        camera = new ScrollingCamera(new Rect(0, 0, screenWidth, screenHeight));

        Bitmap bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.platform);
        for (int j = 0; j < 20; j++) {
            for (int i = 0; i < 20; i++) {
                entities.add(new Entity(200 * j, 150 * i, 10, 100, bitmap));
            }
        }

        doodle = new Doodle(getScreenWidth() / 2 - 50, getScreenHeight() - (doodleSize.x + doodleSize.y), doodleSize.x, doodleSize.y, null, 10, 10);
        entities.add(doodle);
        camera.setEntities(entities);

        Log.i("DoodleGame", "Total entities" + entities.size());
    }

    public void update() {
        camera.update(doodle);
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
        setJumpSize(10);
    }

    @Override
    public void screenSizeChanged(int width, int height) {
        this.screenWidth = width;
        this.screenHeight = height;
    }

    @Override
    public void rotationChanged(float newRotation) {

    }
}
