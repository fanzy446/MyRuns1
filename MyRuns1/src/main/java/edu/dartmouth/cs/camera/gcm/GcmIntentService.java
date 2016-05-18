package edu.dartmouth.cs.camera.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.logging.Level;
import java.util.logging.Logger;

import edu.dartmouth.cs.camera.HistoryFragment;
import edu.dartmouth.cs.camera.database.ExerciseEntryDbHelper;

/**
 * Created by Varun on 2/18/16.
 */
public class GcmIntentService extends IntentService {

    public static final String TYPE = "type";
    public static final String CODE = "code";
    public static final String DATA = "data";

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (extras != null && !extras.isEmpty()) {  // has effect of unparcelling Bundle
            // Since we're not using two way messaging, this is all we really to check for
            if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                Logger.getLogger("GCM_RECEIVED").log(Level.INFO, extras.toString());
                String msg = extras.getString("message");
                JsonParser parser = new JsonParser();
                JsonObject obj = parser.parse(msg).getAsJsonObject();
                if (obj.get(TYPE).getAsString().equals(GCMAsyncTask.UPDATE)) {
                    if (obj.get(CODE).getAsInt() == 0) {
                        showToast("Synced successfully");
                    } else {
                        showToast("Synced failed");
                    }
                } else if (obj.get(TYPE).getAsString().equals(GCMAsyncTask.DELETE)) {
                    if (obj.get(CODE).getAsInt() == 0) {
                        showToast("Deleted successfully");
                        int id = obj.get(DATA).getAsJsonObject().get(GCMAsyncTask.ID).getAsInt();
                        ExerciseEntryDbHelper dbHelper = new ExerciseEntryDbHelper(getApplicationContext());
                        dbHelper.removeEntry(id);
                        dbHelper.close();
                        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(HistoryFragment.ENTRY_CHANGE));
                    } else {
                        showToast("Deleted failed");
                    }
                }

            }
        }
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    protected void showToast(final String message) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        });
    }
}