package com.anthony.marco.doodlejump;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by anthony on 3-1-2017.
 */

public class Doodle extends Entity {
    private float velocityX;
    private float velocityY;
    private boolean shouldFall;
    private TimerTask fallingTask;

    public Doodle(float x, float y, float height, float width, Bitmap image, float velocityX, final float velocityY) {
        super(x, y, height, width, image);
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.shouldFall = true;
    }

    public void checkCollision(ArrayList<Entity> entities) {
        for (Entity entity : entities) {
            // If the entity is a doodle just skip
            if (entity instanceof Doodle)
                continue;

            if (this.getX() > entity.getX() && this.getX() < (entity.getX() + entity.getWidth())) {
                float doodlePos = this.getY() + this.velocityY;
                float doodlePosHeight = doodlePos + this.getHeight() + this.velocityY;

                float platformPos = entity.getY();
                float platformPosHeight = platformPos + entity.getHeight();

                if (platformPos > doodlePos && platformPosHeight < doodlePosHeight) {
                    this.shouldFall = false;
                    this.velocityY = 0;
                    this.setY(platformPos - this.getHeight());
                    break;
                }


                /*if (this.getY() + this.getHeight() < entity.getY() && this.getY() + this.getHeight() > entity.getY() - 20) {
                        this.velocityY = 0;
                    break;
                }*/
            }
        }
    }

    public void handleInput() {
        //TODO: handle input..
    }

    @Override
    public void update() {
        super.update();

        if(shouldFall){
            velocityY += 1;
        }

        float newY = this.getY() + velocityY;

        setY(newY);

        Log.i("Velocity", "" + velocityY);
    }

    @Override
    public void draw(ScrollingCamera camera, Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.YELLOW);
        paint.setStyle(Paint.Style.FILL);

        canvas.drawCircle(getX(), camera.getRelativeYPosition(getY()), getWidth(), paint);
    }

    public void setJumpSize(int jumpSize) {
        this.velocityY = -jumpSize;

        fallingTask = new TimerTask() {
            @Override
            public void run() {
                shouldFall = true;
                velocityY = 0;
            }
        };

        Timer timer = new Timer();
        timer.schedule(fallingTask, 2000);
    }
}
