package edu.dartmouth.cs.camera.helper;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateHelper {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String fromCalendarToString(Calendar calendar) {
        Log.d("DateHelper", "Transform datetime: " + calendar.getTime());
        return DATE_FORMAT.format(calendar.getTime());
    }

    public static Calendar fromStringToCalendar(String date) {
        Calendar t = Calendar.getInstance();
        try {
            t.setTime(DATE_FORMAT.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return t;
    }
}