package edu.dartmouth.cs.camera;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.dartmouth.cs.camera.database.ExerciseEntry;
import edu.dartmouth.cs.camera.database.ExerciseEntryDbHelper;

public class StartFragment extends Fragment {

    public static String SERVER_ADDR = "http://10.0.2.2:8080";

    public static final String INPUT_TYPE = "Input Type";
    public static final String ACTIVITY_TYPE = "Activity Type";

    private Button syncButton;
    private ExerciseEntryDbHelper dbHelper;

    public StartFragment() {

    }

    public static StartFragment newInstance() {
        return new StartFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        syncButton = (Button) getActivity().findViewById(R.id.start_sync_button);
        syncButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AsyncTask<Void, Void, String>() {

                    @Override
                    // Get history and upload it to the server.
                    protected String doInBackground(Void... arg0) {


                        // Upload the history of all entries using upload().
                        dbHelper = new ExerciseEntryDbHelper(getActivity().getApplicationContext());

                        List<ExerciseEntry> list = dbHelper.fetchEntries();
                        JSONArray jsonArray = new JSONArray();
                        String uploadState="";
                        try {
                            for(ExerciseEntry exerciseEntry : list) {
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("id", exerciseEntry.getId());
                                jsonObject.put("input_type", exerciseEntry.getmInputType());
                                jsonObject.put("activity_type", exerciseEntry.getmActivityType());
                                jsonObject.put("time", exerciseEntry.getmDateTime());
                                jsonObject.put("duration", exerciseEntry.getmDuration());
                                jsonObject.put("distance", exerciseEntry.getmDistance());
                                jsonObject.put("avg_speed", exerciseEntry.getmAvgSpeed());
                                jsonObject.put("calorie", exerciseEntry.getmCalorie());
                                jsonObject.put("climb", exerciseEntry.getmClimb());
                                jsonObject.put("heart_rate", exerciseEntry.getmHeartRate());
                                jsonObject.put("comment", exerciseEntry.getmComment());
                                jsonArray.put(jsonObject);
                            }
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("entry", jsonArray.toString());

                            ServerUtilities.post(SERVER_ADDR+"/update.do", params);
                        } catch (IOException e1) {
                            uploadState = "Sync failed: " + e1.getCause();
                            Log.e("TAGG", "data posting error " + e1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        return uploadState;
                    }

                    @Override
                    protected void onPostExecute(String errString) {
                        String resultString;
                        if(errString.equals("")) {
                            resultString =  " entry uploaded.";
                        } else {
                            resultString = errString;
                        }

                        Toast.makeText(getActivity().getApplicationContext(), resultString,
                                Toast.LENGTH_SHORT).show();

                    }

                }.execute();
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_start, container, false);

        view.findViewById(R.id.start_start_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStartClicked(v);
            }
        });

        view.findViewById(R.id.start_sync_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStartClicked(v);
            }
        });

        return view;
    }


    // the function of the START button in StartFragment
    public void onStartClicked(View v) {

        Spinner mSpinner = (Spinner) getActivity().findViewById(R.id.input_type);
        String mItemSelected = mSpinner.getSelectedItem().toString();

        int inputType = mSpinner.getSelectedItemPosition();
        int activityType = ((Spinner) getActivity().findViewById(R.id.activity_type)).getSelectedItemPosition();

        Intent mIntent = null;

        switch (mItemSelected) {
            case "Manual Entry":
                mIntent = new Intent(getActivity(), ListviewActivity.class);
                break;
            case "GPS":
                mIntent = new Intent(getActivity(), MapDisplayActivity.class);
                break;
            case "Automatic":
                mIntent = new Intent(getActivity(), MapDisplayActivity.class);
                break;
        }
        Bundle bundle = new Bundle();
        bundle.putInt(INPUT_TYPE, inputType);
        bundle.putInt(ACTIVITY_TYPE, activityType);
        if (mIntent != null) {
            mIntent.putExtras(bundle);
            startActivity(mIntent);
        }
    }
}
