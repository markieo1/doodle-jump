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

    public Doodle(){
        super();
        this.velocityX = 0;
        this.velocityY = 0;
    }

    public Doodle(int velocityX, int velocityY) {
        super();
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }

    public Doodle(int x, int y, int heigth, int width, Bitmap image, int velocityX, int velocityY) {
        super(x, y, heigth, width, image);
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }

    public Boolean checkCollision(ArrayList<Entity> Platforms){

        return null;
    }

    @Override
    public void draw(Canvas canvas) {

    }


    @Override
    public void handleInput(){
        //TODO: handle input..
    }

    @Override
    public void update() {

    }
}
