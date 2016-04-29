package edu.dartmouth.cs.camera.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.android.gms.maps.model.LatLng;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import edu.dartmouth.cs.camera.R;

public class DistanceUnitHelper {

    /**
     * convert distance to String in UI
     *
     * @param context  context
     * @param dis      distance
     * @param withUnit whether the string contains unit in the end or not
     * @return the String in UI
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

    public static double distance(LatLng p1, LatLng p2) {
        final double R = 3959;
        double dlon = p2.longitude - p1.longitude;
        double dlat = p2.latitude - p1.latitude;
        double a = Math.pow((Math.sin(dlat / 2)), 2) + Math.cos(p1.latitude) * Math.cos(p2.latitude) * Math.pow((Math.sin(dlon / 2)), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = R * c;
        return d;
    }
}
