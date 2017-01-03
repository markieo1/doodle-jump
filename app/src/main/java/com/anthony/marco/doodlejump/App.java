package com.anthony.marco.doodlejump;

import android.app.Application;
import android.content.Context;

/**
 * Created by marco on 3-1-2017.
 */

public class App extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Context getContext(){
        return mContext;
    }
}
