package com.anthony.marco.doodlelibrary.model;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by anthony on 3-1-2017.
 */

public class Doodle extends Entity {
	private final String TAG = "Doodle";
	/**
	 * The highest y achieved by the doodle
	 */
	private float highestY;

	/**
	 * The jump size
	 */
	private float jumpSize;

	/**
	 * The gravity of the doodle
	 */
	private float gravity;

	private boolean grounded;

	public Doodle(float x, float y, float width, float height, float jumpSize, float gravity, Bitmap image) {
		super(x, y, width, height, image);
		this.jumpSize = jumpSize;
		this.gravity = gravity;
		this.grounded = false;

		Log.i(TAG, "New Doodle created, jumpSize = " + jumpSize + ", gravity = " + gravity);
	}

	/**
	 * Checks the collision with the Doodle in comparison to all the other entities
	 *
	 * @param entities The entities to check
	 * @return True if the Doodle is colliding, else false
	 */
	public boolean checkCollision(ArrayList<Entity> entities) {
		boolean colliding = collidingWithPlatforms(entities);
		if (colliding) {
			this.grounded = true;
		} else {
			// we are not colliding so we aren't on any ground
			this.grounded = false;
		}
		return colliding;
	}

	/**
	 * Checks if the doodle is colliding with platforms.
	 *
	 * @param entities The platforms to check
	 * @return True if Doodle is colliding, else false
	 */
	private boolean collidingWithPlatforms(ArrayList<Entity> entities) {
		boolean isCollidingWithPlatform = false;
		for (Entity entity : entities) {
			if (!(entity instanceof Platform))
				continue;

			Platform platform = (Platform) entity;

			if (isColliding(platform, velocityY)) {
				isCollidingWithPlatform = true;

				// Move because we might have passed the platform by a bit.
				this.setY(platform.getY() - this.getHeight());

				// Get the jump boost for the platform
				this.setVelocityY(-platform.getJumpBoost());
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
	 * @return True if there is collision, else false
	 */
	private boolean isColliding(Entity entity, float velocityY) {
		boolean isColliding = false;

		float myXPosition = getX();
		float myWidth = getWidth();
		float margin = (myWidth / 4);
		float myXLeftMargin = myXPosition - margin;
		float myXRightMargin = myXPosition + margin;
		float myXEnd = myXLeftMargin + myWidth;

		float entityXPosition = entity.getX();
		float entityWidth = entity.getWidth();
		float entityXEnd = entityXPosition + entityWidth;

		if (myXRightMargin <= entityXEnd && entityXPosition <= myXEnd) {
			// Doodle is between the platform
			float myYPosition = getY();
			float myHeight = getHeight();
			float myYEnd = myYPosition + myHeight;

			float entityYPosition = entity.getY();
			float entityHeight = entity.getHeight() + velocityY;
			float entityYEnd = entityYPosition + entityHeight;

			if (myYEnd >= entityYPosition && myYEnd <= entityYEnd) {
				isColliding = true;
			}
		}

		return isColliding;
	}

	@Override
	public void update() {
		if (!grounded) {
			setVelocityY(getVelocityY() + gravity);
		}

		super.update();
	}

	@Override
	public void setY(float y) {
		super.setY(y);

		if (y <= this.highestY) {
			this.highestY = y;
		}
	}

	public void jump() {
		Log.i(TAG, "Doodle jump occurred!");

		this.velocityY = -jumpSize;
	}

	public float getHighestY() {
		return highestY;
	}
}
