package com.anthony.marco.doodlelibrary.graphics.view.ui;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.anthony.marco.doodlelibrary.graphics.util.TextHelper;

/**
 * Created by marco on 4-3-2017.
 */

public class Label extends GuiItem {
	protected String text;

	protected float textPositionX;

	protected float textPositionY;

	public Label(float x, float y, Paint paint, String text) {
		super(x, y, paint);

		setText(text);
	}

	@Override
	public void draw(Canvas canvas) {
		if (text != null) {
			canvas.drawText(text, textPositionX, textPositionY, paint);
		}
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		if (text == null || text.length() <= 0)
			throw new IllegalArgumentException("Invalid text supplied!");

		this.text = text;

		updateTextPosition();
	}

	protected void updateTextPosition() {
		textPositionX = x + TextHelper.getXToCenterText(text, paint, 0) / 2;

		float yToCenterText = TextHelper.getYToCenterText(text, paint, 0);

		if (y == 0)
			yToCenterText *= 2;

		textPositionY = y + yToCenterText;
	}

	@Override
	public void setX(float x) {
		super.setX(x);
		updateTextPosition();
	}

	@Override
	public void setY(float y) {
		super.setY(y);
		updateTextPosition();
	}
}
