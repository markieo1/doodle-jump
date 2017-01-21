package com.anthony.marco.doodlejump.view.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.anthony.marco.doodlejump.R;
import com.anthony.marco.doodlejump.adapter.ScoresAdapter;
import com.anthony.marco.doodlejump.listener.UiListener;
import com.anthony.marco.doodlelibrary.model.Score;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Created by marco on 20-1-2017.
 */

public class ScoreboardFragment extends Fragment {
    public static final String TAG = "ScoreboardFragment";

    private UiListener mUiListener;

    private ArrayList<Score> scores;
    private ScoresAdapter scoresAdapter;

    public ScoreboardFragment() {
        super();
        scores = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.score_board_fragment, container, false);

        ListView scoreboardListView = (ListView) fragmentView.findViewById(R.id.score_board_listview);
        scoresAdapter = new ScoresAdapter(inflater, scores);
        scoreboardListView.setAdapter(scoresAdapter);

        Button backScoreBoardButton = (Button) fragmentView.findViewById(R.id.score_board_back_button);
        backScoreBoardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mUiListener != null)
                    mUiListener.onMainMenu();
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

    public void addScores(Score... newScores) {
        addScores(Arrays.asList(newScores));
    }

    public void addScores(Collection<Score> newScores) {
        this.scores.clear();
        this.scores.addAll(newScores);

        this.scoresAdapter.notifyDataSetChanged();
    }
}
