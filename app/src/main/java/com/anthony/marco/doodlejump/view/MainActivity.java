package com.anthony.marco.doodlejump.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.anthony.marco.doodlejump.R;
import com.anthony.marco.doodlejump.adapter.ScoresAdapter;
import com.anthony.marco.doodlejump.database.task.LoadScoresTask;
import com.anthony.marco.doodlejump.database.task.NameInUseTask;
import com.anthony.marco.doodlejump.database.task.SaveScoreTask;
import com.anthony.marco.doodlejump.listener.DatabaseListener;
import com.anthony.marco.doodlelibrary.listener.DoodleListener;
import com.anthony.marco.doodlelibrary.model.Score;
import com.anthony.marco.doodlelibrary.view.DoodleSurfaceView;

import java.util.ArrayList;

public class MainActivity extends Activity implements DoodleListener, DatabaseListener {
    private final String TAG = "MainActivity";

    /**
     * The time (MILLISECONDS) before the game goes into attract mode
     */
    private static final long IDLE_TIME = 10000;

    private DoodleSurfaceView doodleSurfaceView;

    private View gameButtonsView;
    private View mainMenuButtonsView;
    private View gameOverView;
    private View newGameView;
    private View scoreBoardView;
    private View attractView;

    private TextView scoreTextView;
    private TextView finalScoreTextView;
    private EditText playerNameEditText;

    private SensorManager mSensorManager;
    private Sensor mSensor;

    private TextView timer;

    private UiState currentUiState;

    private Score currentScore;

    private ArrayList<Score> scores;
    private ScoresAdapter scoresAdapter;

    private Handler idleHandler;
    private Runnable idleRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUiState(UiState.MAIN_MENU);

        scores = new ArrayList<>();
        scoresAdapter = new ScoresAdapter(getLayoutInflater(), scores);

        idleHandler = new Handler(Looper.getMainLooper());
        idleRunnable = new Runnable() {
            @Override
            public void run() {
                //handle your IDLE state
                goAttractMode();
            }
        };

        doodleSurfaceView = (DoodleSurfaceView) findViewById(R.id.doodle_surface_view);

        gameButtonsView = findViewById(R.id.game_buttons);
        mainMenuButtonsView = findViewById(R.id.main_menu_buttons);
        gameOverView = findViewById(R.id.game_over_layout);
        newGameView = findViewById(R.id.new_game_layout);
        scoreBoardView = findViewById(R.id.score_board_layout);
        attractView = findViewById(R.id.attract_layout);

        scoreTextView = (TextView) gameButtonsView.findViewById(R.id.score_text_view);
        finalScoreTextView = (TextView) gameOverView.findViewById(R.id.final_score_text_view);
        timer = (TextView) findViewById(R.id.player_timer);
        playerNameEditText = (EditText) newGameView.findViewById(R.id.player_name_edit_text);

        ListView scoreboardListView = (ListView) scoreBoardView.findViewById(R.id.score_board_listview);
        scoreboardListView.setAdapter(scoresAdapter);

        Button newGameButton = (Button) mainMenuButtonsView.findViewById(R.id.new_game_button);
        Button aboutUsButton = (Button) mainMenuButtonsView.findViewById(R.id.about_us_button);
        Button scoresButton = (Button) mainMenuButtonsView.findViewById(R.id.scores_button);

        Button stopGameButton = (Button) gameButtonsView.findViewById(R.id.stop_game_button);

        Button playAgainButton = (Button) gameOverView.findViewById(R.id.play_again_button);
        Button mainMenuButton = (Button) gameOverView.findViewById(R.id.main_menu_button);

        Button startGameButton = (Button) newGameView.findViewById(R.id.start_game_button);
        Button backNewGameButton = (Button) newGameView.findViewById(R.id.new_game_back_button);

        Button backScoreBoardButton = (Button) scoreBoardView.findViewById(R.id.score_board_back_button);

        hideSystemUI();

        getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                hideSystemUI();
            }
        });

        // Add wake lock to prevent the screen from sleeping
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // Load the sensors
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

        // Register all the button listeners.
        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newGame();
            }
        });

        aboutUsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), AboutUsActivity.class);

                // Start the intent
                startActivity(intent);
            }
        });

        scoresButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showScoreboard();
            }
        });

        mainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainMenu();
            }
        });

        playAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newGame();
            }
        });

        stopGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopGame();
            }
        });

        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkFields();
            }
        });

        backScoreBoardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainMenu();
            }
        });

        backNewGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainMenu();
            }
        });

        attractView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainMenu();
            }
        });

        // Load the scores
        LoadScoresTask loadScoresTask = new LoadScoresTask(this, this);
        loadScoresTask.execute(true);
    }

    @Override
    protected void onPause() {
        super.onPause();

        doodleSurfaceView.pause();

        idleHandler.removeCallbacks(idleRunnable);

        if (currentUiState == UiState.GAME)
            unregisterListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();

        doodleSurfaceView.resume();

        if (currentUiState == UiState.GAME)
            registerListeners();

        if (currentUiState == UiState.MAIN_MENU) {
            scheduleIdleCallback();
        }

        switchViews();
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();

        if (currentUiState == UiState.MAIN_MENU) {
            scheduleIdleCallback();
        }
    }

    private void scheduleIdleCallback() {
        Log.i(TAG, "Starting delayed idle runnable.");
        idleHandler.removeCallbacks(idleRunnable);
        idleHandler.postDelayed(idleRunnable, IDLE_TIME);
    }

    /**
     * Switches to the scoreboard state
     */
    private void showScoreboard() {
        setUiState(UiState.SCOREBOARD);
        switchViews();
    }

    /**
     * Switches to the new game layout
     */
    private void newGame() {
        setUiState(UiState.NEW_GAME);
        switchViews();

        // Clear the textview
        playerNameEditText.getText().clear();
    }

    /**
     * Starts the game
     */
    private void startGame() {
        // Hide the soft input
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        setUiState(UiState.GAME);

        registerListeners();

        switchViews();

        doodleSurfaceView.startGame(this);
    }

    /**
     * Stops the entire game and goes into the game over callback.
     */
    private void stopGame() {
        unregisterListeners();
        doodleSurfaceView.stopGame();
    }

    /**
     * Goes to the main menu
     */
    private void mainMenu() {
        setUiState(UiState.MAIN_MENU);
        switchViews();

        scheduleIdleCallback();
    }

    /**
     * Registers the listeners for the sensors
     */
    private void registerListeners() {
        mSensorManager.registerListener(doodleSurfaceView, mSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    /**
     * Unregisters the listeners for the sensors
     */
    private void unregisterListeners() {
        mSensorManager.unregisterListener(doodleSurfaceView);
    }

    /**
     * Checks if the player name field has been filled in and is in use.
     */
    private void checkFields() {
        // Check if a name has been filled in!
        String playerName = playerNameEditText.getText().toString();

        if (playerName.isEmpty()) {
            Log.i(TAG, "Player name not filled in!");

            Toast.makeText(this, R.string.enter_name, Toast.LENGTH_SHORT).show();
            return;
        }

        Log.i(TAG, "Checking if name in use, name = " + playerName);

        // Check if the player name is not already used
        NameInUseTask nameInUseTask = new NameInUseTask(this, this);
        nameInUseTask.execute(playerName);
    }

    /**
     * Switches to attract mode
     */
    private void goAttractMode() {
        Log.i(TAG, "Switching to attract mode!");
        setUiState(UiState.ATTRACT);
        switchViews();
    }

    /**
     * Hides the SystemUI
     */
    private void hideSystemUI() {
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        getWindow().getDecorView().setSystemUiVisibility(uiOptions);
    }

    /**
     * Changes the UiState
     *
     * @param uiState the new ui state
     */
    private void setUiState(UiState uiState) {
        this.currentUiState = uiState;
        Log.i(TAG, "UI State changed, new = " + uiState);
    }

    /**
     * Switches the visibility off the views according to the Ui State
     */
    private void switchViews() {
        if (currentUiState != UiState.MAIN_MENU)
            idleHandler.removeCallbacks(idleRunnable);

        switch (currentUiState) {
            case GAME: {
                mainMenuButtonsView.setVisibility(View.GONE);
                gameButtonsView.setVisibility(View.VISIBLE);
                gameOverView.setVisibility(View.GONE);
                newGameView.setVisibility(View.GONE);
                scoreBoardView.setVisibility(View.GONE);
                attractView.setVisibility(View.GONE);
                break;
            }
            case MAIN_MENU: {
                mainMenuButtonsView.setVisibility(View.VISIBLE);
                gameButtonsView.setVisibility(View.GONE);
                gameOverView.setVisibility(View.GONE);
                newGameView.setVisibility(View.GONE);
                scoreBoardView.setVisibility(View.GONE);
                attractView.setVisibility(View.GONE);
                break;
            }
            case GAME_OVER: {
                mainMenuButtonsView.setVisibility(View.GONE);
                gameButtonsView.setVisibility(View.GONE);
                gameOverView.setVisibility(View.VISIBLE);
                newGameView.setVisibility(View.GONE);
                scoreBoardView.setVisibility(View.GONE);
                attractView.setVisibility(View.GONE);
                break;
            }
            case NEW_GAME: {
                mainMenuButtonsView.setVisibility(View.GONE);
                gameButtonsView.setVisibility(View.GONE);
                gameOverView.setVisibility(View.GONE);
                newGameView.setVisibility(View.VISIBLE);
                scoreBoardView.setVisibility(View.GONE);
                attractView.setVisibility(View.GONE);
                break;
            }
            case SCOREBOARD: {
                mainMenuButtonsView.setVisibility(View.GONE);
                gameButtonsView.setVisibility(View.GONE);
                gameOverView.setVisibility(View.GONE);
                newGameView.setVisibility(View.GONE);
                scoreBoardView.setVisibility(View.VISIBLE);
                attractView.setVisibility(View.GONE);
                break;
            }
            case ATTRACT: {
                mainMenuButtonsView.setVisibility(View.GONE);
                gameButtonsView.setVisibility(View.GONE);
                gameOverView.setVisibility(View.GONE);
                newGameView.setVisibility(View.GONE);
                scoreBoardView.setVisibility(View.GONE);
                attractView.setVisibility(View.VISIBLE);
                break;
            }
        }
    }

    /**
     * Saves the score in the database
     *
     * @param score The score to save
     */
    private void saveScore(Score score) {
        // Save the score in the database
        SaveScoreTask saveScoreTask = new SaveScoreTask(this, this);
        saveScoreTask.execute(score);
    }

    @Override
    public void gameOver(final int score) {
        // Run On UI Thread since this callback is called from another thread
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                currentScore.setScore(score);

                saveScore(currentScore);

                unregisterListeners();
                setUiState(UiState.GAME_OVER);
                switchViews();

                finalScoreTextView.setText(String.valueOf(score));
            }
        });
    }

    @Override
    public void scoreChanged(final int newScore) {
        // Run On UI Thread since this callback is called from another thread
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                currentScore.setScore(newScore);
                // Update the score label
                scoreTextView.setText(String.valueOf(newScore));
            }
        });
    }

    @Override
    public void updateTimer(final long timeLeft) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String timerLeftInSeconds = String.valueOf((float) timeLeft / 1000);

                timer.setText(timerLeftInSeconds);
            }
        });
    }

    @Override
    public void scoresLoaded(ArrayList<Score> scores) {
        this.scores.clear();
        this.scores.addAll(scores);
        scoresAdapter.notifyDataSetChanged();
    }

    @Override
    public void nameInUseChecked(String name, boolean inUse) {
        if (currentUiState != UiState.NEW_GAME)
            return;

        if (inUse) {
            Toast.makeText(this, R.string.name_in_use, Toast.LENGTH_SHORT).show();
            return;
        }

        currentScore = new Score(name, 0);

        // Start game since the name is not already in use
        startGame();
    }
}
