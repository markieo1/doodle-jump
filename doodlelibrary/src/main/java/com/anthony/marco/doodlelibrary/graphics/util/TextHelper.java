package com.anthony.marco.doodlelibrary.graphics.util;

import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

/**
 * Helper class for determining the position of texts
 * Created by marco on 3-3-2017.
 */

public class TextHelper {

	/**
	 * Determines the X to center the text horizontally in the specified width
	 *
	 * @param text                 The text to center
	 * @param fontSize             The size of the font to use
	 * @param widthToFitStringInto The width to fit into
	 * @return The x position (starting from 0) to center the text
	 */
	public static float getXToCenterText(String text, float fontSize, float widthToFitStringInto) {
		return getXToCenterText(text, null, fontSize, widthToFitStringInto);
	}

	/**
	 * Determines the X to center the text horizontally in the specified width
	 *
	 * @param text                 The text to center
	 * @param typeface             The typeface to use for the text
	 * @param fontSize             The size of the font to use
	 * @param widthToFitStringInto The width to fit into
	 * @return The x position (starting from 0) to center the text
	 */
	public static float getXToCenterText(String text, Typeface typeface, float fontSize, float widthToFitStringInto) {
		Paint p = new Paint();

		if (typeface != null)
			p.setTypeface(typeface);
		p.setTextSize(fontSize);

		return getXToCenterText(text, p, widthToFitStringInto);
	}

	/**
	 * Determines the X to center the text horizontally in the specified width
	 *
	 * @param text                 The text to center
	 * @param paint                The paint to use
	 * @param widthToFitStringInto The width to fit into
	 * @return The x position (starting from 0) to center the text
	 */
	public static float getXToCenterText(String text, Paint paint, float widthToFitStringInto) {
		float textWidth = paint.measureText(text);

		if (widthToFitStringInto == 0) {
			return (textWidth / 2);
		}

		return (widthToFitStringInto / 2f) - (textWidth / 2f);
	}

	/**
	 * Determines the Y to center the text vertically in the specified height
	 *
	 * @param text                  The text to center
	 * @param fontSize              The size of the font to use
	 * @param heightToFitStringInto The height to fit into
	 * @return The y position (starting from 0) to center the text
	 */
	public static float getYToCenterText(String text, float fontSize, float heightToFitStringInto) {
		return getYToCenterText(text, null, fontSize, heightToFitStringInto);
	}

	/**
	 * Determines the Y to center the text vertically in the specified height
	 *
	 * @param text                  The text to center
	 * @param typeface              The typeface to use for the text
	 * @param fontSize              The size of the font to use
	 * @param heightToFitStringInto The height to fit into
	 * @return The y position (starting from 0) to center the text
	 */
	public static float getYToCenterText(String text, Typeface typeface, float fontSize, float heightToFitStringInto) {
		Paint p = new Paint();

		if (typeface != null)
			p.setTypeface(typeface);
		p.setTextSize(fontSize);

		return getYToCenterText(text, p, heightToFitStringInto);
	}

	/**
	 * Determines the Y to center the text vertically in the specified height
	 *
	 * @param text                  The text to center
	 * @param paint                 The paint to use
	 * @param heightToFitStringInto The height to fit into
	 * @return The y position (starting from 0) to center the text
	 */
	public static float getYToCenterText(String text, Paint paint, float heightToFitStringInto) {
		if (text == null || text.length() <= 0) {
			throw new IllegalArgumentException("Invalid text string supplied!");
		}

		Rect textBounds = new Rect();
		paint.getTextBounds(text, 0, text.length(), textBounds);
		float textHeight = textBounds.height();

		if (heightToFitStringInto == 0) {
			return (textHeight / 2);
		}

		return (heightToFitStringInto / 2) + (textHeight / 2);
	}

	/**
	 * Determines the correct text size.
	 * If the supplied text size would not match in the width / height.
	 * The lowest of the two would be chosen.
	 *
	 * @param text            The text to measure
	 * @param textSize        The text size that should fit
	 * @param widthToFitInto  The maximum width for the text
	 * @param heightToFitInto The maximum height for the text
	 * @return The maximum text size for the section provided
	 */
	public static float getTextSize(String text, float textSize, float widthToFitInto, float heightToFitInto) {
		Paint paint = new Paint();
		paint.setTextSize(textSize);
		return getTextSize(text, paint, widthToFitInto, heightToFitInto);
	}

	/**
	 * Determines the correct text size.
	 * If the supplied text size would not match in the width / height.
	 * The lowest of the two would be chosen.
	 *
	 * @param text            The text to measure
	 * @param typeface        The typeface to use
	 * @param textSize        The text size that should fit
	 * @param widthToFitInto  The maximum width for the text
	 * @param heightToFitInto The maximum height for the text
	 * @return The maximum text size for the section provided
	 */
	public static float getTextSize(String text, Typeface typeface, float textSize, float widthToFitInto, float heightToFitInto) {
		Paint paint = new Paint();
		paint.setTextSize(textSize);
		paint.setTypeface(typeface);
		return getTextSize(text, paint, widthToFitInto, heightToFitInto);
	}

	/**
	 * Determines the correct text size.
	 * If the supplied text size would not match in the width / height.
	 * The lowest of the two would be chosen.
	 *
	 * @param text            The text to measure
	 * @param paint           The paint to use
	 * @param widthToFitInto  The maximum width for the text
	 * @param heightToFitInto The maximum height for the text
	 * @return The maximum text size for the section provided
	 */
	public static float getTextSize(String text, Paint paint, float widthToFitInto, float heightToFitInto) {
		if (text == null || text.length() <= 0) {
			throw new IllegalArgumentException("Invalid text string supplied!");
		}

		if (paint == null) {
			throw new IllegalArgumentException("Invalid paint supplied!");
		}

		if (widthToFitInto <= 0 || heightToFitInto <= 0) {
			throw new IllegalArgumentException("Invalid width or height supplied!");
		}

		float textSize = paint.getTextSize();
		Rect bounds = new Rect();
		paint.getTextBounds(text, 0, text.length(), bounds);

		float textWidth = bounds.width();
		float textHeight = bounds.height();

		if (textWidth <= widthToFitInto && textHeight <= heightToFitInto) {
			// Already in the bounds, no need to change the textSize
			return textSize;
		}

		float maxTextSizeWidth = (widthToFitInto / textWidth) * textSize;
		float maxTextSizeHeight = (heightToFitInto / textHeight) * textSize;
		return Math.min(maxTextSizeWidth, maxTextSizeHeight);
	}
}
