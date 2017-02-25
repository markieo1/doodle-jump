package com.anthony.marco.doodlelibrary.logic.scene;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

import com.anthony.marco.doodlelibrary.R;
import com.anthony.marco.doodlelibrary.graphics.AssetManager;
import com.anthony.marco.doodlelibrary.graphics.animation.Animation;
import com.anthony.marco.doodlelibrary.graphics.view.GameplayOverlay;
import com.anthony.marco.doodlelibrary.logic.DifficultyHandler;
import com.anthony.marco.doodlelibrary.logic.DoodleGame;
import com.anthony.marco.doodlelibrary.logic.ScrollingCamera;
import com.anthony.marco.doodlelibrary.model.Doodle;
import com.anthony.marco.doodlelibrary.model.Entity;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by marco on 25-2-2017.
 */

public class GameplayScene extends Scene {
	private static final String TAG = "GameplayScene";

	/**
	 * The start y position for the doodle
	 */
	private static final int DOODLE_START_Y = -100;

	/**
	 * The doodle width
	 */
	private static final int DOODLE_WIDTH = 50;

	/**
	 * The doodle height
	 */
	private static final int DOODLE_HEIGHT = 50;

	/**
	 * The jump size for the doodle
	 */
	private static final int DOODLE_JUMP_SIZE = 40;

	/**
	 * The gravity of the doodle
	 */
	private static final float DOODLE_GRAVITY = 0.8f;

	/**
	 * The platform width
	 */
	private static final int PLATFORM_WIDTH = 100;

	/**
	 * The platform height
	 */
	private static final int PLATFORM_HEIGHT = 10;

	/**
	 * The min difference from the previous Y generated
	 */
	private static final int MIN_DIFFERENCE = 100;

	/**
	 * The maximum (exclusive) difference from the previous Y generated
	 */
	private static final int MAX_DIFFERENCE = 500;

	/**
	 * The amount of entities to generate
	 */
	private static final int GENERATION_COUNT = 100;

	/**
	 * The threshold when the generation should commence again.
	 * This is the lastYGenerated + the threshold, since we are negative on the Y Axis.
	 */
	private static final int GENERATION_START_THRESHOLD = 3000;

	/**
	 * The interval (MILLISECONDS) in which the score label should be updated
	 */
	private static final int SCORE_UPDATE_INTERVAL = 100;

	/**
	 * Determines if the timer is enabled!
	 */
	private static final boolean TIMER_ENABLED = true;

	public static final int VAL_SCORE = 1;
	public static final int VAL_TIME_REMAINING = 2;

	private boolean isStarted;

	/**
	 * The camera used for drawing purposes
	 */
	private ScrollingCamera camera;

	/**
	 * The Doodle eq. Player
	 */
	private Doodle doodle;

	/**
	 * All the entities currently in the game
	 */
	private ArrayList<Entity> entities;

	/**
	 * The difficulty handler
	 */
	private DifficultyHandler difficultyHandler;

	/**
	 * Check wether the timer is started or not
	 */
	private boolean isTimerStarted;

	/**
	 * The time needed to count down in seconds
	 */
	private int timerTimeToCountDownInS;

	/**
	 * The time needed to count down in milliseconds
	 */
	private int timerTimeToCountDownInMS;

	/**
	 * The y position of the last generated platform
	 */
	private float lastYGenerated;

	/**
	 * The thread pool
	 */
	private ScheduledExecutorService ses;

	/**
	 * Determines if the timer needs a reset
	 */
	private boolean timerNeedReset;

	/**
	 * The loop in which the countdown occurs
	 */
	private ScheduledFuture timerLoop;

	/**
	 * The timer that updates the label and the count down time
	 */
	private ScheduledFuture timerCountDown;

	public GameplayScene(DoodleGame game) {
		super(game);
		entities = new ArrayList<>();

		timerTimeToCountDownInS = 8;
		timerTimeToCountDownInMS = timerTimeToCountDownInS * 1000;
		this.isStarted = false;

		overlays.add(new GameplayOverlay(this));
	}

	@Override
	public void loadResources(Context context) {
		super.loadResources(context);
		AssetManager.getInstance().addBitmapToMemoryCache(R.drawable.platform, AssetManager.decodeSampledBitmapFromResource(context.getResources(), R.drawable.platform, PLATFORM_WIDTH, PLATFORM_HEIGHT));
		AssetManager.getInstance().addBitmapToMemoryCache(R.drawable.circle, AssetManager.decodeSampledBitmapFromResource(context.getResources(), R.drawable.circle, DOODLE_WIDTH, DOODLE_HEIGHT));
		AssetManager.getInstance().addBitmapToMemoryCache(R.drawable.circle_blue, AssetManager.decodeSampledBitmapFromResource(context.getResources(), R.drawable.circle_blue, DOODLE_WIDTH, DOODLE_HEIGHT));
		AssetManager.getInstance().addBitmapToMemoryCache(R.drawable.circle_green, AssetManager.decodeSampledBitmapFromResource(context.getResources(), R.drawable.circle_green, DOODLE_WIDTH, DOODLE_HEIGHT));
	}

	@Override
	public void start() {
		ses = Executors.newScheduledThreadPool(2);

		entities.clear();

		camera = new ScrollingCamera(new Rect(0, 0, game.getScreenWidth(), game.getScreenHeight()));
		doodle = new Doodle(game.getScreenWidth() / 2 - DOODLE_WIDTH, DOODLE_START_Y, DOODLE_WIDTH, DOODLE_HEIGHT, DOODLE_JUMP_SIZE, DOODLE_GRAVITY);
		Animation doodleAnimation = Animation.fromBitmaps(1000, AssetManager.getInstance().getBitmapFromMemCache(R.drawable.circle), AssetManager.getInstance().getBitmapFromMemCache(R.drawable.circle_blue), AssetManager.getInstance().getBitmapFromMemCache(R.drawable.circle_green));
		doodle.setAnimation(doodleAnimation);
		entities.add(doodle);

		// Add a platform right below the Doodle to stop it from failing the game when started
		Entity platform = new Entity(doodle.getX() - (doodle.getWidth() / 2), doodle.getY() + doodle.getHeight(), PLATFORM_WIDTH, PLATFORM_HEIGHT, AssetManager.getInstance().getBitmapFromMemCache(R.drawable.platform));
		entities.add(platform);

		difficultyHandler = new DifficultyHandler(PLATFORM_WIDTH, MIN_DIFFERENCE, MAX_DIFFERENCE, timerTimeToCountDownInS);

		// Reset the last y generated
		lastYGenerated = platform.getY();

		ses.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				if (doodle != null) {
					int resultScore = Math.round(doodle.getHighestY() * -1);
					values.put(GameplayScene.VAL_SCORE, resultScore);
				}
			}
		}, 0, SCORE_UPDATE_INTERVAL, TimeUnit.MILLISECONDS);

		resetTimer();
		startTimer();
		timerNeedReset = true;

		isStarted = true;
	}

	@Override
	public void update(double deltaTime) {
		if (!isStarted)
			return;

		if (doodle.checkCollision(entities)) {
			if (isTimerStarted && timerNeedReset) {
				timerNeedReset = false;
				resetTimer();
				startTimer();
			}
		} else {
			timerNeedReset = true;
		}

		camera.update(deltaTime, doodle);

		generatePlatforms();
		cleanupOldPlatforms();

		if (!doodle.isInScreen(camera)) {
			Log.i(TAG, "Doodle left screen");
			float resultScore = doodle.getHighestY() * -1;
			game.stopGame(Math.round(resultScore));
		}

		if (difficultyHandler.needNewValues(doodle.getHighestY())) {
			difficultyHandler.setNewValues(doodle.getHighestY());
		}

		super.update(deltaTime);
	}

	@Override
	public void draw(Canvas canvas) {
		if (!isStarted)
			return;

		camera.draw(canvas);

		super.draw(canvas);
	}

	@Override
	public void cleanup() {
		this.isStarted = false;
		if (ses != null) {
			Log.i(TAG, "Shutting down ScheduledExecutorService");
			ses.shutdownNow();
		}
	}

	@Override
	public void onScreenTouched(float x, float y) {
		super.onScreenTouched(x, y);

		if (!isStarted)
			return;

		if (doodle != null)
			doodle.jump();

		timerNeedReset = true;

		if (!isTimerStarted) {
			startTimer();
		}
	}

	@Override
	public void onRotationChanged(float newRotation) {
		super.onRotationChanged(newRotation);

		if (!isStarted)
			return;

		// Roll -90 is device rotated completely to the eg. landscape mode
		// Roll 90 is device rotated to the left eg. landscape mode
		// So we need to flip the roll
		float velocityX = newRotation * -1;

		if (doodle != null)
			doodle.setVelocityX(velocityX);
	}

	/**
	 * Generates platforms for the player to jump onto.
	 */
	private void generatePlatforms() {
		if (lastYGenerated < 0 && doodle.getHighestY() > lastYGenerated + GENERATION_START_THRESHOLD)
			return;

		Log.i(TAG, "Generating new platforms");

		Random rnd = new Random();

		for (int i = 0; i < GENERATION_COUNT; i++) {
			int x = rnd.nextInt(game.getScreenWidth() - 100) + 1;

			float randomY = rnd.nextFloat() * (difficultyHandler.getMaxDifference() - difficultyHandler.getMinDifference()) + difficultyHandler.getMinDifference();
			if (randomY > 0) {
				// Make it negative since we are going up
				randomY *= -1;
			}

			float platformY = lastYGenerated + randomY;

			lastYGenerated = platformY;

			Entity entity = new Entity(x, platformY, difficultyHandler.getPlatformWidth(), PLATFORM_HEIGHT, AssetManager.getInstance().getBitmapFromMemCache(R.drawable.platform));
			entities.add(entity);
		}

		camera.setEntities(entities);

		Log.i(TAG, "New platforms generated, count = " + GENERATION_COUNT);
	}

	/**
	 * Cleans up the platforms that are not in screen anymore
	 */
	private void cleanupOldPlatforms() {
		float screenBorder = (doodle.getY() + game.getScreenHeight() / 2);

		ArrayList<Integer> entityIndicesToRemove = new ArrayList<>();
		for (Entity entity : entities) {
			if (entity instanceof Doodle)
				continue;

			// Remove all entities under the screen border
			if (entity.getY() >= screenBorder) {
				entityIndicesToRemove.add(entities.indexOf(entity));
			}
		}

		for (int index : entityIndicesToRemove) {
			entities.remove(index);
		}

		if (entityIndicesToRemove.size() > 0) {
			camera.setEntities(entities);
			Log.i(TAG, "Cleaning up, total cleaned up = " + entityIndicesToRemove.size());
		}
	}

	/**
	 * Starts the current timer if not initialized
	 */
	public void startTimer() {
		if (!TIMER_ENABLED)
			return;

		if (timerLoop == null) {
			//timerNeedReset = false;
			isTimerStarted = true;
			Log.i(TAG, "Timer started");
			timerCountDown = ses.scheduleAtFixedRate(new Runnable() {
				@Override
				public void run() {
					timerTimeToCountDownInMS -= 100;
					values.append(GameplayScene.VAL_TIME_REMAINING, timerTimeToCountDownInMS);
				}
			}, 0, 100, TimeUnit.MILLISECONDS);

			timerLoop = ses.schedule(new Runnable() {
				@Override
				public void run() {
					timerCountDown.cancel(true);
					float resultScore = doodle.getHighestY() * -1;
					game.stopGame(Math.round(resultScore));
				}
			}, timerTimeToCountDownInS, TimeUnit.SECONDS);
		}
	}

	/**
	 * Resets the timer and the timer count down
	 */
	public void resetTimer() {
		if (!TIMER_ENABLED)
			return;

		if (timerLoop != null) {
			Log.i(TAG, "Timer reseted");
			timerTimeToCountDownInS = difficultyHandler.getInitialCountDownTimerInS();
			timerTimeToCountDownInMS = timerTimeToCountDownInS * 1000;
			values.append(GameplayScene.VAL_TIME_REMAINING, timerTimeToCountDownInMS);
			timerCountDown.cancel(true);
			isTimerStarted = false;
			timerLoop.cancel(true);
			timerLoop = null;
		}
	}
}
