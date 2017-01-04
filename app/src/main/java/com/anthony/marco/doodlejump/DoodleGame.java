package com.anthony.marco.doodlejump;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;

/**
 * Created by marco on 3-1-2017.
 */

public class DoodleGame {
    private ArrayList<Entity> entities;
    private int screenWidth;
    private int screenHeight;

    public DoodleGame(int screenWidth,int screenHeight) {
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;

        entities = new ArrayList<>();
        generatePlatforms();
    }

    public void startGame() {

    }

    public void stopGame() {

    }

    private void generatePlatforms() {
        Bitmap bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.platform);
        for (int j = 0; j < 20; j++) {
            for (int i = 0; i < 20; i++) {
                entities.add(new Entity(200 * j, 150 * i, 10, 100, bitmap));
            }
        }

        Doodle doodle = new Doodle(500, 500, 25, 25, null, 10, 10);
        entities.add(doodle);
    }

    public void update() {
        // TODO: Update all entities here
        for (Entity entity : entities) {
            entity.update();
        }
    }

    public void handleInput() {
        // TODO: Handle input for the entities that require it
        for (Entity entity : entities) {
            entity.handleInput();
        }
    }

    public void draw(Canvas canvas) {
        // TODO: Draw the entities on the screen
        for (Entity entity : entities) {
            entity.draw(canvas);
        }
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }
}
