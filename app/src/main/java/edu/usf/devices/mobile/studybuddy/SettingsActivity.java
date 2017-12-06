package edu.usf.devices.mobile.studybuddy;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by shawnc96 on 6/21/2017.
 */
public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
}
