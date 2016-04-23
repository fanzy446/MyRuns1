package edu.dartmouth.cs.camera.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import edu.dartmouth.cs.camera.R;

public class DistanceUnitHelper {

    public static String distanceToString(Context context, double dis, boolean withUnit) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String unitItems = preferences.getString(context.getString(R.string.preference_key_settings_unit), "");
        if (unitItems.equals("Metric")) {
            dis *= 1.6;
        }
        if (withUnit) {
            if (unitItems.equals("Metric")) {
                return String.format("%.2f Kilometers", dis);
            } else {
                return String.format("%.2f Miles", dis);
            }
        }
        return String.format("%.2f", dis);
    }
}
