package com.anthony.marco.doodlelibrary.logic.scene;

import android.graphics.Paint;

import com.anthony.marco.doodlelibrary.graphics.util.TextHelper;
import com.anthony.marco.doodlelibrary.graphics.view.ui.Label;

/**
 * Limited label class containing limits for width and height, eq. max width and max height to determine the position
 * Created by marco on 4-3-2017.
 */

public class LimitedLabel extends Label {
	protected float maxWidth;
	protected float maxHeight;

	public LimitedLabel(float x, float y, Paint paint, String text, float maxWidth, float maxHeight) {
		super(x, y, paint, text);

		this.maxWidth = maxWidth;
		this.maxHeight = maxHeight;
	}

	@Override
	protected void updateTextPosition() {
		textPositionX = x + TextHelper.getXToCenterText(text, paint, maxWidth);

		float yToCenterText = TextHelper.getYToCenterText(text, paint, maxHeight);

		if (y == 0)
			yToCenterText *= 2;

		textPositionY = y + yToCenterText;
	}

	public float getMaxWidth() {
		return maxWidth;
	}

	public void setMaxWidth(float maxWidth) {
		this.maxWidth = maxWidth;
	}

	public float getMaxHeight() {
		return maxHeight;
	}

	public void setMaxHeight(float maxHeight) {
		this.maxHeight = maxHeight;
	}
}
