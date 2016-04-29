package edu.dartmouth.cs.camera;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
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

import java.util.Calendar;

import edu.dartmouth.cs.camera.database.ExerciseEntry;
import edu.dartmouth.cs.camera.database.ExerciseEntryDbHelper;
import edu.dartmouth.cs.camera.helper.DistanceUnitHelper;

public class MapDisplayActivity extends FragmentActivity implements OnMapReadyCallback, ServiceConnection {

    public static final String ENTRY = "maps_entry";
    public static final String CURSPEED = "maps_curspeed";
    private final Messenger mMessenger = new Messenger(
            new IncomingMessageHandler());
    private GoogleMap mMap;
    private Messenger mServiceMessenger = null;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Fanzy", "UI: onCreate");
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


        Bundle bundle = getIntent().getExtras();
        if (!bundle.containsKey(HistoryFragment.ENTRY)) {
            mEntry = new ExerciseEntry();
            mEntry.setmInputType(bundle.getInt(StartFragment.INPUT_TYPE, 0));
            mEntry.setmActivityType(bundle.getInt(StartFragment.ACTIVITY_TYPE, 0));
            mEntry.setmDateTime(Calendar.getInstance());
            mEntry.setmAvgSpeed(.0);
            mEntry.setmClimb(.0);
            mEntry.setmCalorie(0);
            mEntry.setmDistance(.0);

            bundle.getInt(StartFragment.INPUT_TYPE, 0);
            doBindService();
        } else {
            mEntry = (new Gson()).fromJson(bundle.getString(HistoryFragment.ENTRY), ExerciseEntry.class);
            findViewById(R.id.btn_start_gps_save).setVisibility(View.INVISIBLE);
            findViewById(R.id.btn_start_gps_cancel).setVisibility(View.INVISIBLE);
        }

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("Fanzy", "UI: onMapReady");
        mMap = googleMap;
        mRoute = mMap.addPolyline(new PolylineOptions().width(5).color(Color.RED));
        updateUI();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        doUnbindService();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        if (getIntent().getExtras().containsKey(HistoryFragment.ENTRY)) {
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

    public void onSaveClicked(View v) {
        Log.d("Fanzy", (new Gson()).toJson(mEntry));
        ExerciseEntryDbHelper dbHelper = new ExerciseEntryDbHelper(this);
        dbHelper.insertEntry(mEntry);
        dbHelper.close();
        finish();
    }

    public void onCancelClicked(View v) {
        finish();
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        Log.d("Fanzy", "UI:onServiceConnected()");
        mServiceMessenger = new Messenger(service);
        try {
            Message msg = Message.obtain(null, TrackingService.MSG_REGISTER_CLIENT);
            msg.replyTo = mMessenger;
            Log.d("Fanzy", "UI: TX MSG_REGISTER_CLIENT");
            mServiceMessenger.send(msg);
        } catch (RemoteException e) {
            // In this case the service has crashed before we could even do
            // anything with it
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        mServiceMessenger = null;
    }

    private void doBindService() {
        Log.d("Fanzy", "UI:doBindService()");
        bindService(new Intent(this, TrackingService.class), this,
                Context.BIND_AUTO_CREATE);
    }

    private void doUnbindService() {
        Log.d("Fanzy", "UI:doUnBindService()");
        if (mServiceMessenger != null) {
            try {
                Message msg = Message.obtain(null,
                        TrackingService.MSG_UNREGISTER_CLIENT);
                Log.d("Fanzy", "C: TX MSG_UNREGISTER_CLIENT");
                msg.replyTo = mMessenger;
                mServiceMessenger.send(msg);
                unbindService(this);
            } catch (RemoteException e) {
                // There is nothing special we need to do if the service has
                // crashed.
            }
        }
    }

    private void updateUI() {
        Log.d("Fanzy", "Update UI...");
        mTypeText.setText(String.format("Type: %s", getResources().getStringArray(R.array.spinner_input_type)[mEntry.getmInputType()]));
        mAvgspeedText.setText(String.format("Avg speed: %s", DistanceUnitHelper.speedToString(this, mEntry.getmAvgSpeed(), true)));
        mCurspeedText.setText(String.format("Cur speed: %s", DistanceUnitHelper.speedToString(this, mSpeed, true)));
        mClimbText.setText(String.format("Climb: %s", DistanceUnitHelper.distanceToString(this, mEntry.getmDistance(), true)));
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

//            if (whereAmI != null)
//                whereAmI.remove();
//
//            whereAmI = mMap.addMarker(new MarkerOptions().position(latlng).icon(BitmapDescriptorFactory.defaultMarker(
//                    BitmapDescriptorFactory.HUE_GREEN)).title("Here I Am."));
    }

    private class IncomingMessageHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Log.d("Fanzy", "UI:IncomingHandler:handleMessage");
            switch (msg.what) {
                case TrackingService.MSG_UPDATE_ENTRY:
                    Bundle bundle = msg.getData();
                    mSpeed = bundle.getDouble(CURSPEED);
                    ExerciseEntry entryTmp = (new Gson()).fromJson(bundle.getString(ENTRY), ExerciseEntry.class);
                    mEntry.setmAvgSpeed(entryTmp.getmAvgSpeed());
                    mEntry.setmClimb(entryTmp.getmClimb());
                    mEntry.setmCalorie(entryTmp.getmCalorie());
                    mEntry.setmDistance(entryTmp.getmDistance());
                    mEntry.setmLocationList(entryTmp.getmLocationList());
                    mEntry.setmDuration(entryTmp.getmDuration());
                    updateUI();
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }
}
