package edu.dartmouth.cs.camera.database;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Fanzy on 5/2/16.
 */
public class ExerciseEntryAsyncTask extends AsyncTask<ExerciseEntry, Void, Long> {

    Context context = null;

    public ExerciseEntryAsyncTask(Context c) {
        context = c;
    }

    // Params
    @Override
    protected Long doInBackground(ExerciseEntry... entries) {
        ExerciseEntryDbHelper dbHelper = new ExerciseEntryDbHelper(context);
        long id = dbHelper.insertEntry(entries[0]);
        dbHelper.close();
        return id;
    }

    // Result
    @Override
    protected void onPostExecute(Long id) {
        Toast.makeText(context, "Entry #" + id + " saved", Toast.LENGTH_SHORT).show();
        Log.d("Fanzy", "onPostExecute");
    }

}
