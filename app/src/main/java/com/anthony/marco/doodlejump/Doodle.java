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
    private final String TAG = "Doodle";

    private float velocityX;
    private float velocityY;
    private boolean shouldFall;

    public Doodle(float x, float y, float height, float width, Bitmap image) {
        super(x, y, height, width, image);
        this.velocityX = 0;
        this.velocityY = 0;
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

                // TODO: Check if the ball is not floating in the air when not jumping
            }
        }
    }

    public void handleInput() {
        //TODO: handle input..
    }

    @Override
    public void update() {
        super.update();

        if (shouldFall) {
            velocityY += 1;
        }

        float newX = this.getX() + velocityX;
        float newY = this.getY() + velocityY;

        setX(newX);
        setY(newY);
    }

    @Override
    public void draw(ScrollingCamera camera, Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.YELLOW);
        paint.setStyle(Paint.Style.FILL);

        canvas.drawCircle(getX(), camera.getRelativeYPosition(getY()), getWidth(), paint);
    }

    public void setJumpSize(int jumpSize) {
        Log.i(TAG, "Jump Size changed, new: " + jumpSize);

        this.velocityY = -jumpSize;

        TimerTask fallingTask = new TimerTask() {
            @Override
            public void run() {
                shouldFall = true;

                Log.i(TAG, "Ball falling commenced!");
            }
        };

        Timer timer = new Timer();
        timer.schedule(fallingTask, 100);
    }

    public void setVelocityX(float velocityX) {
        this.velocityX = velocityX;
    }
}
