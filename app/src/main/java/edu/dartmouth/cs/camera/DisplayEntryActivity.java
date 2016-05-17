package edu.dartmouth.cs.camera;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import com.google.gson.Gson;

import edu.dartmouth.cs.camera.database.ExerciseEntry;
import edu.dartmouth.cs.camera.database.ExerciseEntryDbHelper;
import edu.dartmouth.cs.camera.helper.DateHelper;
import edu.dartmouth.cs.camera.helper.DistanceUnitHelper;

public class DisplayEntryActivity extends Activity {

    private ExerciseEntry mEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_entry);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mEntry = (new Gson()).fromJson(bundle.getString(HistoryFragment.ENTRY), ExerciseEntry.class);
        }

        //Initialize contents in EditText
        ((EditText) findViewById(R.id.et_display_inputtype)).setText(getResources().getStringArray(R.array.spinner_input_type)[mEntry.getmInputType()]);
        ((EditText) findViewById(R.id.et_display_activitytype)).setText(getResources().getStringArray(R.array.spinner_activity_type)[mEntry.getmActivityType()]);
        ((EditText) findViewById(R.id.et_display_datetime)).setText(DateHelper.calendarToString(mEntry.getmDateTime()));
        ((EditText) findViewById(R.id.et_display_duration)).setText(DateHelper.secondsToString(mEntry.getmDuration()));
        ((EditText) findViewById(R.id.et_display_distance)).setText(DistanceUnitHelper.distanceToString(this, mEntry.getmDistance(), true));
        ((EditText) findViewById(R.id.et_display_calorie)).setText(String.format("%d cals", mEntry.getmCalorie()));
        ((EditText) findViewById(R.id.et_display_heartrate)).setText(String.format("%d bpm", mEntry.getmHeartRate()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.display_entry, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menuitem_display_delete:
                ExerciseEntryDbHelper dbHelper = new ExerciseEntryDbHelper(this);
                dbHelper.removeEntry(mEntry.getId());
                dbHelper.close();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
