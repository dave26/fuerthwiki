package com.example.fuerthwiki;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

public class CameraActivity extends Activity{
	private static final String LOG_TAG = CameraActivity.class.getSimpleName();
	private static String photoName = "";
	
	
	private boolean done = true;
	File sdDir;

	protected static final String PHOTO_TAKEN = "photo_taken";

	@Override
	public void onCreate(Bundle savedInstanceState) {

		try {
			super.onCreate(savedInstanceState);   
			Intent i = getIntent();
			photoName=i.getStringExtra(Constants.PHOTONAME);
			File root = new File(Constants.FUERTHWIKI_FOLDER);
			root.mkdirs();
			sdDir = new File(root, photoName+".jpg");
			Log.d(LOG_TAG, "Creating image storage file: " + sdDir.getPath());
			startCameraActivity();
		} catch (Exception e) {
			finish();
		}

	}

	protected void startCameraActivity() {
		Uri outputFileUri = Uri.fromFile(sdDir);
		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
		startActivityForResult(intent, 0);
	}
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Intent intent = new Intent();
		intent.putExtra("uri", sdDir.getPath());
		setResult(Constants.CODE_FOR_RESULTFILE, intent);
		finish();
	}

//	@Override
//	public void onStart() 
//	{
//		Intent i = getIntent();
//		excelFile=i.getStringExtra(Constants.EXCELFILELOCATION);
//	}
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		if (savedInstanceState.getBoolean(CameraActivity.PHOTO_TAKEN)) {
			done = true;
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putBoolean(CameraActivity.PHOTO_TAKEN,  done);
	}



}
