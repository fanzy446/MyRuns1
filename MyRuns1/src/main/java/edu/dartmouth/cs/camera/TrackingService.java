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

import java.util.Calendar;

import edu.dartmouth.cs.camera.database.ExerciseEntry;
import edu.dartmouth.cs.camera.helper.DistanceUnitHelper;

public class TrackingService extends Service {

    public static final int MSG_REGISTER_CLIENT = 1;
    public static final int MSG_UNREGISTER_CLIENT = 2;
    public static final int MSG_UPDATE_ENTRY = 3;
    public static final int MSG_SET_STRING_VALUE = 4;
    private static boolean isRunning = false;
    private final Messenger mMessenger = new Messenger(
            new IncomingMessageHandler());
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
        updateWithNewLocation(l);

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

        // getBinder()
        // Return the IBinder that this Messenger is using to communicate with
        // its associated Handler; that is, IncomingMessageHandler().

        return mMessenger.getBinder();
    }

//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        Log.d("Fanzy", "Service:onStartCommand(): Received start id " + startId + ": "
//                + intent);
//        return START_STICKY; // Run until explicitly stopped.
//    }

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

        sendMsg();
    }

    void sendMsg() {
        try {
            if (mClient != null) {
                Message msg = Message.obtain(null, MSG_UPDATE_ENTRY);
                Bundle bundle = new Bundle();
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
                    if (mClient == null) {
                        mClient = msg.replyTo;
                        mEntry.setmInputType(msg.arg1);
                        mEntry.setmActivityType(msg.arg2);
                    }
                    sendMsg();
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
