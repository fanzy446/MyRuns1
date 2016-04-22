package edu.dartmouth.cs.camera.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateHelper {
    private static final SimpleDateFormat DATA_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat STRING_FORMAT = new SimpleDateFormat("HH:mm:ss MMM dd yyyy");

    /**
     * convert calender to the string stored in SQLite, used when accessing database
     *
     * @param calendar calendar
     * @return string in SQLite
     */
    public static String calendarToData(Calendar calendar) {
        return DATA_FORMAT.format(calendar.getTime());
    }

    /**
     * convert the string stored in SQLite to calender, used when accessing database
     *
     * @param date string in SQLite
     * @return calendar
     */
    public static Calendar dataToCalendar(String date) {
        Calendar t = Calendar.getInstance();
        try {
            t.setTime(DATA_FORMAT.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * convert calendar to the string shown in UI
     *
     * @param calendar
     * @return string in UI
     */
    public static String calendarToString(Calendar calendar) {
        return STRING_FORMAT.format(calendar.getTime());
    }

    /**
     * convert seconds to x mins y secs
     *
     * @param seconds seconds
     * @return x mins y secs
     */
    public static String secondsToString(int seconds) {
        if (seconds >= 60) {
            return String.format("%d mins %d secs", seconds / 60, seconds % 60);
        } else {
            return String.format("%d secs", seconds);
        }
    }
}
