package edu.usf.devices.mobile.studybuddy;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;

/**
 * Created by Mayra on 4/7/2017.
 */

public class Feedback extends Activity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedbackwindow);
        DisplayMetrics dmm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dmm);

        int width = dmm.widthPixels;
        int height = dmm.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(height*.6));
    }
}
