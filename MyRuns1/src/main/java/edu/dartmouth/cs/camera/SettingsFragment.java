package edu.dartmouth.cs.camera;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SettingsFragment extends Fragment {

    public SettingsFragment() {
        // Required empty public constructor
    }

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View result = inflater.inflate(R.layout.fragment_settings, container, false);
        result.findViewById(R.id.llSettingsProfile).setOnClickListener(new ItemOnClickListener());
        result.findViewById(R.id.llSettingsPrivacy).setOnClickListener(new ItemOnClickListener());
        result.findViewById(R.id.llSettingsUnit).setOnClickListener(new ItemOnClickListener());
        result.findViewById(R.id.llSettingsComment).setOnClickListener(new ItemOnClickListener());
        result.findViewById(R.id.llSettingsPage).setOnClickListener(new ItemOnClickListener());
        return result;
    }

    class ItemOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.llSettingsProfile:
                    Intent intent = new Intent(getActivity(), CameraControlActivity.class);
                    startActivity(intent);
                    break;

                case R.id.llSettingsPrivacy:
                    break;

                case R.id.llSettingsUnit:
                    break;

                case R.id.llSettingsComment:
                    break;

                case R.id.llSettingsPage:
                    break;
            }
        }
    }
}
