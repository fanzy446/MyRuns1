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
import android.support.v4.content.LocalBroadcastManager;

import com.google.android.gms.maps.model.LatLng;

import edu.dartmouth.cs.camera.database.ExerciseEntry;

public class TrackingService extends Service {

    public static final String LOCATION_UPDATE = "location_update";

    private static boolean isRunning = false;
    private final IBinder mBinder = new TrackingBinder();

    private NotificationManager mNotificationManager;
    private ExerciseEntry mEntry = null;
    private LocationManager locationManager = null;
    private long mStartTime = 0;
    private long mLatestTime = 0;
    private double mStartClimb = 0;
    private Location mLatestPosition = null;
    private double curSpeed = 0;

    //location listener
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

    /**
     * transform Location to LatLng
     *
     * @param location location
     * @return LatLng
     */
    public static LatLng fromLocationToLatLng(Location location) {
        return new LatLng(location.getLatitude(), location.getLongitude());
    }

    // Is the service running?
    public static boolean isRunning() {
        return isRunning;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //initialize mEntry
        mEntry = new ExerciseEntry();
        mEntry.init();

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

    /**
     * Update mEntry when new location is available
     *
     * @param location new location
     */
    private void updateWithNewLocation(Location location) {
        long curTime = System.currentTimeMillis();

        if (mLatestPosition != null) {
            if (curTime - mLatestTime < 1000) {
                return;
            }
            double curDis = location.distanceTo(mLatestPosition) / 1.6 / 1000;
            curSpeed = curDis * 3600 * 1000 / (double) (curTime - mLatestTime);
            mEntry.setmDistance(mEntry.getmDistance() + curDis);
            mEntry.setmDuration((int) (curTime - mStartTime) / 1000);
            mEntry.setmAvgSpeed(mEntry.getmDistance() * 3600 / mEntry.getmDuration());
            mEntry.setmCalorie((int) (mEntry.getmDistance() * 1000 / 15));
            mEntry.setmClimb(location.getAltitude() / 1000 / 1.6 - mStartClimb);
        } else {
            mStartTime = curTime;
            mStartClimb = location.getAltitude() / 1000 / 1.6;
        }
        mEntry.appendLocationList(fromLocationToLatLng(location));

        mLatestPosition = location;
        mLatestTime = curTime;
    }

    // stop service when kill the app
    @Override
    public void onTaskRemoved(Intent intent) {
        stopSelf();
    }

    /**
     * notify UI that mEntry is updated
     */
    void sendMsg() {
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(LOCATION_UPDATE));
    }

    public ExerciseEntry getmEntry() {
        return mEntry;
    }

    /**
     * initialize the entry with inputType and activityType
     *
     * @param inputType    inputType
     * @param activityType inputType
     */
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
