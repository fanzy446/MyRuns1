package edu.dartmouth.cs.camera.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import edu.dartmouth.cs.camera.ExerciseEntry;

/**
 * Created by oubai on 4/20/16.
 */
public class ExerciseEntryDataSource {

    // database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = {MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_ACTIVITY_TYPE, MySQLiteHelper.COLUMN_DATETIME,
            MySQLiteHelper.COLUMN_DURATION, MySQLiteHelper.COLUMN_DISTANCE,
            MySQLiteHelper.COLUMN_AVG_PACE, MySQLiteHelper.COLUMN_AVG_SPEED,
            MySQLiteHelper.COLUMN_CALORIE, MySQLiteHelper.COLUMN_HEART_RATE,
            MySQLiteHelper.COLUMN_COMMENT };

    public ExerciseEntryDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }
/*
    public ExerciseEntry createEntry(String activityType, String dateTime, long duration, long distance, long avgPace, long avgSpeed, long calorie, long heartRate, String comment) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_ACTIVITY_TYPE, activityType);
        values.put(MySQLiteHelper.COLUMN_DATETIME, dateTime);
        values.put(MySQLiteHelper.COLUMN_DURATION, duration);
        values.put(MySQLiteHelper.COLUMN_DISTANCE, distance);
        values.put(MySQLiteHelper.COLUMN_AVG_PACE, avgPace);
        values.put(MySQLiteHelper.COLUMN_AVG_SPEED, avgSpeed);
        values.put(MySQLiteHelper.COLUMN_CALORIE, calorie);
        values.put(MySQLiteHelper.COLUMN_HEART_RATE, heartRate);
        values.put(MySQLiteHelper.COLUMN_COMMENT, comment);
        long insertId = database.insert(MySQLiteHelper.TABLE_ENTRY, null, values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_ENTRY, allColumns, MySQLiteHelper.COLUMN_ID + "=" + insertId, null, null, null, null);
        cursor.moveToNext();
        ExerciseEntry newExerciseEntry = cursorToComment(cursor);

        cursor.close();
        return newExerciseEntry;
    }
    */
}
