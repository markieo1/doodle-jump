package com.anthony.marco.doodlejump;

import android.graphics.Bitmap;

/**
 * Created by anthony on 3-1-2017.
 */

public class Entity {
    private int x;
    private int y;
    private int height;
    private int width;
    private Bitmap image;

    public Entity(int x, int y, int heigth, int width, Bitmap image){
        this.x = x;
        this.y = y;
        this.height = heigth;
        this.width = width;
        this.image = image;
    }

    public Entity(){
        x = 0;
        y = 0;
        height = 0;
        width = 0;
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

    public int getHeigth() {
        return height;
    }

    public void setHeigth(int heigth) {
        this.height = heigth;
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

    public void draw(){
        //TODO: implement
    }
}
