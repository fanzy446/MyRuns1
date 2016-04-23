package edu.dartmouth.cs.camera;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;

import edu.dartmouth.cs.camera.view.SlidingTabLayout;

public class MainActivity extends Activity {

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
}

