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
    /**
     * The entities that can be updated/drawn
     */
    private ArrayList<Entity> entities;

    /**
     * The current y position of the camera
     */
    private float cameraY;

    /**
     * The bounds of the screen
     */
    private Rect bounds;

    public ScrollingCamera(Rect bounds) {
        entities = new ArrayList<>();
        this.bounds = bounds;
        this.cameraY = bounds.top - bounds.height();
    }

    /**
     * Updates all entities and updates the cameraY to center the Doodle.
     * @param doodle The doodle to follow
     */
    public void update(Doodle doodle) {
        for (Entity entity : entities) {
            entity.update();
        }

        float lastY = cameraY;

        // make the camera follow the player
        cameraY = doodle.getY() - getScreenHeight() / 2;

        // Check if we are not moving downwards, eq. moving positive on the Y axis.
        if (cameraY >= lastY)
            cameraY = lastY;

        // Check if the doodle is not leaving the screen
        if (doodle.getX() >= getScreenWidth()) {
            doodle.setX(0 - doodle.getWidth());
        } else if (doodle.getX() < 0 - doodle.getWidth()) {
            doodle.setX(getScreenWidth());
        }
    }

    /**
     * Draw all the entities on the specified canvas
     * @param canvas The canvas to draw onto
     */
    public void draw(Canvas canvas) {
        for (Entity entity : entities) {
            if (this.isEntityInScreen(entity)) {
                entity.draw(this, canvas);
            }
        }
    }

    /**
     * Checks if an entity is in the screen
     *
     * @param entity The entity to check
     * @return true if the entitiy is within the screen bounds
     */
    private boolean isEntityInScreen(Entity entity) {
        float screenCoordinateY = getRelativeYPosition(entity.getY());

        return (entity.getX() >= bounds.left && entity.getX() + entity.getWidth() <= getScreenWidth()) && (screenCoordinateY >= bounds.top && screenCoordinateY + entity.getHeight() <= getScreenHeight());
    }

    /**
     * Sets the entities for this camera instance
     * @param entities The entities to set
     */
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

    /**
     * Gets the screen bounds width
     * @return The screen width
     */
    public int getScreenWidth() {
        return bounds.width();
    }

    /**
     * Gets the screen bounds height
     * @return The screen height
     */
    public int getScreenHeight() {
        return bounds.height();
    }

}
