package com.anthony.marco.doodlelibrary.graphics.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.anthony.marco.doodlelibrary.R;
import com.anthony.marco.doodlelibrary.graphics.AssetManager;
import com.anthony.marco.doodlelibrary.graphics.util.TextHelper;
import com.anthony.marco.doodlelibrary.graphics.view.ui.Button;
import com.anthony.marco.doodlelibrary.listener.OnClickListener;
import com.anthony.marco.doodlelibrary.logic.scene.GameplayScene;
import com.anthony.marco.doodlelibrary.logic.scene.Scene;

/**
 * Created by marco on 25-2-2017.
 */

public class GameplayOverlay extends Overlay {
	private float textSize;

	private String scoreText;
	private String timeRemainingText;

	private Paint paint;

	private float scoreTextPositionX;
	private float textPositionY;

	private float timeRemainingTextPositionX;

	public GameplayOverlay(Scene scene) {
		super(scene);
		scoreTextPositionX = 0;
		scoreText = String.valueOf(0);
	}

	@Override
	public void loadResources(Context context) {
		super.loadResources(context);
		textSize = context.getResources().getDimension(R.dimen.textSize);
		float btnStopWidth = context.getResources().getDimension(R.dimen.btnStopWidth);
		float btnStopHeight = context.getResources().getDimension(R.dimen.btnStopHeight);
		Bitmap btnStop = AssetManager.decodeSampledBitmapFromResource(context.getResources(), R.drawable.btn_background, btnStopWidth, btnStopHeight);
		String stopText = context.getResources().getString(R.string.gameplay_btn_stop);

		paint = new Paint();
		paint.setColor(Color.RED);
		paint.setTextSize(textSize);
		paint.setAntiAlias(true);

		// Times two since it started the counting from 0
		textPositionY = (TextHelper.getYToCenterText(scoreText, paint, 0) * 2);


		float xPos = scene.getWidth() - btnStopWidth;
		Paint buttonPaint = new Paint(paint);
		buttonPaint.setTextSize(TextHelper.getTextSize(stopText, textSize, btnStopWidth, btnStopHeight));
		Button button = new Button(xPos, 0, btnStopWidth, btnStopHeight, stopText, btnStop, buttonPaint);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick() {
				int score = (int) scene.getValues().get(GameplayScene.VAL_SCORE, 0);
				scene.getGame().stopGame(score);
			}
		});
		buttons.add(button);
	}

	@Override
	public void update(double dt) {
		super.update(dt);

		int score = (int) scene.getValues().get(GameplayScene.VAL_SCORE, 0);
		scoreText = String.valueOf(score);

		int timeRemaining = (int) scene.getValues().get(GameplayScene.VAL_TIME_REMAINING, 0);
		if (timeRemaining != 0)
			timeRemainingText = String.valueOf((float) timeRemaining / 1000);

		timeRemainingTextPositionX = TextHelper.getXToCenterText(timeRemainingText, paint, scene.getWidth());
	}

	@Override
	public void draw(Canvas canvas) {
		if (scoreText != null)
			canvas.drawText(scoreText, scoreTextPositionX, textPositionY, paint);

		if (timeRemainingText != null) {
			canvas.drawText(timeRemainingText, timeRemainingTextPositionX, textPositionY, paint);
		}

		super.draw(canvas);
	}
}
