package com.anthony.marco.doodlejump;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by anthony on 3-1-2017.
 */

public class Doodle extends Entity {
    private float velocityX;
    private float velocityY;
    private int jumpSize;
    private boolean collisionOccured;
    private int numberToIncreaseVelocity;
    private boolean distancePassed;
    private int maxJumpSize;

    public Doodle(float x, float y, float height, float width, Bitmap image, float velocityX, float velocityY) {
        super(x, y, height, width, image);
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        jumpSize = 0;
        collisionOccured =false;
        distancePassed = false;
        maxJumpSize = 40;
    }

    public boolean checkCollision(ArrayList<Entity> entities) {
        for (Entity entity : entities){
            if (!entity.getClass().equals(Doodle.class)) {
                if (this.getX() > entity.getX() && this.getX() < (entity.getX() + entity.getWidth())) {
                    float currentYlocationPlatform = entity.getY();
                    float ballLocation = this.getY() + this.getHeight();
                    if (this.getY() + this.getHeight() < entity.getY() && this.getY() + this.getHeight() > entity.getY() - maxJumpSize) {
                        if (distancePassed) {
                            this.setY(entity.getY() - this.getHeight());
                            collisionOccured = true;
                        }
                    }
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
        if (numberToIncreaseVelocity > 0) {
            distancePassed = false;
            numberToIncreaseVelocity -= jumpSize;

            float newY = this.getY() - jumpSize;
            setY(newY);
            if (numberToIncreaseVelocity % 2 == 0 || numberToIncreaseVelocity % 3 == 0) {
                jumpSize -= 1;
            }
            if (numberToIncreaseVelocity < 0) {
                numberToIncreaseVelocity = 0;
            }
            if (jumpSize < 0) {
                numberToIncreaseVelocity = -1;
            }
        } else if (numberToIncreaseVelocity < 0) {
            if (collisionOccured) {
                jumpSize = 0;
                numberToIncreaseVelocity = 0;
                collisionOccured=false;
            }
            else{
                distancePassed = true;
                if (jumpSize > -maxJumpSize){
                    jumpSize -= 1;
                }

                float newY = this.getY() - jumpSize;
                setY(newY);
            }
        }

        Log.i("distanceToJump", Integer.toString(numberToIncreaseVelocity));
        Log.i("jumpSize", Integer.toString(jumpSize));
    }

    @Override
    public void draw(ScrollingCamera camera, Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.YELLOW);
        paint.setStyle(Paint.Style.FILL);

        canvas.drawCircle(getX(), camera.getRelativeYPosition(getY()), getWidth(), paint);
    }

    public int getJumpSize() {
        return jumpSize;
    }

    public void setJumpSize(int jumpSize) {
        this.jumpSize = jumpSize;
    }

    public int getnumberToIncreaseVelocity() {
        return numberToIncreaseVelocity;
    }

    public void setnumberToIncreaseVelocity(int distanceToJump) {
        this.numberToIncreaseVelocity = distanceToJump;
    }
}
