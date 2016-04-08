package edu.dartmouth.cs.camera;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

public class SettingsFragment extends Fragment {

    private CheckBox mPrivacyCheckBox;

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

        mPrivacyCheckBox = (CheckBox) result.findViewById(R.id.cbSettingsPrivacy);

        result.findViewById(R.id.llSettingsProfile).setOnClickListener(new ItemOnClickListener());
        result.findViewById(R.id.llSettingsPrivacy).setOnClickListener(new ItemOnClickListener());
        result.findViewById(R.id.llSettingsUnit).setOnClickListener(new ItemOnClickListener());
        result.findViewById(R.id.llSettingsComment).setOnClickListener(new ItemOnClickListener());
        result.findViewById(R.id.llSettingsPage).setOnClickListener(new ItemOnClickListener());

        return result;
    }

    public static class UnitDialogFragment extends DialogFragment {

        private static final String ARG_CHOICE = "choice";

        public static UnitDialogFragment newInstance(int choice) {
            UnitDialogFragment frag = new UnitDialogFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_CHOICE, choice);
            frag.setArguments(args);
            return frag;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            int choice = getArguments().getInt(ARG_CHOICE);

            AlertDialog.Builder unitDialog = new AlertDialog.Builder(getActivity());
            unitDialog.setTitle("Unit Preference");
            unitDialog.setSingleChoiceItems(R.array.ui_settings_unit_items, choice, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //TODO: save the choice here
                }
            });
            unitDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,
                                    int whichButton) {
                    dialog.cancel();
                }
            });
            return unitDialog.create();
        }
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
                    // TODO: change the temporary value here
                    mPrivacyCheckBox.setChecked(!mPrivacyCheckBox.isChecked());
                    break;

                case R.id.llSettingsUnit:
                    UnitDialogFragment udFragment = UnitDialogFragment.newInstance(0);
                    udFragment.show(getFragmentManager(), getString(R.string.ui_settings_unit_title));
                    break;

                case R.id.llSettingsComment:
                    EditDialogFragment edFragment = EditDialogFragment.newInstance(getString(R.string.ui_settings_comment_title), null);
                    edFragment.show(getFragmentManager(), getString(R.string.ui_settings_comment_title));
                    break;

                case R.id.llSettingsPage:
                    break;
            }
        }
    }
}
