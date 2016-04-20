package edu.dartmouth.cs.camera;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by oubai on 4/20/16.
 */
public class EntryDataSource {

    // database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = {MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_ACTIVITY_TYPE, MySQLiteHelper.COLUMN_DATETIME,
            MySQLiteHelper.COLUMN_DURATION, MySQLiteHelper.COLUMN_DISTANCE,
            MySQLiteHelper.COLUMN_AVG_PACE, MySQLiteHelper.COLUMN_AVG_SPEED,
            MySQLiteHelper.COLUMN_CALORIE, MySQLiteHelper.COLUMN_HEART_RATE,
            MySQLiteHelper.COLUMN_COMMENT };

    public EntryDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }
}
