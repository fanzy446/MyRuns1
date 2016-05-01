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
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Messenger;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.Calendar;

import edu.dartmouth.cs.camera.database.ExerciseEntry;
import edu.dartmouth.cs.camera.helper.DistanceUnitHelper;

public class TrackingService extends Service {

    public static final String LOCATION_UPDATE = "location_update";
    public static final int MSG_REGISTER_CLIENT = 1;
    public static final int MSG_UNREGISTER_CLIENT = 2;
    public static final int MSG_UPDATE_ENTRY = 3;

    private static boolean isRunning = false;
    private final IBinder mBinder = new TrackingBinder();

    private NotificationManager mNotificationManager;
    private ExerciseEntry mEntry = null;
    private LocationManager locationManager = null;
    private Messenger mClient = null;
    private long mLatestTime = 0;
    private LatLng mLatestPosition = null;
    private double curSpeed = 0;

    private final LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            updateWithNewLocation(location);
            sendMsg();
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

    public static boolean isRunning() {
        return isRunning;
    }

    @Override
    public void onCreate() {
        Log.d("Fanzy", "TrackingService onCreate");
        super.onCreate();
        //initialize mEntry
        mEntry = new ExerciseEntry();
        mEntry.setmDateTime(Calendar.getInstance());
        mEntry.setmDistance(0.0);
        mEntry.setmDuration(0);
        mEntry.setmAvgSpeed(.0);
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
        if (l != null) {
            updateWithNewLocation(l);
            sendMsg();
        }

        showNotification();
        isRunning = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRunning = false;
        mNotificationManager.cancelAll();
        locationManager.removeUpdates(locationListener);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d("Fanzy", "Service:onBind() - return mMessenger.getBinder()");
        return mBinder;
    }

    /**
     * Display a notification in the notification bar.
     */
    private void showNotification() {

        Intent intent = new Intent(this, MapDisplayActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                intent, 0);
        Notification notification = new Notification.Builder(this)
                .setContentTitle(this.getString(R.string.app_name))
                .setContentText(
                        getResources().getString(R.string.ui_start_gps_notification_text))
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentIntent(contentIntent).build();
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notification.flags = notification.flags
                | Notification.FLAG_ONGOING_EVENT;

        mNotificationManager.notify(0, notification);
    }

    private void updateWithNewLocation(Location location) {
        Log.d("Fanzy", "Service: updateWithNewLocation");

        LatLng curPos = fromLocationToLatLng(location);
        long curTime = System.currentTimeMillis();


        if (mLatestPosition != null) {
            if (curTime - mLatestTime < 1000) {
                return;
            }
            double curDis = DistanceUnitHelper.distance(curPos, mLatestPosition);
            curSpeed = curDis * 3600 * 1000 / (double) (curTime - mLatestTime);
            mEntry.setmDistance(mEntry.getmDistance() + curDis);
            mEntry.setmDuration(mEntry.getmDuration() + (int) (curTime - mLatestTime) / 1000);
            mEntry.setmAvgSpeed(mEntry.getmDistance() * 3600 / mEntry.getmDuration());
            mEntry.setmCalorie((int) (mEntry.getmDistance() / 15));
        }
        mEntry.appendLocationList(curPos);
        mEntry.setmClimb(location.getAltitude());

        mLatestPosition = curPos;
        mLatestTime = curTime;
    }

    void sendMsg() {
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(LOCATION_UPDATE));
    }

    public ExerciseEntry getmEntry() {
        return mEntry;
    }

    public void initializeEntry(int inputType, int activityType) {
        mEntry.setmInputType(inputType);
        mEntry.setmActivityType(activityType);
    }

    public double getCurSpeed() {
        return curSpeed;
    }

    public void setCurSpeed(double curSpeed) {
        this.curSpeed = curSpeed;
    }

    public class TrackingBinder extends Binder {
        TrackingService getService() {
            // Return this instance of DownloadBinder so clients can call public methods
            return TrackingService.this;
        }
    }
}
