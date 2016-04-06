package edu.dartmouth.cs.camera;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.soundcloud.android.crop.Crop;

public class CameraControlActivity extends Activity {

	public static final int REQUEST_CODE_TAKE_FROM_CAMERA = 0;
	public static final int REQUEST_CODE_CROP_PHOTO = 2;

	private static final String IMAGE_UNSPECIFIED = "image/*";
	private static final String URI_INSTANCE_STATE_KEY = "saved_uri";

	private Uri mImageCaptureUri;
	private ImageView mImageView;
	private EditText nameText;
	private EditText emailText;
	private EditText phoneText;
	private RadioGroup genderRadio;
	private EditText classText;
	private EditText majorText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile);

		mImageView = (ImageView) findViewById(R.id.imageProfile);
		nameText = (EditText) findViewById(R.id.nameProfile);
		emailText = (EditText) findViewById(R.id.emailProfile);
		phoneText = (EditText) findViewById(R.id.phoneProfile);
		genderRadio = (RadioGroup) findViewById(R.id.genderProfile);
		classText = (EditText) findViewById(R.id.classProfile);
		majorText = (EditText) findViewById(R.id.majorProfile);

		loadInfo();
		loadSnap();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}

	public void onSaveClicked(View v) {
		saveSnap();
		saveInfo();
		Toast.makeText(getApplicationContext(), getString(R.string.ui_profile_toast_save_text), Toast.LENGTH_SHORT).show();
		finish();
	}

	public void onCancelClicked(View v) {
		finish();
	}

	public void onChangePhotoClicked(View v) {
		Intent intent;

		intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "tmp_"
				+ String.valueOf(System.currentTimeMillis()) + ".jpg"));
		intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
		intent.putExtra("return-data", true);
		try {
			startActivityForResult(intent, REQUEST_CODE_TAKE_FROM_CAMERA);
		} catch (ActivityNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) {
			deleteTmp();
			return;
		}

		switch (requestCode) {
			case REQUEST_CODE_TAKE_FROM_CAMERA:
				cropImage();
				break;

			case REQUEST_CODE_CROP_PHOTO:
				try {
					Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mImageCaptureUri);
					mImageView.setImageBitmap(bitmap);
				} catch (IOException e) {
					e.printStackTrace();
				}
				deleteTmp();
				break;
		}
	}

	//private method
	private void deleteTmp() {
		if (mImageCaptureUri != null) {
			File f = new File(mImageCaptureUri.getPath());
			if (f.exists())
				f.delete();
			mImageCaptureUri = null;
		}
	}

	private void loadInfo() {

		String mKey = getString(R.string.preference_name);
		SharedPreferences mPrefs = getSharedPreferences(mKey, MODE_PRIVATE);

		mKey = getString(R.string.preference_key_profile_name);
		String mName = mPrefs.getString(mKey, "");
		((EditText) findViewById(R.id.nameProfile)).setText(mName);

		mKey = getString(R.string.preference_key_profile_email);
		String mEmail = mPrefs.getString(mKey, "");
		((EditText) findViewById(R.id.emailProfile)).setText(mEmail);

		mKey = getString(R.string.preference_key_profile_number);
		String mNumber = mPrefs.getString(mKey, "");
		((EditText) findViewById(R.id.phoneProfile)).setText(mNumber);

		mKey = getString(R.string.preference_key_profile_class);
		String mClass = mPrefs.getString(mKey, "");
		((EditText) findViewById(R.id.classProfile)).setText(mClass);

		mKey = getString(R.string.preference_key_profile_major);
		String mMajor = mPrefs.getString(mKey, "");
		((EditText) findViewById(R.id.majorProfile)).setText(mMajor);

		mKey = getString(R.string.preference_key_profile_gender);

		int mIntValue = mPrefs.getInt(mKey, -1);
		if (mIntValue >= 0) {
			RadioButton radioBtn = (RadioButton) ((RadioGroup) findViewById(R.id.genderProfile)).getChildAt(mIntValue);
			radioBtn.setChecked(true);
		}
	}

	private void saveInfo() {
		String mKey = getString(R.string.preference_name);
		SharedPreferences mPrefs = getSharedPreferences(mKey, MODE_PRIVATE);

		SharedPreferences.Editor mEditor = mPrefs.edit();
		mEditor.clear();

		mKey = getString(R.string.preference_key_profile_name);
		String mName = ((EditText) findViewById(R.id.nameProfile)).getText().toString();
		mEditor.putString(mKey, mName);

		mKey = getString(R.string.preference_key_profile_email);
		String mEmail = ((EditText) findViewById(R.id.emailProfile)).getText().toString();
		mEditor.putString(mKey, mEmail);

		mKey = getString(R.string.preference_key_profile_number);
		String mNumber = ((EditText) findViewById(R.id.phoneProfile)).getText().toString();
		mEditor.putString(mKey, mNumber);

		mKey = getString(R.string.preference_key_profile_class);
		String mClass = ((EditText) findViewById(R.id.classProfile)).getText().toString();
		mEditor.putString(mKey, mClass);

		mKey = getString(R.string.preference_key_profile_major);
		String mMajor = ((EditText) findViewById(R.id.majorProfile)).getText().toString();
		mEditor.putString(mKey, mMajor);

		mKey = getString(R.string.preference_key_profile_gender);

		RadioGroup mRadioGroup = (RadioGroup) findViewById(R.id.genderProfile);
		int mIntValue = mRadioGroup.indexOfChild(findViewById(mRadioGroup.getCheckedRadioButtonId()));
		mEditor.putInt(mKey, mIntValue);

		mEditor.commit();
	}

	private void loadSnap() {
		try {
			FileInputStream fis = openFileInput(getString(R.string.profile_photo_file_name));
			Bitmap bmap = BitmapFactory.decodeStream(fis);
			mImageView.setImageBitmap(bmap);
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
			mImageView.setImageResource(R.drawable.default_profile);
		}
	}

	private void saveSnap() {
		try {
			mImageView.buildDrawingCache();
			Bitmap bmap = mImageView.getDrawingCache();
			FileOutputStream fos = openFileOutput(
					getString(R.string.profile_photo_file_name), MODE_PRIVATE);
			bmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
			fos.flush();
			fos.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	private void cropImage() {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(mImageCaptureUri, IMAGE_UNSPECIFIED);

		intent.putExtra("outputX", 100);
		intent.putExtra("outputY", 100);

		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("scale", true);
		intent.putExtra("return-data", true);

		startActivityForResult(intent, REQUEST_CODE_CROP_PHOTO);
	}

}