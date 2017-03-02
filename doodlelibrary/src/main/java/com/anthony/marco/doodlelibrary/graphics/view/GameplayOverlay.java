package com.anthony.marco.doodlelibrary.graphics.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.anthony.marco.doodlelibrary.R;
import com.anthony.marco.doodlelibrary.logic.scene.GameplayScene;
import com.havermans.marco.gamelibrary.graphics.Overlay;
import com.havermans.marco.gamelibrary.logic.Scene;

/**
 * Created by marco on 25-2-2017.
 */

public class GameplayOverlay extends Overlay {
	private float textSize;

	private String scoreText;
	private String timeRemainingText;

	private Paint paint;

	private int scoreTextPositionX;
	private int textPositionY;

	private int timeRemainingTextPositionX;

	public GameplayOverlay(Scene scene) {
		super(scene);
		scoreTextPositionX = 0;
	}


	@Override
	public void loadResources(Context context) {
		super.loadResources(context);
		textSize = context.getResources().getDimension(R.dimen.textSize);

		paint = new Paint();
		paint.setColor(Color.RED);
		paint.setTextSize(textSize);
		paint.setAntiAlias(true);

		Paint.FontMetrics metric = paint.getFontMetrics();
		int textHeight = (int) Math.ceil(metric.descent - metric.ascent);
		textPositionY = (int) (textHeight - metric.descent);
	}

	@Override
	public void update(double dt) {
		super.update(dt);

		int score = (int) scene.getValues().get(GameplayScene.VAL_SCORE, 0);
		scoreText = String.valueOf(score);

		int timeRemaining = (int) scene.getValues().get(GameplayScene.VAL_TIME_REMAINING, 0);
		if (timeRemaining != 0)
			timeRemainingText = String.valueOf((float) timeRemaining / 1000);

		int textWidth = (int) paint.measureText(timeRemainingText);
		timeRemainingTextPositionX = (scene.getWidth() / 2) - (textWidth / 2);
	}

	//@Override
	public void draw(Canvas canvas) {
		//super.draw(canvas);

		if (scoreText != null)
			canvas.drawText(scoreText, scoreTextPositionX, textPositionY, paint);

		if (timeRemainingText != null) {
			canvas.drawText(timeRemainingText, timeRemainingTextPositionX, textPositionY, paint);
		}
	}
}
