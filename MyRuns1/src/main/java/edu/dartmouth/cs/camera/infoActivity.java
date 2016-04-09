package edu.dartmouth.cs.camera;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ListActivity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

/**
 * Created by oubai on 4/7/16.
 */
public class infoActivity extends Activity {

    Calendar mDateAndTime = Calendar.getInstance();
    private ArrayAdapter<String> mAdapter;
    private ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        listView = (ListView) findViewById(R.id.list_view);
        String[] mItems = new String[]{"Data", "Time", "Duration", "Distance", "Calories", "Heart Rate", "Comment"};
        mAdapter = new ArrayAdapter<String>(this, R.layout.listview_layout, mItems);
        listView.setAdapter(mAdapter);

        final OnItemClickListener mListener = new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch(position) {
                    case 0:
                        onDateClicked(view);
                        break;
                    case 1:
                        onTimeClicked(view);
                        break;
                    case 2:
                        EditDialogFragment edFragment1 = EditDialogFragment.newInstance(getString(R.string.ui_listview_duration_title), null);
                        edFragment1.show(getFragmentManager(), getString(R.string.ui_listview_duration_title));
                        break;
                    case 3:
                        EditDialogFragment edFragment2 = EditDialogFragment.newInstance(getString(R.string.ui_listview_distance_title), null);
                        edFragment2.show(getFragmentManager(), getString(R.string.ui_listview_distance_title));
                        break;
                    case 4:
                        EditDialogFragment edFragment3 = EditDialogFragment.newInstance(getString(R.string.ui_listview_calory_title), null);
                        edFragment3.show(getFragmentManager(), getString(R.string.ui_listview_calory_title));
                        break;
                    case 5:
                        EditDialogFragment edFragment4 = EditDialogFragment.newInstance(getString(R.string.ui_listview_heartrate_title), null);
                        edFragment4.show(getFragmentManager(), getString(R.string.ui_listview_heartrate_title));
                        break;
                    case 6:
                        EditDialogFragment edFragment5 = EditDialogFragment.newInstance(getString(R.string.ui_listview_comment_title), getString(R.string.ui_listview_comment_summary));
                        edFragment5.show(getFragmentManager(), getString(R.string.ui_listview_comment_title));
                        break;
                }
            }
        };

        listView.setOnItemClickListener(mListener);

    }

    public void onDateClicked(View v) {

        DatePickerDialog.OnDateSetListener mDateListener = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                mDateAndTime.set(Calendar.YEAR, year);
                mDateAndTime.set(Calendar.MONTH, monthOfYear);
                mDateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            }
        };

        new DatePickerDialog(infoActivity.this, mDateListener,
                mDateAndTime.get(Calendar.YEAR),
                mDateAndTime.get(Calendar.MONTH),
                mDateAndTime.get(Calendar.DAY_OF_MONTH)).show();

    }

    public void onTimeClicked(View v) {
        TimePickerDialog.OnTimeSetListener mTimeListener = new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                mDateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                mDateAndTime.set(Calendar.MINUTE, minute);
            }
        };

        new TimePickerDialog(infoActivity.this, mTimeListener, mDateAndTime.get(Calendar.HOUR_OF_DAY), mDateAndTime.get(Calendar.MINUTE), true).show();

    }

    public void onItemButtonClicked(View v) {
        //Intent mIntent = new Intent(infoActivity.this, MainActivity.class);
        //startActivity(mIntent);
        finish();
    }
}
