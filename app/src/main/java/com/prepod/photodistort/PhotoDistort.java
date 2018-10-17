package com.prepod.photodistort;

import android.app.Application;
import android.os.Handler;


/**
 * Created by Anton on 13.10.2018.
 */
public class PhotoDistort extends Application {

    private static PhotoDistort mInstanse;
    private Handler uiHandler = new Handler();

    public static PhotoDistort getInstatnse() {
        return mInstanse;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstanse = this;
    }

    public Handler getUiHandler() {
        return uiHandler;
    }
}
