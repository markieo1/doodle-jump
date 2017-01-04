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
    private int distanceToJump;
    private boolean distancePassed;

    public Doodle(float x, float y, float height, float width, Bitmap image, float velocityX, float velocityY) {
        super(x, y, height, width, image);
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        jumpSize = 0;
        collisionOccured =false;
        distancePassed = false;
    }

    public boolean checkCollision(ArrayList<Entity> entities) {
        for (Entity entity : entities){
            if (!entity.getClass().equals(Doodle.class)) {
                if (this.getX() > entity.getX() && this.getX() < (entity.getX() + entity.getWidth())) {
                    float currentYlocationPlatform = entity.getY();
                    float ballLocation = this.getY() + this.getHeight();
                    if (this.getY() + this.getHeight() < entity.getY() && this.getY() + this.getHeight() >
                            entity.getY() -10 ){
                        if(distancePassed) {
                            collisionOccured = true;
                        }
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
        if (distanceToJump > 0){
            distancePassed = false;
            distanceToJump -= jumpSize;

            float newY = this.getY() - jumpSize;
            setY(newY);
                if (distanceToJump % 2 == 0) {
                    jumpSize -= 1;
                }
                if (distanceToJump < 0) {
                    distanceToJump = 0;
                }
            if (jumpSize < 0){
                distanceToJump = -1;
            }
        }
        else if (distanceToJump < 0){
            distancePassed = true;
            jumpSize -=2;
            if (collisionOccured){
                jumpSize =0;
                distanceToJump = 0;
            }
            float newY = this.getY() - jumpSize;
            setY(newY);
        }

        Log.i("distanceToJump", Integer.toString(distanceToJump));
        Log.i("jumpSize", Integer.toString(jumpSize));
    }

    @Override
    public void draw(ScrollingCamera camera, Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.YELLOW);
        paint.setStyle(Paint.Style.FILL);

        canvas.drawCircle(camera.getRelativeX(getX()), camera.getRelativeY(getY()), getWidth(), paint);
    }

    public int getJumpSize() {
        return jumpSize;
    }

    public void setJumpSize(int jumpSize) {
        this.jumpSize = jumpSize;
    }

    public int getDistanceToJump() {
        return distanceToJump;
    }

    public void setDistanceToJump(int distanceToJump) {
        this.distanceToJump = distanceToJump;
    }
}
