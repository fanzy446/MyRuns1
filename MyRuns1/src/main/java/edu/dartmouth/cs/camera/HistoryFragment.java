package edu.dartmouth.cs.camera;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by oubai on 4/7/16.
 */
public class HistoryFragment extends Fragment {

    // list of array strings which will serve as list items
    ArrayList<String> listItems = new ArrayList<>();

    // define a string adapter which will handle the data of the listview
    ArrayAdapter<String> arrayAdapter;

    ListView listView;

    public HistoryFragment() {

    }

    public static HistoryFragment newInstance() {
        return new HistoryFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        listView = (ListView) getActivity().findViewById(R.id.listview_show_history);

        arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.fragment_history, listItems);

        // assign the adapter to ListView
        listView.setAdapter(arrayAdapter);

        // define the listener
        AdapterView.OnItemClickListener listViewListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), DisplayEntryActivity.class);
                startActivity(intent);
            }
        };

        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    // method which will handle dynamic insertion
    public void addItems(View v) {
        arrayAdapter.add("");
    }

}
