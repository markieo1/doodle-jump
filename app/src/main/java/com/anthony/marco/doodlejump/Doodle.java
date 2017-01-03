package com.anthony.marco.doodlejump;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.ArrayList;

/**
 * Created by anthony on 3-1-2017.
 */

public class Doodle extends Entity {
    private int velocityX;
    private int velocityY;

    public Doodle(int x, int y, int height, int width, Bitmap image, int velocityX, int velocityY) {
        super(x, y, height, width, image);
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }

    public Boolean checkCollision(ArrayList<Entity> Platforms){

        return null;
    }

    @Override
    public void draw(Canvas canvas) {

    }

    public void handleInput(){
        //TODO: handle input..
    }
}
