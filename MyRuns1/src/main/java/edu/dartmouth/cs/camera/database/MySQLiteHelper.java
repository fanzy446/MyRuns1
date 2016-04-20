package edu.dartmouth.cs.camera.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by oubai on 4/19/16.
 */
public class MySQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_ENTRY = "exercise_entry";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_ACTIVITY_TYPE = "activity_type";
    public static final String COLUMN_DATETIME = "date_time";
    public static final String COLUMN_DURATION = "duration";
    public static final String COLUMN_DISTANCE = "distance";
    public static final String COLUMN_AVG_PACE = "avg_pace";
    public static final String COLUMN_AVG_SPEED = "avg_speed";
    public static final String COLUMN_CALORIE = "calorie";
    public static final String COLUMN_HEART_RATE = "heart_rate";
    public static final String COLUMN_COMMENT = "comment";

    private static final String DATABASE_NAME = "exercise_entry.db";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE = "create table" + TABLE_ENTRY + "(" + COLUMN_ID;

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // if the database does not exist, run onCreate method
    // if the database has been created, run onUpgrade method
    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_ENTRY);
        onCreate(db);
    }
}
