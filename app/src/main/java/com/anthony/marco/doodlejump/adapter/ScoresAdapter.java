package com.anthony.marco.doodlejump.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.anthony.marco.doodlejump.R;
import com.anthony.marco.doodlejump.model.Score;

import java.util.ArrayList;

/**
 * Created by marco on 11-1-2017.
 */

public class ScoresAdapter extends BaseAdapter {
    private LayoutInflater mLayoutInflater;
    private ArrayList<Score> mScores;

    public ScoresAdapter(LayoutInflater layoutInflater, ArrayList<Score> scores) {
        mLayoutInflater = layoutInflater;
        this.mScores = scores;
    }

    @Override
    public int getCount() {
        return mScores.size();
    }

    @Override
    public Object getItem(int i) {
        return mScores.get(i);
    }

    @Override
    public long getItemId(int i) {
        return mScores.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;

        if (view == null) {
            view = mLayoutInflater.inflate(R.layout.score_list_item, null);

            viewHolder = new ViewHolder();
            viewHolder.nameTextView = (TextView) view.findViewById(R.id.score_list_name_textview);
            viewHolder.scoreTextView = (TextView) view.findViewById(R.id.score_list_score_textview);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        Score score = (Score) this.getItem(i);
        viewHolder.nameTextView.setText(score.getName());
        viewHolder.scoreTextView.setText(String.valueOf(score.getScore()));

        return view;
    }

    private static class ViewHolder {
        TextView nameTextView;
        TextView scoreTextView;
    }
}
