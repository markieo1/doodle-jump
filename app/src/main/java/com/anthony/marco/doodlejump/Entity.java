package com.anthony.marco.doodlejump;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by anthony on 3-1-2017.
 */

public class Entity {
    private int x;
    private int y;
    private int height;
    private int width;
    private Bitmap image;

    public Entity(int x, int y, int height, int width, Bitmap image){
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
        this.image = image;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public void draw(Canvas canvas){};

    public void update(){};

    public void handleInput(){};
}
