package edu.dartmouth.cs.camera;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import edu.dartmouth.cs.camera.database.ExerciseEntry;
import edu.dartmouth.cs.camera.helper.DistanceUnitHelper;

public class TrackingService extends Service {

    public static final int MSG_REGISTER_CLIENT = 1;
    public static final int MSG_UNREGISTER_CLIENT = 2;
    public static final int MSG_UPDATE_ENTRY = 3;
    public static final int MSG_SET_STRING_VALUE = 4;
    private final Messenger mMessenger = new Messenger(
            new IncomingMessageHandler());
    private NotificationManager mNotificationManager;
    private ExerciseEntry mEntry = null;
    private LocationManager locationManager = null;
    private Messenger mClient = null;

    private long mLatestTime = 0;
    private LatLng mLatestPosition = null;

    private final LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            updateWithNewLocation(location);
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status,
                                    Bundle extras) {
        }
    };

    public TrackingService() {
    }

    public static LatLng fromLocationToLatLng(Location location) {
        return new LatLng(location.getLatitude(), location.getLongitude());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        showNotification();
        //initialize mEntry
        mEntry = new ExerciseEntry();
        mEntry.setmDistance(0.0);
        mEntry.setmDuration(0);
        mEntry.setmCalorie(0);

        mLatestTime = System.currentTimeMillis();

        //start location update
        String svcName = Context.LOCATION_SERVICE;
        locationManager = (LocationManager) getSystemService(svcName);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);
        criteria.setCostAllowed(true);
        String provider = locationManager.getBestProvider(criteria, true);

        Location l = locationManager.getLastKnownLocation(provider);
        locationManager.requestLocationUpdates(provider, 1000, 5,
                locationListener);

        //start activity update
        updateWithNewLocation(l);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mNotificationManager.cancelAll();
        locationManager.removeUpdates(locationListener);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d("Fanzy", "Service:onBind() - return mMessenger.getBinder()");

        // getBinder()
        // Return the IBinder that this Messenger is using to communicate with
        // its associated Handler; that is, IncomingMessageHandler().

        return mMessenger.getBinder();
    }

    /**
     * Display a notification in the notification bar.
     */
    private void showNotification() {

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MapDisplayActivity.class), 0);
        Notification notification = new Notification.Builder(this)
                .setContentTitle(this.getString(R.string.app_name))
                .setContentText(
                        getResources().getString(R.string.ui_start_gps_notification_text))
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentIntent(contentIntent).build();
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notification.flags = notification.flags
                | Notification.FLAG_ONGOING_EVENT;
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        mNotificationManager.notify(0, notification);

    }

    private void updateWithNewLocation(Location location) {

        Log.d("Fanzy", "Service: updateWithNewLocation");

        LatLng curPos = fromLocationToLatLng(location);
        long curTime = System.currentTimeMillis();
        double curSpeed = 0;
        Bundle bundle = new Bundle();

        if (mLatestPosition != null) {
            if (curTime - mLatestTime < 1000) {
                return;
            }
            double curDis = DistanceUnitHelper.distance(curPos, mLatestPosition);
            curSpeed = curDis * 3600 * 1000 / (double) (curTime - mLatestTime);
            mEntry.setmDistance(mEntry.getmDistance() + curDis);
            mEntry.setmDuration(mEntry.getmDuration() + (int) (curTime - mLatestTime) / 1000);
            mEntry.setmAvgSpeed(mEntry.getmDistance() * 3600 / mEntry.getmDuration());
        }
        mEntry.appendLocationList(curPos);
        mEntry.setmClimb(location.getAltitude());
        mEntry.setmCalorie(mEntry.getmDuration());

        mLatestPosition = curPos;
        mLatestTime = curTime;
        try {
            if (mClient != null) {
                Message msg = Message.obtain(null, MSG_UPDATE_ENTRY);
                bundle.putDouble(MapDisplayActivity.CURSPEED, curSpeed);
                bundle.putString(MapDisplayActivity.ENTRY, (new Gson()).toJson(mEntry));
                msg.setData(bundle);
                mClient.send(msg);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    /**
     * Handle incoming messages from MainActivity
     */
    private class IncomingMessageHandler extends Handler { // Handler of
        // incoming messages
        // from clients.
        @Override
        public void handleMessage(Message msg) {
            Log.d("Fanzy", "Service:handleMessage: " + msg.what);
            switch (msg.what) {
                case MSG_REGISTER_CLIENT:
                    Log.d("Fanzy", "Service: RX MSG_REGISTER_CLIENT");
                    mClient = msg.replyTo;
                    break;
                case MSG_UNREGISTER_CLIENT:
                    Log.d("Fanzy", "Service: RX MSG_UNREGISTER_CLIENT");
                    mClient = null;
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }
}
