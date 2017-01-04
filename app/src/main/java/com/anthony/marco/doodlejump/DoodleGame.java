package com.anthony.marco.doodlejump;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by marco on 3-1-2017.
 */

public class DoodleGame {
    private ArrayList<Entity> entities;
    private int screenWidth;
    private int screenHeight;
    private ScrollingCamera camera;
    Doodle doodle;
    private Point doodleSize;

    public DoodleGame(){
        entities = new ArrayList<>();
        doodleSize = new Point(25,25);
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

        doodle = new Doodle(getScreenWidth()/2 - 50, getScreenHeight() - (doodleSize.x + doodleSize.y), doodleSize.x, doodleSize.y, null, 10, 10);
        entities.add(doodle);
        camera.setEntities(entities);

        Log.i("DoodleGame", "Total entities" + entities.size());
    }

    public void update() {
        camera.update();
        // TODO: Update all entities here
        /*for (Entity entity : entities) {
            entity.update();
        }*/
    }

    public void handleInput() {
        // TODO: Handle input for the entities that require it
        for (Entity entity : entities) {
            entity.handleInput();
        }
    }

    public void draw(Canvas canvas) {
        camera.draw(canvas);
        // TODO: Draw the entities on the screen
        /*if(!this.doodle.checkCollision(entities)){
            for (Entity entity : entities) {
                entity.draw(canvas);
            }
        }*/
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
    }

    public void setJumpSize(int jumpSize) {
        if (doodle != null){
            this.doodle.setJumpSize(jumpSize);
        }
    }
}
