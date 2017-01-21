package com.anthony.marco.doodlejump.view.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.anthony.marco.doodlejump.R;
import com.anthony.marco.doodlejump.listener.UiListener;
import com.anthony.marco.doodlelibrary.model.Score;

/**
 * Created by marco on 20-1-2017.
 */

public class GameOverFragment extends Fragment {
    public static final String TAG = "GameOverFragment";
    public static final String ARG_FINAL_SCORE = "FINAL_SCORE";

    private UiListener mUiListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.game_over_fragment, container, false);

        TextView finalScoreTextView = (TextView) fragmentView.findViewById(R.id.final_score_text_view);
        Button playAgainButton = (Button) fragmentView.findViewById(R.id.play_again_button);
        Button mainMenuButton = (Button) fragmentView.findViewById(R.id.main_menu_button);

        mainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mUiListener != null)
                    mUiListener.onMainMenu();
            }
        });

        playAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mUiListener != null)
                    mUiListener.onStartGame();
            }
        });

        Score finalScore = getScore();

        finalScoreTextView.setText(String.valueOf(finalScore.getScore()));

        return fragmentView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mUiListener = (UiListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + " must implement UiListener");
        }
    }

    private Score getScore() {
        return (Score) getArguments().getSerializable(ARG_FINAL_SCORE);
    }
}
