package com.anthony.marco.doodlejump.view.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anthony.marco.doodlejump.R;
import com.anthony.marco.doodlejump.listener.UiListener;

/**
 * Created by marco on 20-1-2017.
 */

public class GameOverlayFragment extends Fragment {
    public static final String TAG = "GameOverlayFragment";

    private UiListener mUiListener;

    //private TextView scoreTextView;
    private TextView timerTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.game_overlay_fragment, container, false);

        /*Button stopGameButton = (Button) fragmentView.findViewById(R.id.stop_game_button);
        stopGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mUiListener != null)
                    mUiListener.onStopGame();
            }
        })*/

        //scoreTextView = (TextView) fragmentView.findViewById(R.id.score_text_view);
        timerTextView = (TextView) fragmentView.findViewById(R.id.timer_text_view);

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

    public void setScore(int score) {
        //scoreTextView.setText(String.valueOf(score));
    }

    public void setRemainingTime(float timeLeft) {
        String timerLeftInSeconds = String.valueOf(timeLeft / 1000);
        timerTextView.setText(timerLeftInSeconds);
    }
}
