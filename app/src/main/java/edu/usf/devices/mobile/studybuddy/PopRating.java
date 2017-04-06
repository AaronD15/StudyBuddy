package edu.usf.devices.mobile.studybuddy;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.widget.TextView;

/**
 * Created by Mayra on 4/5/2017.
 */

public class PopRating extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.popup_rating);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(height*.6));
        Bundle extras = getIntent().getExtras();
        if(extras!=null) {
            String value = extras.getString("my text");
            TextView editText = (TextView)findViewById(R.id.tittle);
            editText.setText(value);
            String valuea = extras.getString("my address");
            TextView editTex = (TextView)findViewById(R.id.address);
            editTex.setText(valuea);
        }
    }
}
