package edu.dartmouth.cs.camera;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

/**
 * Created by oubai on 4/8/16.
 */
public class GpsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
    }

    // the function of SAVE and CANCEL button in GPS and AUTOMATIC
    public void onGPSButtonClicked(View v) {
        finish();
    }
}
