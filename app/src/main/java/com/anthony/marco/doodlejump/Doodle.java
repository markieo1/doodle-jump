package com.anthony.marco.doodlejump;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;

/**
 * Created by anthony on 3-1-2017.
 */

public class Doodle extends Entity {
    private int velocityX;
    private int velocityY;
    private int jumpSize;
    private boolean collisionOccured;

    public Doodle(int x, int y, int height, int width, Bitmap image, int velocityX, int velocityY) {
        super(x, y, height, width, image);
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        jumpSize = 0;
        collisionOccured =false;
    }

    public boolean checkCollision(ArrayList<Entity> entities) {
        for (Entity entity : entities){
            if (!entity.getClass().equals(Doodle.class)) {
                if (this.getX() > entity.getX() && this.getX() < (entity.getX() + entity.getWidth())) {
                    if (this.getY() + this.getHeight() == entity.getY()){
                        collisionOccured = true;
                        jumpSize = 0;
                    }
                } else {
                    collisionOccured = false;
                }
            }
        }
        return collisionOccured;
    }

    public void handleInput() {
        //TODO: handle input..
    }

    @Override
    public void update() {
        super.update();

        //int newX = this.getX() - velocityX;

        int newY = this.getY() - jumpSize;
        setY(newY);
        //setX(newX);
        // TODO: Add collision checks
    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.YELLOW);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(getX(), getY(), getWidth(), paint);
    }

    public int getJumpSize() {
        return jumpSize;
    }

    public void setJumpSize(int jumpSize) {
        this.jumpSize = jumpSize;
    }
}
