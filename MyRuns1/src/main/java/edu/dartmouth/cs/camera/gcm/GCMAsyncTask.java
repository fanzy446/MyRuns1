package edu.dartmouth.cs.camera.gcm;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import edu.dartmouth.cs.camera.backend.registration.Registration;

public class GCMAsyncTask extends AsyncTask<String, Void, String> {

    public static final String SERVER_ADDR = "http://fanzy446.appspot.com";
    public static final String ID = "id";
    public static final String ENTRIES = "Entries";
    public static final String REGISTER = "register";
    public static final String UNREGISTER = "unregister";
    public static final String UPDATE = "update";
    public static final String DELETE = "delete";
    private static final String SENDER_ID = "661501379715";

    private Registration regService = null;
    private GoogleCloudMessaging gcm;
    private Context context;

    public GCMAsyncTask(Context context) {
        this.context = context;
    }

    // Params
    @Override
    protected String doInBackground(String... args) {

        String uploadState = "";
        Map<String, String> params = new HashMap<>();
        try {
            if (args[0].equals(REGISTER)) {
                if (regService == null) {
                    Registration.Builder builder = new Registration.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                            .setRootUrl(SERVER_ADDR + "/_ah/api/");
                    regService = builder.build();
                }
                if (gcm == null) {
                    gcm = GoogleCloudMessaging.getInstance(context);
                }
                String regId = gcm.register(SENDER_ID);
                uploadState = "Device registered, registration ID = " + regId;

                // You should send the registration ID to your server over HTTP,
                // so it can use GCM/HTTP or CCS to send messages to your app.
                // The request to your server should be authenticated if your app
                // is using accounts.
                regService.register(regId).execute();

            } else if (args[0].equals(UNREGISTER)) {
                if (gcm != null && regService != null) {
                    String regId = gcm.register(SENDER_ID);
                    regService.unregister(regId);
                    gcm.unregister();
                }
            } else if (args[0].equals(UPDATE)) {
                params.put(ENTRIES, args[1]);
                ServerUtilities.post(SERVER_ADDR + "/update.do", params);
            } else if (args[0].equals(DELETE)) {
                params.put(ID, args[1]);
                ServerUtilities.post(SERVER_ADDR + "/delete.do", params);
            }
        } catch (IOException e) {
            uploadState = args[0] + " failed: " + e.getCause();
            Log.e(GCMAsyncTask.class.getName(), e + "");
        }
        return uploadState;
    }

    // Result
    @Override
    protected void onPostExecute(String msg) {
        if (!msg.equals("")) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        }
    }

}
