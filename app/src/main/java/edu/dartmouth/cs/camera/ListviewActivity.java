package edu.dartmouth.cs.camera;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import edu.dartmouth.cs.camera.database.ExerciseEntry;
import edu.dartmouth.cs.camera.database.ExerciseEntryAsyncTask;

public class ListviewActivity extends Activity {

    private ExerciseEntry mEntry = new ExerciseEntry();
    private ArrayAdapter<String> mAdapter;
    private ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // show the activity_info layout, which contains the listview and two buttons
        setContentView(R.layout.activity_info);

        if (getIntent().getExtras() != null) {
            mEntry.setmInputType(getIntent().getExtras().getInt(StartFragment.INPUT_TYPE));
            mEntry.setmActivityType(getIntent().getExtras().getInt(StartFragment.ACTIVITY_TYPE));
        }
        mEntry.setmDateTime(Calendar.getInstance());

        listView = (ListView) findViewById(R.id.list_view);
        String[] mItems = new String[]{"Data", "Time", "Duration", "Distance", "Calories", "Heart Rate", "Comment"};
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mItems);
        listView.setAdapter(mAdapter);

        // listen to different items in the listview
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
                        Integer duration = mEntry.getmDuration();
                        EditDialogFragment edFragment1 = EditDialogFragment.newInstance(getString(R.string.ui_listview_duration_title),
                                null, null, InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                        edFragment1.show(getFragmentManager(), getString(R.string.ui_listview_duration_title));
                        break;
                    case 3:
                        Double distance = mEntry.getmDistance();
                        EditDialogFragment edFragment2 = EditDialogFragment.newInstance(getString(R.string.ui_listview_distance_title),
                                null, null, InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                        edFragment2.show(getFragmentManager(), getString(R.string.ui_listview_distance_title));
                        break;
                    case 4:
                        Integer calorie = mEntry.getmCalorie();
                        EditDialogFragment edFragment3 = EditDialogFragment.newInstance(getString(R.string.ui_listview_calory_title),
                                null, null, InputType.TYPE_CLASS_NUMBER);
                        edFragment3.show(getFragmentManager(), getString(R.string.ui_listview_calory_title));
                        break;
                    case 5:
                        Integer heartRate = mEntry.getmHeartRate();
                        EditDialogFragment edFragment4 = EditDialogFragment.newInstance(getString(R.string.ui_listview_heartrate_title),
                                null, null, InputType.TYPE_CLASS_NUMBER);
                        edFragment4.show(getFragmentManager(), getString(R.string.ui_listview_heartrate_title));
                        break;
                    case 6:
                        EditDialogFragment edFragment5 = EditDialogFragment.newInstance(getString(R.string.ui_listview_comment_title),
                                null, getString(R.string.ui_listview_comment_summary), InputType.TYPE_CLASS_TEXT);
                        edFragment5.show(getFragmentManager(), getString(R.string.ui_listview_comment_title));
                        break;
                }
            }
        };

        listView.setOnItemClickListener(mListener);

    }

    // show the calendar
    public void onDateClicked(View v) {

        DatePickerDialog.OnDateSetListener mDateListener = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                mEntry.getmDateTime().set(Calendar.YEAR, year);
                mEntry.getmDateTime().set(Calendar.MONTH, monthOfYear);
                mEntry.getmDateTime().set(Calendar.DAY_OF_MONTH, dayOfMonth);
            }
        };

        new DatePickerDialog(ListviewActivity.this, mDateListener,
                mEntry.getmDateTime().get(Calendar.YEAR),
                mEntry.getmDateTime().get(Calendar.MONTH),
                mEntry.getmDateTime().get(Calendar.DAY_OF_MONTH)).show();

    }

    // show the time picker
    public void onTimeClicked(View v) {
        TimePickerDialog.OnTimeSetListener mTimeListener = new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                mEntry.getmDateTime().set(Calendar.HOUR_OF_DAY, hourOfDay);
                mEntry.getmDateTime().set(Calendar.MINUTE, minute);
            }
        };

        new TimePickerDialog(ListviewActivity.this, mTimeListener, mEntry.getmDateTime().get(Calendar.HOUR_OF_DAY), mEntry.getmDateTime().get(Calendar.MINUTE), true).show();

    }

    // the recall function of the SAVE button
    public void onListviewSaveClicked(View v) {
        ExerciseEntryAsyncTask task = new ExerciseEntryAsyncTask(this);
        task.execute(mEntry);
        finish();
    }

    // the recall function of the CANCEL button
    public void onListviewCancelClicked(View v) {
        Toast.makeText(ListviewActivity.this, "Entry discarded.", Toast.LENGTH_SHORT).show();
        finish();
    }

    /**
     * The callback function to get data from EditDialogFragment
     *
     * @param title   the title of EditDialogFragment, used to differentiate the type of EditDialogFragment
     * @param content the content user inputs
     */
    public void onEditDialogFinish(String title, String content) {
        //TODO: Implement an interface containing this method
        if (title.equals(getString(R.string.ui_listview_duration_title))) {
            mEntry.setmDuration((int) (60 * Double.parseDouble(content)));
        } else if (title.equals(getString(R.string.ui_listview_distance_title))) {
            mEntry.setmDistance(Double.parseDouble(content));
        } else if (title.equals(getString(R.string.ui_listview_calory_title))) {
            mEntry.setmCalorie(Integer.parseInt(content));
        } else if (title.equals(getString(R.string.ui_listview_heartrate_title))) {
            mEntry.setmHeartRate(Integer.parseInt(content));
        } else if (title.equals(getString(R.string.ui_listview_comment_title))) {
            mEntry.setmComment(content);
        }
    }

}
