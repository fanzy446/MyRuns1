package edu.dartmouth.cs.camera;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
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
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class CameraControlActivity extends Activity {

	public static final int REQUEST_CODE_TAKE_FROM_CAMERA = 0;
	public static final int REQUEST_CODE_TAKE_FROM_GALLERY = 1;
	public static final int REQUEST_CODE_CROP_PHOTO = 2;

	private static final String IMAGE_UNSPECIFIED = "image/*";
	private static final String URI_INSTANCE_STATE_KEY = "saved_uri";

	private Uri mImageCaptureUri;
	private ImageView mImageView;
	private EditText mNameText;
	private EditText mEmailText;
	private EditText mPhoneText;
	private RadioGroup mGenderRadio;
	private EditText mClassText;
	private EditText mMajorText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile);

		mImageView = (ImageView) findViewById(R.id.imageProfile);
		mNameText = (EditText) findViewById(R.id.nameProfile);
		mEmailText = (EditText) findViewById(R.id.emailProfile);
		mPhoneText = (EditText) findViewById(R.id.phoneProfile);
		mGenderRadio = (RadioGroup) findViewById(R.id.genderProfile);
		mClassText = (EditText) findViewById(R.id.classProfile);
		mMajorText = (EditText) findViewById(R.id.majorProfile);

		mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), getString(R.string.profile_photo_file_name_temp)));

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
		showPictureDialog();
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

			case REQUEST_CODE_TAKE_FROM_GALLERY:
				Uri picUri = data.getData();
				String[] filePathColumn = {MediaStore.Images.Media.DATA};
				Cursor cursor = getContentResolver().query(picUri,
						filePathColumn, null, null, null);
				cursor.moveToFirst();
				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				String path = cursor.getString(columnIndex);
				cursor.close();
				try {
					FileInputStream fis = new FileInputStream(new File(path));
					FileOutputStream fos = new FileOutputStream(new File(mImageCaptureUri.getPath()));
					byte[] buffer = new byte[256];
					while (fis.read(buffer) != -1) {
						fos.write(buffer);
					}
					fis.close();
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
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
			if (f.exists()) {
				f.delete();
			}
		}
	}

	private void loadInfo() {

		String mKey = getString(R.string.preference_name);
		SharedPreferences mPrefs = getSharedPreferences(mKey, MODE_PRIVATE);

		mKey = getString(R.string.preference_key_profile_name);
		String mName = mPrefs.getString(mKey, "");
		mNameText.setText(mName);

		mKey = getString(R.string.preference_key_profile_email);
		String mEmail = mPrefs.getString(mKey, "");
		mEmailText.setText(mEmail);

		mKey = getString(R.string.preference_key_profile_number);
		String mNumber = mPrefs.getString(mKey, "");
		mPhoneText.setText(mNumber);

		mKey = getString(R.string.preference_key_profile_class);
		String mClass = mPrefs.getString(mKey, "");
		mClassText.setText(mClass);

		mKey = getString(R.string.preference_key_profile_major);
		String mMajor = mPrefs.getString(mKey, "");
		mMajorText.setText(mMajor);

		mKey = getString(R.string.preference_key_profile_gender);
		int mIntValue = mPrefs.getInt(mKey, -1);
		mGenderRadio.check(mIntValue);
	}

	private void saveInfo() {
		String mKey = getString(R.string.preference_name);
		SharedPreferences mPrefs = getSharedPreferences(mKey, MODE_PRIVATE);

		SharedPreferences.Editor mEditor = mPrefs.edit();
		mEditor.clear();

		mKey = getString(R.string.preference_key_profile_name);
		String mName = mNameText.getText().toString();
		mEditor.putString(mKey, mName);

		mKey = getString(R.string.preference_key_profile_email);
		String mEmail = mEmailText.getText().toString();
		mEditor.putString(mKey, mEmail);

		mKey = getString(R.string.preference_key_profile_number);
		String mNumber = mPhoneText.getText().toString();
		mEditor.putString(mKey, mNumber);

		mKey = getString(R.string.preference_key_profile_class);
		String mClass = mClassText.getText().toString();
		mEditor.putString(mKey, mClass);

		mKey = getString(R.string.preference_key_profile_major);
		String mMajor = mMajorText.getText().toString();
		mEditor.putString(mKey, mMajor);

		mKey = getString(R.string.preference_key_profile_gender);
		mEditor.putInt(mKey, mGenderRadio.getCheckedRadioButtonId());

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

	private void showPictureDialog() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
		alertDialog.setTitle(R.string.ui_profile_photo_picker_title);
		alertDialog.setItems(R.array.ui_settings_profile_picture, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {
				Intent intent;
				switch (i) {
					case REQUEST_CODE_TAKE_FROM_CAMERA:
						intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
						intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
						try {
							startActivityForResult(intent, REQUEST_CODE_TAKE_FROM_CAMERA);
						} catch (ActivityNotFoundException e) {
							e.printStackTrace();
						}
						break;

					case REQUEST_CODE_TAKE_FROM_GALLERY:
						intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
						try {
							startActivityForResult(intent, REQUEST_CODE_TAKE_FROM_GALLERY);
						} catch (ActivityNotFoundException e) {
							e.printStackTrace();
						}
						break;
				}

			}
		});
		alertDialog.create().show();
	}

}