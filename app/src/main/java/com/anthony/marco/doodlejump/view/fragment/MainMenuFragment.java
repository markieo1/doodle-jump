package com.anthony.marco.doodlejump.view.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.anthony.marco.doodlejump.R;
import com.anthony.marco.doodlejump.listener.UiListener;
import com.anthony.marco.doodlejump.view.AboutUsActivity;

/**
 * Created by marco on 20-1-2017.
 */

public class MainMenuFragment extends Fragment {
    public static final String TAG = "MainMenuFragment";

    private UiListener mUiListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.main_menu_fragment, container, false);

        Button newGameButton = (Button) fragmentView.findViewById(R.id.new_game_button);
        Button aboutUsButton = (Button) fragmentView.findViewById(R.id.about_us_button);
        Button scoresButton = (Button) fragmentView.findViewById(R.id.scores_button);

        // Register all the button listeners.
        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mUiListener != null)
                    mUiListener.onStartGame();
            }
        });

        aboutUsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getBaseContext(), AboutUsActivity.class);

                // Start the intent
                startActivity(intent);
            }
        });

        scoresButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mUiListener != null)
                    mUiListener.onShowScoreboard();
            }
        });

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
}
