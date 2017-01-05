package com.anthony.marco.doodlejump;

/**
 * Created by marco on 4-1-2017.
 */

public interface ScreenListener {
    /**
     * The screen touched callback
     * @param xPosition The x position of the touch
     * @param yPosition The y position of the touch
     */
    void screenTouched(float xPosition, float yPosition);
    void screenSizeChanged(int width, int height);

    /**
     * Occurs when the rotation is changed
     * @param newRotation the new value of rotation in degrees
     */
    void rotationChanged(float newRotation);
}
