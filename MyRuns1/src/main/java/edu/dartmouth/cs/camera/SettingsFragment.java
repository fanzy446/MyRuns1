package edu.dartmouth.cs.camera;

import android.os.Bundle;
import android.preference.PreferenceFragment;
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
    }
}
