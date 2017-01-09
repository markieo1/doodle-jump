package com.anthony.marco.doodlejump.logic;

import android.graphics.Canvas;
import android.graphics.Rect;

import com.anthony.marco.doodlejump.model.Doodle;
import com.anthony.marco.doodlejump.model.Entity;

import java.util.ArrayList;

/**
 * Created by marco on 4-1-2017.
 */

public class ScrollingCamera {
    private ArrayList<Entity> entities;

    private int totalDrawnEntities;

    /**
     * The current y position of the camera
     */
    private float cameraY;

    private float lastY;

    /**
     * The bounds of the screen
     */
    private Rect bounds;

    public ScrollingCamera(Rect bounds) {
        entities = new ArrayList<>();
        this.bounds = bounds;
        this.cameraY = bounds.top - bounds.height();
    }

    public void update(Doodle doodle) {
        doodle.checkCollision(entities);

        for (Entity entity : entities) {
            entity.update();
        }

        lastY = cameraY;


        // make the camera follow the player
        cameraY = doodle.getY() - bounds.height() / 2;

        // Check if we are not moving downwards, eq. moving positive on the Y axis.
        if (cameraY >= lastY)
            cameraY = lastY;

        // Check if the doodle is not leaving the screen
        if (doodle.getX() >= bounds.width()) {
            doodle.setX(0 - doodle.getWidth());
        } else if (doodle.getX() < 0 - doodle.getWidth()) {
            doodle.setX(bounds.width());
        }
    }

    public void draw(Canvas canvas) {
        int totalDrawn = 0;
        for (Entity entity : entities) {
            if (this.isEntityInScreen(entity)) {
                totalDrawn++;
                entity.draw(this, canvas);
            }
        }
        totalDrawnEntities = totalDrawn;
        //Log.i("ScrollingCamera", "Total drawn: " + totalDrawn);
    }

    /**
     * Checks if an entity is in the screen
     *
     * @param entity The entity to check
     * @return true if the entitiy is within the screen bounds
     */
    private boolean isEntityInScreen(Entity entity) {
        float screenCoordinateY = getRelativeYPosition(entity.getY());

        return (entity.getX() >= bounds.left && entity.getX() + entity.getWidth() <= bounds.right) && (screenCoordinateY >= bounds.top && screenCoordinateY + entity.getHeight() <= bounds.bottom);
    }

    public void setEntities(ArrayList<Entity> entities) {
        this.entities = entities;
    }

    /**
     * Gets the position in screen coordinates
     *
     * @param yPos The position to translate
     * @return the position in screen coordinates
     */
    public float getRelativeYPosition(float yPos) {
        return yPos - this.cameraY;
    }

    public int getTotalDrawnEntities() {
        return totalDrawnEntities;
    }

    public int getScreenWidth() {
        return bounds.width();
    }

    public int getScreenHeight() {
        return bounds.height();
    }

}
