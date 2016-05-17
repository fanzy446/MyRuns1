package edu.dartmouth.cs.camera.gcm;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import edu.dartmouth.cs.camera.MainActivity;

public class GCMAsyncTask extends AsyncTask<String, Void, String> {

    public static final String ID = "id";
    public static final String ENTRIES = "Entries";
    public static final String UPDATE = "update";
    public static final String DELETE = "delete";

    private Context context = null;

    public GCMAsyncTask(Context context) {
        this.context = context;
    }

    // Params
    @Override
    protected String doInBackground(String... args) {

        String uploadState = "";
        Map<String, String> params = new HashMap<>();

        if (args[0].equals("update")) {
            params.put(ENTRIES, args[1]);
            try {
                ServerUtilities.post(MainActivity.SERVER_ADDR + "/update.do", params);
                uploadState = "Entry synced.";
            } catch (IOException e) {
                uploadState = "Entry sync failed: " + e.getCause();
                Log.e(GCMAsyncTask.class.getName(), e + "");
            }
        } else if (args[0].equals("delete")) {
            params.put(ID, args[1]);
            try {
                ServerUtilities.post(MainActivity.SERVER_ADDR + "/delete.do", params);
                uploadState = "Entry deleted.";
            } catch (IOException e) {
                uploadState = "Entry deletion failed: " + e.getCause();
                Log.e(GCMAsyncTask.class.getName(), e + "");
            }
        }
        return uploadState;
    }

    // Result
    @Override
    protected void onPostExecute(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

}
