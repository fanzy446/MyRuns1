package edu.dartmouth.cs.camera;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import com.google.gson.Gson;

import edu.dartmouth.cs.camera.database.ExerciseEntryDbHelper;
import edu.dartmouth.cs.camera.gcm.GCMAsyncTask;

public class StartFragment extends Fragment {

    public static final String INPUT_TYPE = "Input Type";
    public static final String ACTIVITY_TYPE = "Activity Type";

    public StartFragment() {

    }

    public static StartFragment newInstance() {
        return new StartFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
                onSyncClicked(v);
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

    public void onSyncClicked(View v) {
        ExerciseEntryDbHelper dbHelper = new ExerciseEntryDbHelper(getActivity());
        String json = (new Gson()).toJson(dbHelper.fetchEntries());
        dbHelper.close();
        (new GCMAsyncTask(getActivity())).execute(GCMAsyncTask.UPDATE, json);
    }
}
