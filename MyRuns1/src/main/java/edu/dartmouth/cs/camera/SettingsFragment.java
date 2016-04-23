package edu.dartmouth.cs.camera;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.widget.CheckBox;

public class SettingsFragment extends PreferenceFragment {

    private CheckBox mPrivacyCheckBox;
    private boolean mPrivacy;
    private String mUnit;
    private String mComment;

    public SettingsFragment() {
        // Required empty public constructor
    }

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.fragment_settings);
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
                Log.d("Fanzy", "onSharedPreferenceChanged: " + s);
                if (s.equals(getString(R.string.preference_key_settings_unit))) {
                    //TODO: refresh the list in the history fragment
                }
            }
        });
    }


}
