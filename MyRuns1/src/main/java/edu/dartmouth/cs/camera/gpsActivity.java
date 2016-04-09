package edu.dartmouth.cs.camera;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

/**
 * Created by oubai on 4/8/16.
 */
public class gpsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
    }

    public void onButtonClicked(View v) {
        finish();
    }
}
