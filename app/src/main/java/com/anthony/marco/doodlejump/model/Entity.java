package com.anthony.marco.doodlejump.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;

import com.anthony.marco.doodlejump.logic.ScrollingCamera;

/**
 * Created by anthony on 3-1-2017.
 */

public class Entity {
    private float x;
    private float y;
    private float height;
    private float width;
    private Bitmap image;

    public Entity(float x, float y, float height, float width, Bitmap image) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
        this.image = image;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public void draw(ScrollingCamera camera, Canvas canvas) {
        float relativeYPos = camera.getRelativeYPosition(getY());
        canvas.drawBitmap(getImage(), null, new RectF(getX(), relativeYPos, getX() + getWidth(), relativeYPos + getHeight()), null);
    }

    public void update() {
    }

    public void handleInput() {
    }
}
