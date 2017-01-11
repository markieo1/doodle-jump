package com.anthony.marco.doodlejump.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.anthony.marco.doodlejump.App;
import com.anthony.marco.doodlejump.R;
import com.anthony.marco.doodlejump.adapter.ScoresAdapter;
import com.anthony.marco.doodlejump.database.task.LoadScoresTask;
import com.anthony.marco.doodlejump.database.task.NameInUseTask;
import com.anthony.marco.doodlejump.database.task.SaveScoreTask;
import com.anthony.marco.doodlejump.listener.DatabaseListener;
import com.anthony.marco.doodlejump.listener.DoodleListener;
import com.anthony.marco.doodlejump.model.Score;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends Activity implements DoodleListener, DatabaseListener {
    private final String TAG = "MainActivity";

    private DoodleSurfaceView doodleSurfaceView;

    private View gameButtonsView;
    private View mainMenuButtonsView;
    private View gameOverView;
    private View newGameView;
    private View scoreBoardView;

    private TextView scoreTextView;
    private TextView finalScoreTextView;
    private EditText playerNameEditText;

    private SensorManager mSensorManager;
    private Sensor mSensor;

    private UiState currentUiState;

    private Score currentScore;

    private ArrayList<Score> scores;
    private ScoresAdapter scoresAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUiState(UiState.MAIN_MENU);

        scores = new ArrayList<>();
        scoresAdapter = new ScoresAdapter(getLayoutInflater(), scores);

        doodleSurfaceView = (DoodleSurfaceView) findViewById(R.id.doodle_surface_view);

        gameButtonsView = findViewById(R.id.game_buttons);
        mainMenuButtonsView = findViewById(R.id.main_menu_buttons);
        gameOverView = findViewById(R.id.game_over_layout);
        newGameView = findViewById(R.id.new_game_layout);
        scoreBoardView = findViewById(R.id.score_board_layout);

        scoreTextView = (TextView) gameButtonsView.findViewById(R.id.score_text_view);
        finalScoreTextView = (TextView) gameOverView.findViewById(R.id.final_score_text_view);
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

        Button backButton = (Button) scoreBoardView.findViewById(R.id.score_board_back_button);

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
        mSensorManager = (SensorManager) App.getContext().getSystemService(Context.SENSOR_SERVICE);
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
                setUiState(UiState.MAIN_MENU);
                switchViews();
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

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scoreboardBack();
            }
        });

        // Load the scores
        LoadScoresTask loadScoresTask = new LoadScoresTask(this, this);
        loadScoresTask.execute(true);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (currentUiState == UiState.GAME)
            unregisterListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (currentUiState == UiState.GAME)
            registerListeners();

        switchViews();
    }

    private void scoreboardBack() {
        setUiState(UiState.MAIN_MENU);
        switchViews();
    }

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

    private void checkFields() {
        // Check if a name has been filled in!
        String playerName = playerNameEditText.getText().toString();

        if (playerName.isEmpty()) {
            Log.i(TAG, "Player name not filled in!");

            Toast.makeText(this, "Please enter a name...", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.i(TAG, "Checking if name in use, name = " + playerName);

        // Check if the player name is not already used
        NameInUseTask nameInUseTask = new NameInUseTask(this, this);
        nameInUseTask.execute(playerName);
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
     * Switches the visibility off the views according to the Ui State
     */
    private void switchViews() {
        switch (currentUiState) {
            case GAME: {
                mainMenuButtonsView.setVisibility(View.GONE);
                gameButtonsView.setVisibility(View.VISIBLE);
                gameOverView.setVisibility(View.GONE);
                newGameView.setVisibility(View.GONE);
                scoreBoardView.setVisibility(View.GONE);
                break;
            }
            case MAIN_MENU: {
                mainMenuButtonsView.setVisibility(View.VISIBLE);
                gameButtonsView.setVisibility(View.GONE);
                gameOverView.setVisibility(View.GONE);
                newGameView.setVisibility(View.GONE);
                scoreBoardView.setVisibility(View.GONE);
                break;
            }
            case GAME_OVER: {
                mainMenuButtonsView.setVisibility(View.GONE);
                gameButtonsView.setVisibility(View.GONE);
                gameOverView.setVisibility(View.VISIBLE);
                newGameView.setVisibility(View.GONE);
                scoreBoardView.setVisibility(View.GONE);
                break;
            }
            case NEW_GAME: {
                mainMenuButtonsView.setVisibility(View.GONE);
                gameButtonsView.setVisibility(View.GONE);
                gameOverView.setVisibility(View.GONE);
                newGameView.setVisibility(View.VISIBLE);
                scoreBoardView.setVisibility(View.GONE);
                break;
            }
            case SCOREBOARD: {
                mainMenuButtonsView.setVisibility(View.GONE);
                gameButtonsView.setVisibility(View.GONE);
                gameOverView.setVisibility(View.GONE);
                newGameView.setVisibility(View.GONE);
                scoreBoardView.setVisibility(View.VISIBLE);
                break;
            }
        }
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
            Toast.makeText(this, "Name already in use...", Toast.LENGTH_SHORT).show();
            return;
        }

        currentScore = new Score(name, 0);

        // Start game since the name is not already in use
        startGame();
    }
}
