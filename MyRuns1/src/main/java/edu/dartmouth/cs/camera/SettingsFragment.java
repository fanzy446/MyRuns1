package edu.dartmouth.cs.camera;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.support.v4.view.ViewPager;
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
        // catch listPreference modification, change the distance unit
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        ListPreference unit = (ListPreference) preferenceScreen.findPreference(getString(R.string.preference_key_settings_unit));
        unit.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                ViewPager viewPager = (ViewPager) getActivity().findViewById(R.id.viewpager);
                MainActivity.ViewPagerAdapter adapter = (MainActivity.ViewPagerAdapter) viewPager.getAdapter();
                HistoryFragment fragment = (HistoryFragment) adapter.getItem(1);
                fragment.reloadData();
                return true;
            }
        });
    }
}
