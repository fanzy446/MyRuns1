package edu.dartmouth.cs.camera;

import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import edu.dartmouth.cs.camera.database.ExerciseEntry;
import edu.dartmouth.cs.camera.database.ExerciseEntryDbHelper;
import edu.dartmouth.cs.camera.helper.DateHelper;
import edu.dartmouth.cs.camera.helper.DistanceUnitHelper;

public class HistoryFragment extends ListFragment implements LoaderManager.LoaderCallbacks<List<ExerciseEntry>> {


    public static final String ENTRY_CHANGE = "entry_change";
    public static final String ENTRY = "history_entry";
    // list of array strings which will serve as list items
    ArrayList<ExerciseEntry> listItems = new ArrayList<>();

    // define a string adapter which will handle the data of the listview
    ArrayAdapter<ExerciseEntry> arrayAdapter;

    private BroadcastReceiver onEntryUpdate = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent i) {
            reloadData();
        }
    };

    public HistoryFragment() {

    }

    public static HistoryFragment newInstance() {
        return new HistoryFragment();
    }


    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(onEntryUpdate, new IntentFilter(ENTRY_CHANGE));

        arrayAdapter = new ArrayAdapter<ExerciseEntry>(getActivity(), android.R.layout.simple_list_item_2, listItems) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View row;
                if (convertView == null) {
                    LayoutInflater inflater = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    row = inflater.inflate(android.R.layout.simple_list_item_2, null);
                } else {
                    row = convertView;
                }
                ExerciseEntry data = listItems.get(position);

                String str1 = String.format("%s: %s: %s", getResources().getStringArray(R.array.spinner_input_type)[data.getmInputType()],
                        data.getmActivityType() == -1 ? "Unknown" : getResources().getStringArray(R.array.spinner_activity_type)[data.getmActivityType()],
                        DateHelper.calendarToString(data.getmDateTime()));

                String str2 = String.format("%s, %s", DistanceUnitHelper.distanceToString(getContext(), data.getmDistance(), true), DateHelper.secondsToString(data.getmDuration()));


                ((TextView) row.findViewById(android.R.id.text1)).setText(str1);
                ((TextView) row.findViewById(android.R.id.text1)).setTypeface(null, Typeface.BOLD);
                ((TextView) row.findViewById(android.R.id.text2)).setText(str2);

                return row;
            }
        };
        getListView().setScrollbarFadingEnabled(false);
        setListAdapter(arrayAdapter);

        // define the listener
        AdapterView.OnItemClickListener listViewListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ExerciseEntry entry = listItems.get(position);
                Bundle bundle = new Bundle();
                bundle.putString(ENTRY, (new Gson()).toJson(entry));
                if (entry.getmInputType() == 0) {
                    Intent intent = new Intent(getActivity(), DisplayEntryActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), MapDisplayActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }

            }
        };

        getListView().setOnItemClickListener(listViewListener);
        getLoaderManager().initLoader(0, null, this);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(onEntryUpdate);
    }

    @Override
    public Loader<List<ExerciseEntry>> onCreateLoader(int i, Bundle bundle) {
        return new DataLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<ExerciseEntry>> loader, List<ExerciseEntry> exerciseEntries) {
        arrayAdapter.clear();
        arrayAdapter.addAll(exerciseEntries);
    }

    @Override
    public void onLoaderReset(Loader<List<ExerciseEntry>> loader) {
        arrayAdapter.clear();
    }

    // reload the data shown in HistoryFragment
    public void reloadData() {
        getLoaderManager().getLoader(0).onContentChanged();
    }

    // create DataLoader with AsyncTaskLoader
    public static class DataLoader extends AsyncTaskLoader<List<ExerciseEntry>> {
        Context context;

        public DataLoader(Context context) {
            super(context);
            this.context = context;
        }

        @Override
        protected void onStartLoading() {
            forceLoad();
        }

        @Override
        public List<ExerciseEntry> loadInBackground() {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ExerciseEntryDbHelper dbHelper = new ExerciseEntryDbHelper(context);
            List<ExerciseEntry> result = dbHelper.fetchEntries();
            dbHelper.close();
            return result;
        }
    }
}
