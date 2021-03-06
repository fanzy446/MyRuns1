package edu.dartmouth.cs.camera;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;

import edu.dartmouth.cs.camera.database.ExerciseEntry;
import edu.dartmouth.cs.camera.database.ExerciseEntryAsyncTask;
import edu.dartmouth.cs.camera.database.ExerciseEntryDbHelper;
import edu.dartmouth.cs.camera.helper.DistanceUnitHelper;

public class MapDisplayActivity extends FragmentActivity implements OnMapReadyCallback, ServiceConnection {

    public static final String INPUT_TYPE = "maps_inputtype";
    public static final String ACTIVITY_TYPE = "maps_acttype";
    private GoogleMap mMap;

    private TrackingService mService = null;
    private LocalBroadcastManager bm = null;

    private ExerciseEntry mEntry = null;
    private TextView mTypeText = null;
    private TextView mAvgspeedText = null;
    private TextView mCurspeedText = null;
    private TextView mClimbText = null;
    private TextView mCalorieText = null;
    private TextView mDistanceText = null;

    private Polyline mRoute = null;
    private Marker mStart = null;
    private Marker mEnd = null;

    private double mSpeed = 0;
    private boolean mBounded = false;

    // the broadcast receiver upon the update of mEntry in the service
    private BroadcastReceiver onLocationReceived = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent i) {
            if (mBounded) {
                mEntry = mService.getmEntry();
                mSpeed = mService.getCurSpeed();
                updateUI();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_display);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mTypeText = (TextView) findViewById(R.id.tv_start_gps_type);
        mAvgspeedText = (TextView) findViewById(R.id.tv_start_gps_avgspeed);
        mCurspeedText = (TextView) findViewById(R.id.tv_start_gps_curspeed);
        mClimbText = (TextView) findViewById(R.id.tv_start_gps_climb);
        mCalorieText = (TextView) findViewById(R.id.tv_start_gps_calorie);
        mDistanceText = (TextView) findViewById(R.id.tv_start_gps_distance);

        bm = LocalBroadcastManager.getInstance(this);
        bm.registerReceiver(onLocationReceived, new IntentFilter(TrackingService.LOCATION_UPDATE));

        Bundle bundle = getIntent().getExtras();
        if (!bundle.containsKey(HistoryFragment.ENTRY)) {
            mEntry = new ExerciseEntry();
            mEntry.setmInputType(bundle.getInt(StartFragment.INPUT_TYPE, 0));
            mEntry.setmActivityType(bundle.getInt(StartFragment.ACTIVITY_TYPE, 0));
            mEntry.init();
            doBindService();
        } else {
            mEntry = (new Gson()).fromJson(bundle.getString(HistoryFragment.ENTRY), ExerciseEntry.class);
            findViewById(R.id.btn_start_gps_save).setVisibility(View.INVISIBLE);
            findViewById(R.id.btn_start_gps_cancel).setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mRoute = mMap.addPolyline(new PolylineOptions().width(5).color(Color.RED));
        if (getIntent().getExtras().containsKey(HistoryFragment.ENTRY)) {
            updateUI();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        doUnbindService();
        bm.unregisterReceiver(onLocationReceived);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey(HistoryFragment.ENTRY)) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.display_entry, menu);
        }
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

    // recall function when save button is pressed
    public void onSaveClicked(View v) {
        ExerciseEntryAsyncTask task = new ExerciseEntryAsyncTask(this);
        task.execute(mEntry);
        finish();
    }

    // recall function when cancel button is pressed
    public void onCancelClicked(View v) {
        finish();
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        mService = ((TrackingService.TrackingBinder) service).getService();
        mBounded = true;
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        mService = null;
        mBounded = false;
    }

    /**
     * start and bind service
     */
    private void doBindService() {
        Intent intent = new Intent(this, TrackingService.class);
        intent.putExtra(INPUT_TYPE, mEntry.getmInputType());
        intent.putExtra(ACTIVITY_TYPE, mEntry.getmActivityType());
        startService(intent);
        bindService(new Intent(this, TrackingService.class), this, Context.BIND_AUTO_CREATE);
    }

    /**
     * unbind and stop service
     */
    private void doUnbindService() {
        if (mService != null) {
            unbindService(this);
            stopService(new Intent(this, TrackingService.class));
        }
    }

    /**
     * update the UI
     */
    private void updateUI() {
        if (mEntry.getmActivityType() == -1) {
            mTypeText.setText("Type: Others");
        } else {
            mTypeText.setText(String.format("Type: %s", getResources().getStringArray(R.array.spinner_activity_type)[mEntry.getmActivityType()]));
        }
        mAvgspeedText.setText(String.format("Avg speed: %s", DistanceUnitHelper.speedToString(this, mEntry.getmAvgSpeed(), true)));
        mCurspeedText.setText(String.format("Cur speed: %s", DistanceUnitHelper.speedToString(this, mSpeed, true)));
        mClimbText.setText(String.format("Climb: %s", DistanceUnitHelper.distanceToString(this, mEntry.getmClimb(), true)));
        mCalorieText.setText(String.format("Calorie: %d", mEntry.getmCalorie()));
        mDistanceText.setText(String.format("Distance: %s", DistanceUnitHelper.distanceToString(this, mEntry.getmDistance(), true)));

        // Send data as a String
        if (mEntry.getmLocationList().size() > 0) {
            //mRoute
            mRoute.setPoints(mEntry.getmLocationList());

            //marker
            LatLng latlng = mEntry.getmLocationList().get(mEntry.getmLocationList().size() - 1);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng,
                    17));
            if (mEnd != null) {
                mEnd.remove();
            }
            mEnd = mMap.addMarker(new MarkerOptions().position(latlng).icon(BitmapDescriptorFactory.defaultMarker(
                    BitmapDescriptorFactory.HUE_RED)).title("End"));
            if (mStart == null) {
                mStart = mMap.addMarker(new MarkerOptions().position(mEntry.getmLocationList().get(0)).
                        icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).title("Start"));
            }

        }
    }
}
