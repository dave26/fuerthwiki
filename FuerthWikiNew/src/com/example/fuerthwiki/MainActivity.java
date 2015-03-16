package com.example.fuerthwiki;

import java.io.File;
import java.net.URISyntaxException;

import com.example.fuerthwiki.Database.DatabaseControl;
import com.example.fuerthwiki.Database.DatabaseEntry;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {

	private static final String TAG = MainActivity.class.getSimpleName();
	private static String excelFile = "";
	TextView textview_infotext_excelfile;
	TextView textview_Resultfolder;
	Button button_take_photo;
	DatabaseControl database;

	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textview_infotext_excelfile = (TextView)findViewById(R.id.textView_Infotext_ExcelFile);
        textview_Resultfolder = (TextView)findViewById(R.id.textView_Infotext_ResultFolder);
        textview_Resultfolder.setText("Zielordner der Bilder: "+Environment.getExternalStoragePublicDirectory(
		            Environment.DIRECTORY_PICTURES)+File.separator+"FuerthWiki");
        button_take_photo=(Button)findViewById(R.id.button_Take_Photo);
        database = new DatabaseControl(this);
        
        button_take_photo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!excelFile.equals("")&&!excelFile.equals(null))
				{
					Intent iname = new Intent(MainActivity.this,GetPhotoNameActivity.class);
					iname.putExtra(Constants.EXCELFILE, excelFile);
					startActivityForResult(iname,Constants.CODE_FOR_PHOTONAME);
				}
				else
					Toast.makeText(MainActivity.this, "Bitte ein Excelfile w�hlen", Toast.LENGTH_SHORT).show();
				
			}
		});
        
	      //get last state from database
	    excelFile = getExcelfileFromDatabase();
	    if(!excelFile.equals(""))
	    	textview_infotext_excelfile.setText("gew�hltes File:" +excelFile);
    }
    
	
//    @Override
//    public void onStart(){
//    	super.onStart();
//    	//get last state from database
//        excelFile = getExcelfileFromDatabase();
//        if(!excelFile.equals(""))
//        	textview_infotext_excelfile.setText("gew�hltes File:" +excelFile);
//    }
//    @Override
//	public void onStop() {
//		super.onStop();
//	}

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    	switch (requestCode) {
        case Constants.FILE_SELECT_CODE:
	        if (resultCode == RESULT_OK) {
	            // Get the Uri of the selected file 
	            Uri uri = data.getData();
	            Log.d(TAG, "File Uri: " + uri.toString());
	            try{
	            	String path = getPath(this, uri);//getPath(this, uri);
	            	excelFile=path;
	            	textview_infotext_excelfile.setText("gew�hltes File:" +excelFile);
	            	writeExcelfileInDatabase(excelFile);
		            Log.d(TAG, "File Path: " + path);
	            }catch(Exception e){
	            	Log.e(TAG,e.getMessage());
	            }
	        }
	        break;
        
        case Constants.CODE_FOR_PHOTONAME:
        	if(resultCode==RESULT_OK)
        	{
	        	String photoName = data.getStringExtra(Constants.PHOTONAME);
	        	Intent i = new Intent(MainActivity.this, CameraActivity.class);
				i.putExtra(Constants.PHOTONAME, photoName);
				startActivityForResult(i, Constants.CODE_FOR_RESULTFILE);
        	}
        	else
        		Toast.makeText(MainActivity.this, "Es wurde kein Fotoname gew�hlt", Toast.LENGTH_SHORT).show();
        	break;
        	
        case Constants.CODE_FOR_RESULTFILE:
        	Log.d(TAG, "activity result; parsing uri: " + data.getStringExtra("uri"));
        	galleryAddPic(data.getStringExtra("uri"));
			//Bitmap snapshot = BitmapFactory.decodeFile(data.getStringExtra("uri"));
			Toast.makeText(MainActivity.this, "Foto wurde erfolgreich aufgenommen", Toast.LENGTH_SHORT).show();
			break;
    }
    }//onActivityResult

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId()){
        case R.id.action_exit: 
        	 AlertDialog.Builder builder = new AlertDialog.Builder(this);		    
		     builder.setTitle("Beenden");
		     builder.setMessage("Wollen Sie FuerthWiki wirklich beenden?");		    
		     builder.setPositiveButton("JA", new DialogInterface.OnClickListener() {	    
			     @Override
			     public void onClick(DialogInterface dialog, int which) {	
				     System.exit(0);				    
				     dialog.dismiss();
			     }	    
		     });
		    
		     builder.setNegativeButton("NEIN", new DialogInterface.OnClickListener() {	    
			     @Override
			     public void onClick(DialogInterface dialog, int which) {
				     dialog.dismiss();
			     }
			    
		     });
		     AlertDialog alert = builder.create();
		     alert.show();
		     return true;
        
        case R.id.action_excelfile:

    	    Intent intent = new Intent(Intent.ACTION_GET_CONTENT); 
    	    intent.setType("*/*"); 
    	    intent.addCategory(Intent.CATEGORY_OPENABLE);

    	    try {
    	        startActivityForResult(
    	                Intent.createChooser(intent, "Select a File to Upload"),
    	                Constants.FILE_SELECT_CODE);
    	    } catch (android.content.ActivityNotFoundException ex) {
    	        // Potentially direct the user to the Market with a Dialog
    	        Toast.makeText(this, "Please install a File Manager.", 
    	                Toast.LENGTH_SHORT).show();
    	    }       	
        	return true;

        case R.id.action_info:
        	String title = getString(R.string.app_name) + " build 1.1";
        	Builder builder_INFO = new Builder(this);
        	builder_INFO.setTitle(title);
        	builder_INFO.setView(View.inflate(this, R.layout.info, null));
        	builder_INFO.setIcon(R.drawable.ic_launcher);
        	builder_INFO.setPositiveButton("OK", null);
        	Dialog info =builder_INFO.create();
        	info.show();
        	return true;
        default:
        	return super.onOptionsItemSelected(item);
        }
    }
    
    @SuppressLint("NewApi")
	public static String getPath(Context context, Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
        	Cursor cursor = null;
        	try { 
        	    String[] proj = { MediaStore.Images.Media.DATA };
        	    cursor = context.getContentResolver().query(uri,  proj, null, null, null);
        	    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        	    cursor.moveToFirst();
        	    String path = cursor.getString(column_index);
        	    if(path!=null)
        	    	return path;
        	    else{
	        	    String wholeID = DocumentsContract.getDocumentId(uri);
	    	        String id = wholeID.split(":")[1];
	    	        return "/storage/emulated/0/"+id;
        	    }
        	  }finally {
        	    if (cursor != null) {
        	      cursor.close();
        	    }
        	  }
        }
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    } 

    @SuppressLint("NewApi")
    public static String getRealPathFromURI_API19(Context context, Uri uri){
        String filePath = "";
        String wholeID = DocumentsContract.getDocumentId(uri);

         // Split at colon, use second item in the array
         String id = wholeID.split(":")[1];

         String[] column = { MediaStore.Images.Media.DATA };     

         // where id is equal to             
         String sel = MediaStore.Images.Media._ID + "=?";

         Cursor cursor = context.getContentResolver().query(uri, 
                                   column, sel, new String[]{ id }, null);

         int columnIndex = cursor.getColumnIndex(column[0]);

         if (cursor.moveToFirst()) {
             filePath = cursor.getString(columnIndex);
         }   
         cursor.close();
         return filePath;
    }

    @SuppressLint("NewApi")
    public static String getRealPathFromURI_API11to18(Context context, Uri contentUri) {
          String[] proj = { MediaStore.Images.Media.DATA };
          String result = null;

          CursorLoader cursorLoader = new CursorLoader(
                  context, 
            contentUri, proj, null, null, null);        
          Cursor cursor = cursorLoader.loadInBackground();

          if(cursor != null){
           int column_index = 
             cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
           cursor.moveToFirst();
           result = cursor.getString(column_index);
          }
          return result;  
    }
    public String getFileName(String Path){
    	String[] pathArray = Path.split("/");
    	return pathArray[pathArray.length-1];
    }
    private void galleryAddPic(String mCurrentPhotoPath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }
    private void writeExcelfileInDatabase(String file){
    	try{
    		database.open();
    		database.createEntry(file);
    		}catch (Exception ex)
    		{
    			Toast.makeText(MainActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
    			Log.d(TAG, ex.toString());
    		}
    		finally
    		{
    			database.close();
    		} 
    }
    private String getExcelfileFromDatabase(){
    	String file = "";
    	try{
    		database.open();
    		DatabaseEntry e = database.getLastEntry();
    		if(!e.equals(null))
    			file = e.getExcelfile();
    	}catch(Exception ex){
    		Toast.makeText(MainActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
			Log.d(TAG, ex.toString());
    	}
    	finally{
    		database.close();
    	}
    	return file;
    }
    
}
