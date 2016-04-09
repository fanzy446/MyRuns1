package edu.dartmouth.cs.camera;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * The custom dialog fragment with an EditText view
 */
public class EditDialogFragment extends DialogFragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_TITLE = "title";
    private static final String ARG_HINT = "hint";

    private String mTitle;
    private String mHint;


    public EditDialogFragment() {
        // Required empty public constructor
    }

    public static EditDialogFragment newInstance(String title, String hint) {
        EditDialogFragment fragment = new EditDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_HINT, hint);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //Initialize the arguments
            mTitle = getArguments().getString(ARG_TITLE);
            mHint = getArguments().getString(ARG_HINT);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder editDialog = new AlertDialog.Builder(getActivity());
        editDialog.setTitle(mTitle);

        final EditText input = new EditText(getActivity());
        input.setHint(mHint);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        editDialog.setView(input);

        editDialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String text = input.getText().toString();
                        // TODO: save the result
                    }
                });

        editDialog.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        return editDialog.create();
    }
}
