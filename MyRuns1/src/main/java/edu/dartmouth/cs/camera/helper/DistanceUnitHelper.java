package edu.dartmouth.cs.camera.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import edu.dartmouth.cs.camera.R;

public class DistanceUnitHelper {

    public static String distanceToString(Context context, double dis, boolean withUnit) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String unitItems = preferences.getString(context.getString(R.string.preference_key_settings_unit), "");
        NumberFormat numberFormat = new DecimalFormat("####.###");
        if (unitItems.equals("Metric")) {
            dis *= 1.6;
        }
        if (withUnit) {
            if (unitItems.equals("Metric")) {
                return numberFormat.format(dis) + "Kilometers";//String.format("%.2f Kilometers", dis);
            } else {
                return numberFormat.format(dis) + "Miles";// String.format("%.2f Miles", dis);
            }
        }
        return numberFormat.format(dis) + "";// String.format("%.2f", dis);
    }
}
