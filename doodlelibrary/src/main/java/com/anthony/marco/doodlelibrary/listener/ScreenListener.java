package com.anthony.marco.doodlelibrary.listener;

/**
 * Created by marco on 4-1-2017.
 */

public interface ScreenListener {
	/**
	 * Occurs when the screen is touched
	 *
	 * @param xPosition The x position of the touch
	 * @param yPosition The y position of the touch
	 */
	void screenTouched(float xPosition, float yPosition);

	/**
	 * Occurs when the screen size is changed
	 *
	 * @param width  The new width
	 * @param height The new height
	 */
	void screenSizeChanged(int width, int height);

	/**
	 * Occurs when the rotation is changed
	 *
	 * @param newRotation the new value of rotation in degrees
	 */
	void rotationChanged(float newRotation);
}
