package com.anthony.marco.doodlejump.view;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;

import com.anthony.marco.doodlejump.R;

public class AboutUsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        ActionBar bar = getActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
        }
    }
}
