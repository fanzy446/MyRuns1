package edu.dartmouth.cs.camera;

import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TwoLineListItem;

import java.util.ArrayList;

import edu.dartmouth.cs.camera.database.ExerciseEntry;
import edu.dartmouth.cs.camera.database.ExerciseEntryDbHelper;

/**
 * Created by oubai on 4/7/16.
 */
public class HistoryFragment extends ListFragment implements LoaderManager.LoaderCallbacks<ArrayList<ExerciseEntry>> {

    // list of array strings which will serve as list items
    ArrayList<ExerciseEntry> listItems = new ArrayList<>();

    // define a string adapter which will handle the data of the listview
    ArrayAdapter<ExerciseEntry> arrayAdapter;

    ListView listView;

    public HistoryFragment() {

    }

    public static HistoryFragment newInstance() {
        return new HistoryFragment();
    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {
        ExerciseEntryDbHelper dbHelper = new ExerciseEntryDbHelper(getActivity());
        listItems.addAll(dbHelper.fetchEntries());

        arrayAdapter = new ArrayAdapter<ExerciseEntry>(getActivity(), android.R.layout.simple_list_item_2, listItems) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TwoLineListItem row;
                if (convertView == null) {
                    LayoutInflater inflater = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    row = (TwoLineListItem) inflater.inflate(android.R.layout.simple_list_item_2, null);
                } else {
                    row = (TwoLineListItem) convertView;
                }
                ExerciseEntry data = listItems.get(position);
                row.getText1().setText(data.getmComment());
                row.getText2().setText(data.getmComment());
                return row;
            }
        };
        getListView().setScrollbarFadingEnabled(false);
        setListAdapter(arrayAdapter);

        // define the listener
        AdapterView.OnItemClickListener listViewListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), DisplayEntryActivity.class);
                startActivity(intent);
            }
        };

        getListView().setOnItemClickListener(listViewListener);
    }

    // method which will handle dynamic insertion
    public void addItems(View v) {
//        arrayAdapter.add("");
    }

    @Override
    public Loader<ArrayList<ExerciseEntry>> onCreateLoader(int i, Bundle bundle) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<ExerciseEntry>> loader, ArrayList<ExerciseEntry> exerciseEntries) {

    }

    @Override
    public void onLoaderReset(Loader<ArrayList<ExerciseEntry>> loader) {

    }

    public class DataLoader extends AsyncTaskLoader<ArrayList<ExerciseEntry>> {

        public DataLoader(Context context) {
            super(context);

        }

        @Override
        protected void onStartLoading() {
            forceLoad();
        }

        @Override
        public ArrayList<ExerciseEntry> loadInBackground() {
            return null;
        }
    }
}
