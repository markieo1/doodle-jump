package com.anthony.marco.doodlelibrary.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.Log;

import com.anthony.marco.doodlelibrary.logic.ScrollingCamera;

/**
 * Created by anthony on 3-1-2017.
 */

public class Entity {
    private final String TAG = "Entity";

    /**
     * The X position
     */
    protected float x;

    /**
     * The Y position
     */
    protected float y;

    /**
     * The width
     */
    protected float width;

    /**
     * The height
     */
    protected float height;

    /**
     * The velocity on the X axis
     */
    protected float velocityX;

    /**
     * The velocity on the Y axis
     */
    protected float velocityY;

    /**
     * The Entity bitmap image
     */
    protected Bitmap image;

    public Entity(float x, float y, float width, float height, Bitmap image) {
        this(x, y, width, height, 0, 0, image);
    }

    public Entity(float x, float y, float width, float height, float velocityX, float velocityY, Bitmap image) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.image = image;

        Log.i(TAG, "New Entity created, pos = " + x + ", " + y + ", width = " + width + ", height = " + height + ", velocityX = " + velocityX + ", velocityY = " + velocityY);
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

    public float getVelocityX() {
        return velocityX;
    }

    public void setVelocityX(float velocityX) {
        this.velocityX = velocityX;
    }

    public float getVelocityY() {
        return velocityY;
    }

    public void setVelocityY(float velocityY) {
        this.velocityY = velocityY;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    /**
     * Draws this instance
     *
     * @param camera The camera to use
     * @param canvas The canvas to draw onto
     */
    public void draw(ScrollingCamera camera, Canvas canvas) {
        float relativeYPos = camera.getRelativeYPosition(getY());
        canvas.drawBitmap(getImage(), null, new RectF(getX(), relativeYPos, getX() + getWidth(), relativeYPos + getHeight()), null);
    }

    /**
     * Updates this instance by adding the velocity
     */
    public void update() {
        setX(getX() + getVelocityX());
        setY(getY() + getVelocityY());
    }

    /**
     * Checks if this Instance is in the Y camera bounds
     *
     * @param camera The camera to use for checking
     * @return True if in screen on Y axis, else false
     */
    public boolean isInScreen(ScrollingCamera camera) {
        float relativeYPosition = camera.getRelativeYPosition(getY());
        return camera.getScreenHeight() > relativeYPosition;
    }
}
