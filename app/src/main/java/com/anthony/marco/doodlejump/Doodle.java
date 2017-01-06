package com.anthony.marco.doodlejump;

import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
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

    private float highestY;

    public Doodle(float x, float y, float height, float width, Bitmap image) {
        super(x, y, height, width, image);
        this.velocityX = 0;
        this.velocityY = 0;
        this.shouldFall = true;
    }

    public void checkCollision(ArrayList<Entity> entities) {
        boolean colliding = collidingWithPlatforms(entities);
        if (colliding) {
            this.shouldFall = false;
            this.velocityY = 0;
        } else {
            // we are not colliding so we should fall
            this.shouldFall = true;
        }
    }

    public void handleInput() {
        //TODO: handle input..
    }

    private boolean collidingWithPlatforms(ArrayList<Entity> entities) {
        boolean isCollidingWithPlatform = false;
        for (Entity platform : entities) {
            if (platform instanceof Doodle)
                continue;

            if (isColliding(platform, velocityY)) {
                isCollidingWithPlatform = true;

                // Move because we might have passed the platform by a bit.
                this.setY(platform.getY() - this.getHeight());
                break;
            }

        }

        return isCollidingWithPlatform;
    }

    /**
     * Checks if the doodle is colliding with the specified entity. Taking into account the velocity for the bounding box of the entity.
     *
     * @param entity    The entity to check if we are colliding with it
     * @param velocityY The velocity to take into account.
     * @return
     */
    private boolean isColliding(Entity entity, float velocityY) {
        boolean isColliding = false;

        float myXPosition = getX();
        float myWidth = getWidth();
        float myXEnd = myXPosition + myWidth;

        float entityXPosition = entity.getX();
        float entityWidth = entity.getWidth();
        float entityXEnd = entityXPosition + entityWidth;

        if (myXPosition >= entityXPosition && myXEnd <= entityXEnd) {
            // Doodle is between the platform
            float myYPosition = getY();
            float myHeight = getHeight();
            float myYEnd = myYPosition + myHeight;

            float entityYPosition = entity.getY();
            float entityHeight = entity.getHeight() + velocityY;
            float entityYEnd = entityYPosition + entityHeight;

            if (myYEnd >= entityYPosition && myYEnd <= entityYEnd) {
                if (shouldFall)
                    isColliding = true;
            }
        }

        return isColliding;
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

    @Override
    public void setY(float y) {
        super.setY(y);

        if (y <= this.highestY) {
            this.highestY = y;
        }
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

    public boolean isInScreen(ScrollingCamera camera) {
        float relativeYPosition = camera.getRelativeYPosition(getY());

        return camera.getScreenHeight() > relativeYPosition;
    }

    public float getHighestY() {
        return highestY;
    }
}
