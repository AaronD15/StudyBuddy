package edu.usf.devices.mobile.studybuddy;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by shawnc96 on 6/21/2017.
 */
public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
