package edu.dartmouth.cs.camera;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;

/**
 * Created by oubai on 4/7/16.
 */
public class StartFragment extends Fragment {

    private Button mStart;
    private Button mSync;

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

        ((Button) view.findViewById(R.id.start_start_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStartClicked(v);
            }
        });

        ((Button) view.findViewById(R.id.start_sync_button)).setOnClickListener(new View.OnClickListener() {
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
        Intent mIntent;

        switch (mItemSelected) {
            case "Manual Entry":
                mIntent = new Intent(getActivity(), ListviewActivity.class);
                startActivity(mIntent);
                break;
            case "GPS":
                mIntent = new Intent(getActivity(), GpsActivity.class);
                startActivity(mIntent);
                break;
            case "Automatic":
                mIntent = new Intent(getActivity(), GpsActivity.class);
                startActivity(mIntent);
                break;
        }
    }

    // the function of the SYNC button, do nothing
    public void onSyncClicked(View v) {
    }
}
