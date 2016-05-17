package edu.dartmouth.cs.camera;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.dartmouth.cs.camera.backend.registration.Registration;
import edu.dartmouth.cs.camera.view.SlidingTabLayout;

public class MainActivity extends Activity {
    public static String SERVER_ADDR = "http://fanzy446.appspot.com";

    private SlidingTabLayout mSlidingTabLayout;
    private ViewPager mViewPager;
    private ViewPagerAdapter mViewPagerAdapter;
    private ArrayList<Fragment> mFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.tab);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        if (mFragments == null) {
            mFragments = new ArrayList<>();
            mFragments.add(StartFragment.newInstance());
            mFragments.add(HistoryFragment.newInstance());
            mFragments.add(SettingsFragment.newInstance());
        }

        mViewPagerAdapter = new ViewPagerAdapter(getFragmentManager(), mFragments);
        mViewPager.setAdapter(mViewPagerAdapter);

        mSlidingTabLayout.setDistributeEvenly(true);
        mSlidingTabLayout.setViewPager(mViewPager);
        new GcmRegistrationAsyncTask(this).execute();
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        public static final int START = 0;
        public static final int HISTORY = 1;
        public static final int SETTINGS = 2;
        public static final String UI_TAB_START = "START";
        public static final String UI_TAB_HISTORY = "HISTORY";
        public static final String UI_TAB_SETTINGS = "SETTINGS";
        private ArrayList<Fragment> fragments;


        public ViewPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public CharSequence getPageTitle(int position) {
            switch (position) {
                case START:
                    return UI_TAB_START;
                case HISTORY:
                    return UI_TAB_HISTORY;
                case SETTINGS:
                    return UI_TAB_SETTINGS;
                default:
                    break;
            }
            return null;
        }
    }

    class GcmRegistrationAsyncTask extends AsyncTask<Void, Void, String> {
        private static final String SENDER_ID = "661501379715";
        private Registration regService = null;
        private GoogleCloudMessaging gcm;
        private Context context;

        public GcmRegistrationAsyncTask(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(Void... params) {
            if (regService == null) {
//                Registration.Builder builder = new Registration.Builder(AndroidHttp.newCompatibleTransport(),
//                        new AndroidJsonFactory(), null)
//                        // Need setRootUrl and setGoogleClientRequestInitializer only for local testing,
//                        // otherwise they can be skipped
//                        .setRootUrl(SERVER_ADDR+"/_ah/api/")
//                        .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
//                            @Override
//                            public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest)
//                                    throws IOException {
//                                abstractGoogleClientRequest.setDisableGZipContent(true);
//                            }
//                        });
                // end of optional local run code
                Registration.Builder builder = new Registration.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                        .setRootUrl(SERVER_ADDR + "/_ah/api/");

                regService = builder.build();
            }

            String msg = "";
            try {
                if (gcm == null) {
                    gcm = GoogleCloudMessaging.getInstance(context);
                }
                String regId = gcm.register(SENDER_ID);
                msg = "Device registered, registration ID=" + regId;

                // You should send the registration ID to your server over HTTP,
                // so it can use GCM/HTTP or CCS to send messages to your app.
                // The request to your server should be authenticated if your app
                // is using accounts.
                regService.register(regId).execute();

            } catch (IOException ex) {
                ex.printStackTrace();
                msg = "Error: " + ex.getMessage();
            }
            return msg;
        }

        @Override
        protected void onPostExecute(String msg) {
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
            Logger.getLogger("REGISTRATION").log(Level.INFO, msg);
        }
    }
}

