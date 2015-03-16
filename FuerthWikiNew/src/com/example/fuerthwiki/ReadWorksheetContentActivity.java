package com.example.fuerthwiki;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class ReadWorksheetContentActivity extends ListActivity{

	//private static final String TAG = ReadWorksheetContentActivity.class.getSimpleName();
	private ArrayAdapter<String> excelItemArrayAdapter;
	String[] item_array=null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		try {
			super.onCreate(savedInstanceState);   
			setContentView(R.layout.activity_getphotoname);
			//hier ListView mit allen Elementen anzeigen
			excelItemArrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);//new CustomAdapter(this,R.id.textview_b_item);
	        setListAdapter(excelItemArrayAdapter);
	        
	        Intent i = getIntent();
	        String excelFile = i.getStringExtra(Constants.EXCELFILE);
	        String worksheet = i.getStringExtra(Constants.WORKSHEET);	        
	        excelItemArrayAdapter.addAll(getWorksheetContent(excelFile,worksheet));
	        excelItemArrayAdapter.notifyDataSetChanged();
			ListView lv = getListView();
			lv.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					Intent intent = new Intent();
					intent.putExtra(Constants.PHOTONAME, excelItemArrayAdapter.getItem(arg2));
					setResult(RESULT_OK, intent);
					finish();
				}
			});
	      
			//Element w�hlen lassen dann(aber noch ab�ndern):
//			
			
		} catch (Exception e) {
			finish();
		}

	}

	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		
	}
	public List<String> getWorksheetContent(String excelFile, String worksheet) throws IOException  {
		List<String> resultSet = new ArrayList<String>();
	    File inputWorkbook = new File(excelFile);
	    FileInputStream FSInputWorkbook = new FileInputStream(excelFile);
	    if(inputWorkbook.exists()){
	        try {
	        	HSSFWorkbook w = new HSSFWorkbook(FSInputWorkbook);
	            // Get the first sheet
	            HSSFSheet sheet = w.getSheet(worksheet);
	            // Loop over column and lines
	            for (int j = 0; j < sheet.getPhysicalNumberOfRows(); j++) {
	                HSSFRow row = sheet.getRow(j);	                
                    for (int i = 0; i < row.getPhysicalNumberOfCells(); i++) {
                        HSSFCell cell = row.getCell(i);
                        if(!cell.getStringCellValue().equals(""))
                        	resultSet.add(cell.getStringCellValue());
                    }	                
	                continue;
	            }
	            w.close();
	        }catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    else
	    {
	        resultSet.add("File not found..!");
	    }
	    if(resultSet.size()==0){
	        resultSet.add("Data not found..!");
	        resultSet.add("Evtl konnte das File nicht gelesen werden");
	        resultSet.add("Dokument als .xls speichern");
	    }
	    return resultSet;
    }

}
