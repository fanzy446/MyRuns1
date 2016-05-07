package edu.dartmouth.cs.camera;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

import edu.dartmouth.cs.camera.database.ExerciseEntry;
import edu.dartmouth.cs.camera.helper.FFT;

public class TrackingService extends Service implements SensorEventListener {

    public static final String LOCATION_UPDATE = "location_update";
    public static final String TYPE_CLASSIFY = "type_classify";
    public static final int ACCELEROMETER_BUFFER_CAPACITY = 2048;
    public static final int ACCELEROMETER_BLOCK_CAPACITY = 64;
    private static ArrayBlockingQueue<Double> mAccBuffer;
    private static boolean isRunning = false;
    private final IBinder mBinder = new TrackingBinder();
    private NotificationManager mNotificationManager;
    private ExerciseEntry mEntry = null;
    private LocationManager locationManager = null;
    private SensorManager sensorManager;
    private long mStartTime = 0;
    private long mLatestTime = 0;
    private double mStartClimb = 0;
    private Location mLatestPosition = null;
    private double curSpeed = 0;
    private List<Double> featureVector;
    private TypeClassificationTask mTypeClassificationTask;
    private int mStandingLabel = 0;
    private int mWalkingLabel = 0;
    private int mRunningLabel = 0;
    private int mOthersLabel = 0;
    //location listener
    private final LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            updateWithNewLocation(location);
            if (isRunning) {
                sendMsg();
            }
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


        locationManager.requestLocationUpdates(provider, 1000, 5,
                locationListener);

        //start activity update
        Location l = locationManager.getLastKnownLocation(provider);
        if (l != null) {
            updateWithNewLocation(l);
        }

        showNotification();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mEntry.setmInputType(intent.getIntExtra(MapDisplayActivity.INPUT_TYPE, 0));
        mEntry.setmActivityType(intent.getIntExtra(MapDisplayActivity.ACTIVITY_TYPE, 0));

        if (mEntry.getmInputType() == 2) {
            Log.d("Fanzy", "start sensorManager");
            // sensorManager
            sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION), SensorManager.SENSOR_DELAY_FASTEST);
            mAccBuffer = new ArrayBlockingQueue<>(ACCELEROMETER_BUFFER_CAPACITY);
            featureVector = new ArrayList<>();
            mTypeClassificationTask = new TypeClassificationTask();
            mTypeClassificationTask.execute();
        }

        isRunning = true;
        sendMsg();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRunning = false;
        mNotificationManager.cancelAll();
        locationManager.removeUpdates(locationListener);
        if (mEntry.getmInputType() == 2) {
            sensorManager.unregisterListener(this);
            mTypeClassificationTask.cancel(true);
        }
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

        mEntry.setmActivityType(findOverallLabel());
    }

    /**
     * notify UI that mEntry is updated
     */
    void sendMsg() {
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(LOCATION_UPDATE));
    }

    void sendMsg2(Intent intent) {
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
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

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            double x = event.values[0];
            double y = event.values[1];
            double z = event.values[2];
            Log.d("Fanzy", String.format("onSensorChanged: %f, %f, %f", x, y, z));

            double magnitude = Math.sqrt(x * x + y * y + z * z);

            // Insert the element into this queue if possible
            // If no space is available, use a capacity-restricted queue
            try {
                mAccBuffer.add(magnitude);
            } catch (IllegalStateException e) {
                ArrayBlockingQueue<Double> newBuffer = new ArrayBlockingQueue<>(mAccBuffer.size() * 2);
                mAccBuffer.drainTo(newBuffer);
                mAccBuffer = newBuffer;
                mAccBuffer.add(magnitude);
            }
        }
    }

    // the activity with the most labels become the overall label
    public int findOverallLabel() {
        int max = Math.max(Math.max(mStandingLabel, mWalkingLabel), Math.max(mRunningLabel, mOthersLabel));
        if (max == mRunningLabel) return 0;
        else if (max == mWalkingLabel) return 1;
        else if (max == mStandingLabel) return 2;
        else return -1;
    }

    public class TrackingBinder extends Binder {
        TrackingService getService() {
            // Return this instance of DownloadBinder so clients can call public methods
            return TrackingService.this;
        }
    }

    private class TypeClassificationTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {
            int blockSize = 0;
            FFT fft = new FFT(ACCELEROMETER_BLOCK_CAPACITY);
            double[] accBlock = new double[ACCELEROMETER_BLOCK_CAPACITY];
            double[] re = accBlock;
            double[] im = new double[ACCELEROMETER_BLOCK_CAPACITY];
            double max = Double.MIN_VALUE;

            while(true) {
                // check if the AsyncTask is cancelled or not in the while loop
                try {
                    if (isCancelled()) {
                        return null;
                    }

                    // dumping buffer
                    accBlock[blockSize++] = mAccBuffer.take().doubleValue();

                    if(blockSize == ACCELEROMETER_BLOCK_CAPACITY) {
                        blockSize = 0;

                        max = .0;
                        for(double val:accBlock) {
                            if(max < val) {
                                max = val;
                            }
                        }

                        fft.fft(re, im);

                        for(int i = 0; i< re.length; i++) {
                            double mag = Math.sqrt(re[i] * re[i] + im[i] * im[i]);
                            featureVector.add(mag);
                            im[i] = .0;
                        }

                        featureVector.add(max);

                        // current activity label
                        double label = WekaClassifier.classify(featureVector.toArray());

                        if (label == 0.0) {
                            Log.d("Fanzy", "mStandingLabel");
                            mStandingLabel++;
                        } else if (label == 1.0) {
                            Log.d("Fanzy", "mWalkingLabel");
                            mWalkingLabel++;
                        } else if (label == 2.0) {
                            Log.d("Fanzy", "mRunningLabel");
                            mRunningLabel++;
                        } else {
                            Log.d("Fanzy", "mOthersLabel");
                            mOthersLabel++;
                        }

                        featureVector.clear();

//                        Intent intent = new Intent(TYPE_CLASSIFY);
//                        intent.putExtra("classified_label", label);
//                        sendMsg2(intent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
