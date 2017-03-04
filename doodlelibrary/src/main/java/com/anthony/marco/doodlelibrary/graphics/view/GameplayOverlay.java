package com.anthony.marco.doodlelibrary.graphics.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;

import com.anthony.marco.doodlelibrary.R;
import com.anthony.marco.doodlelibrary.graphics.AssetManager;
import com.anthony.marco.doodlelibrary.graphics.util.TextHelper;
import com.anthony.marco.doodlelibrary.graphics.view.ui.Button;
import com.anthony.marco.doodlelibrary.graphics.view.ui.Label;
import com.anthony.marco.doodlelibrary.listener.OnClickListener;
import com.anthony.marco.doodlelibrary.logic.scene.GameplayScene;
import com.anthony.marco.doodlelibrary.logic.scene.LimitedLabel;
import com.anthony.marco.doodlelibrary.logic.scene.Scene;

/**
 * Created by marco on 25-2-2017.
 */

public class GameplayOverlay extends Overlay {

	private Label scoreLabel;
	private LimitedLabel timeRemainingLabel;

	private String scoreText;

	public GameplayOverlay(Scene scene) {
		super(scene);
		scoreText = String.valueOf(0);
	}

	@Override
	public void loadResources(Context context) {
		if (initialized)
			return;

		float textSize = context.getResources().getDimension(R.dimen.textSize);
		float btnStopWidth = context.getResources().getDimension(R.dimen.btnStopWidth);
		float btnStopHeight = context.getResources().getDimension(R.dimen.btnStopHeight);
		Bitmap btnStop = AssetManager.decodeSampledBitmapFromResource(context.getResources(), R.drawable.btn_background, btnStopWidth, btnStopHeight);
		String stopText = context.getResources().getString(R.string.gameplay_btn_stop);
		float stopButtonXPosition = getScene().getWidth() - btnStopWidth;

		Paint paint = new Paint();
		paint.setColor(Color.RED);
		paint.setTextSize(textSize);
		paint.setAntiAlias(true);

		scoreLabel = new Label(0, 0, paint, scoreText);
		timeRemainingLabel = new LimitedLabel(0, 0, paint, "0.0", getScene().getWidth(), 0);

		Paint buttonPaint = new Paint(paint);
		buttonPaint.setTextSize(TextHelper.getTextSize(stopText, textSize, btnStopWidth, btnStopHeight));
		Button button = new Button(stopButtonXPosition, 0, btnStopWidth, btnStopHeight, stopText, btnStop, buttonPaint);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick() {
				int score = (int) getScene().getValues().get(GameplayScene.VAL_SCORE, 0);
				getScene().getGame().stopGame(score);
			}
		});

		guiItems.add(scoreLabel);
		guiItems.add(timeRemainingLabel);
		guiItems.add(button);

		super.loadResources(context);
	}

	@Override
	public void update(double dt) {
		super.update(dt);

		int score = (int) getScene().getValues().get(GameplayScene.VAL_SCORE, 0);
		scoreLabel.setText(String.valueOf(score));

		int timeRemaining = (int) getScene().getValues().get(GameplayScene.VAL_TIME_REMAINING, 0);
		if (timeRemaining != 0)
			timeRemainingLabel.setText(String.valueOf((float) timeRemaining / 1000));
	}
}
