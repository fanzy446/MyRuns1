package edu.dartmouth.cs.camera.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import edu.dartmouth.cs.camera.R;

public class DistanceUnitHelper {

    /**
     * convert distance to String for display
     *
     * @param context  context
     * @param dis      distance
     * @param withUnit whether the string contains unit in the end or not
     * @return the String for display
     */
    public static String distanceToString(Context context, double dis, boolean withUnit) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String unitItems = preferences.getString(context.getString(R.string.preference_key_settings_unit), "");
        NumberFormat numberFormat = new DecimalFormat("####.##");
        if (unitItems.equals("Metric")) {
            dis *= 1.6;
        }
        if (withUnit) {
            if (unitItems.equals("Metric")) {
                return numberFormat.format(dis) + " Kilometers";//String.format("%.2f Kilometers", dis);
            } else {
                return numberFormat.format(dis) + " Miles";// String.format("%.2f Miles", dis);
            }
        }
        return numberFormat.format(dis) + "";// String.format("%.2f", dis);
    }

    /**
     * convert speed to String for display
     *
     * @param context  context
     * @param speed    speed
     * @param withUnit whether the string contains unit in the end or not
     * @return the String for display
     */
    public static String speedToString(Context context, double speed, boolean withUnit) {
        if (speed == 0) {
            return "n/a";
        }
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String unitItems = preferences.getString(context.getString(R.string.preference_key_settings_unit), "");
        NumberFormat numberFormat = new DecimalFormat("####.##");
        if (unitItems.equals("Metric")) {
            speed *= 1.6;
        }
        if (withUnit) {
            if (unitItems.equals("Metric")) {
                return numberFormat.format(speed) + " km/h";//String.format("%.2f Kilometers", dis);
            } else {
                return numberFormat.format(speed) + " m/h";// String.format("%.2f Miles", dis);
            }
        }
        return numberFormat.format(speed) + "";// String.format("%.2f", dis);
    }
}
