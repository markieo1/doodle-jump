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

    public DoodleGame() {
        entities = new ArrayList<>();
        generatePlatforms();
    }

    public void startGame() {

    }

    public void stopGame() {

    }

    private void generatePlatforms() {
        Bitmap bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.platform);
        for (int i = 0; i < 40; i++) {
            entities.add(new Entity(100, 100 * i, 10, 10, bitmap));
        }
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
}
