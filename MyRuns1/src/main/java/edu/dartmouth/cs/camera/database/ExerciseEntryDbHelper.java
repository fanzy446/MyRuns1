package edu.dartmouth.cs.camera.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import edu.dartmouth.cs.camera.helper.DateHelper;

/**
 * Created by Fanzy on 4/20/16.
 */
public class ExerciseEntryDbHelper extends SQLiteOpenHelper {

    public static final String TABLE_ENTRY = "ENTRIES";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_INPUT_TYPE = "input_type";
    public static final String COLUMN_ACTIVITY_TYPE = "activity_type";
    public static final String COLUMN_DATETIME = "date_time";
    public static final String COLUMN_DURATION = "duration";
    public static final String COLUMN_DISTANCE = "distance";
    public static final String COLUMN_AVG_PACE = "avg_pace";
    public static final String COLUMN_AVG_SPEED = "avg_speed";
    public static final String COLUMN_CALORIE = "calories";
    public static final String COLUMN_HEART_RATE = "heart_rate";
    public static final String COLUMN_COMMENT = "comment";
    public static final String COLUMN_PRIVACY = "privacy";
    public static final String COLUMN_GPSDATA = "gps_data";
    private static final String DATABASE_NAME = "exercise_entry.db";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_CREATE = "CREATE TABLE IF NOT EXISTS ENTRIES (" +
            "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "input_type INTEGER NOT NULL, " +
            "activity_type INTEGER NOT NULL, " +
            "date_time DATETIME NOT NULL, " +
            "duration INTEGER NOT NULL, " +
            "distance FLOAT, " +
            "avg_pace FLOAT, " +
            "avg_speed FLOAT, " +
            "calories INTEGER, " +
            "climb FLOAT, " +
            "heart_rate INTEGER, " +
            "comment TEXT, " +
            "privacy INTEGER, " +
            "gps_data BLOB );";
    private SQLiteDatabase database;

    // Constructor
    public ExerciseEntryDbHelper(Context context) {
        // DATABASE_NAME is, of course the name of the database, which is defined as a tring constant
        // DATABASE_VERSION is the version of database, which is defined as an integer constant
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        database = getWritableDatabase();
    }

    // Create table schema if not exists
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_ENTRY);
        onCreate(db);
    }

    // Insert a item given each column value
    public long insertEntry(ExerciseEntry entry) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_INPUT_TYPE, entry.getmInputType());
        values.put(COLUMN_ACTIVITY_TYPE, entry.getmActivityType());
        values.put(COLUMN_DATETIME, DateHelper.fromCalendarToString(entry.getmDateTime()));
        values.put(COLUMN_DURATION, entry.getmDuration());
        values.put(COLUMN_DISTANCE, entry.getmDistance());
//        values.put(COLUMN_AVG_PACE, entry.getmAvgPace());
//        values.put(COLUMN_AVG_SPEED, entry.getmAvgSpeed());
        values.put(COLUMN_CALORIE, entry.getmCalorie());
        values.put(COLUMN_HEART_RATE, entry.getmHeartRate());
        values.put(COLUMN_COMMENT, entry.getmComment());
//        values.put(COLUMN_PRIVACY, entry.getmPrivacy());
//        values.put(COLUMN_GPSDATA, entry.getmLocationList());
        return database.insert(TABLE_ENTRY, null,
                values);
    }

    // Remove an entry by giving its index
    public int removeEntry(long rowIndex) {
        return database.delete(TABLE_ENTRY, COLUMN_ID
                + " = " + rowIndex, null);
    }

    // Query a specific entry by its index.
    public ExerciseEntry fetchEntryByIndex(long rowId) {
        Cursor cursor = database.query(TABLE_ENTRY,
                null, COLUMN_ID + " = " + rowId, null,
                null, null, null);
        cursor.moveToFirst();
        ExerciseEntry entry = cursorToExerciseEntry(cursor);
        cursor.close();

        return entry;
    }

    // Query the entire table, return all rows
    public List<ExerciseEntry> fetchEntries() {
        List<ExerciseEntry> entries = new ArrayList<>();

        Cursor cursor = database.query(TABLE_ENTRY,
                null, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            ExerciseEntry entry = cursorToExerciseEntry(cursor);
            entries.add(entry);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return entries;
    }

    private ExerciseEntry cursorToExerciseEntry(Cursor cursor) {
        ExerciseEntry comment = new ExerciseEntry();
        comment.setId(cursor.getLong(0));
        comment.setmInputType(cursor.getInt(1));
        comment.setmActivityType(cursor.getInt(2));
        comment.setmDateTime(DateHelper.fromStringToCalendar(cursor.getString(3)));
        comment.setmDuration(cursor.getInt(4));
        comment.setmDistance(cursor.getDouble(5));
//        comment.setmAvgPace(cursor.getDouble(6));
//        comment.setmAvgSpeed(cursor.getDouble(7));
        comment.setmCalorie(cursor.getInt(8));
        comment.setmClimb(cursor.getDouble(9));
        comment.setmHeartRate(cursor.getInt(10));
        comment.setmComment(cursor.getString(11));
//        comment.setmPrivacy(cursor.getInt(12));
//        comment.setmLocationList();

        return comment;
    }
}
