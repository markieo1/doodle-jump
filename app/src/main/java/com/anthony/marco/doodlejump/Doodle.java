package com.anthony.marco.doodlejump;

import android.graphics.Bitmap;

/**
 * Created by antho on 3-1-2017.
 */

public class Doodle extends Entity {
    private int velocityX;
    private int velocityY;

    

    public Doodle(int velocityX, int velocityY) {
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }

    public Doodle(int x, int y, int heigth, int width, Bitmap image, int velocityX, int velocityY) {
        super(x, y, heigth, width, image);
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }
}
