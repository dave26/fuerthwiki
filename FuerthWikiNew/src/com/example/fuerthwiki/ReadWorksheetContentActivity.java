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
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class ReadWorksheetContentActivity extends ListActivity{

	//private static final String TAG = ReadWorksheetContentActivity.class.getSimpleName();
	//private ArrayAdapter<String> excelItemArrayAdapter;
	private ItemAdapter excelItemArrayAdapter;
	ArrayList<Item> item_array=null;

	private ArrayList<Item> m_parts = new ArrayList<Item>();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		try {
			super.onCreate(savedInstanceState);   
			setContentView(R.layout.activity_getphotoname);
			//hier ListView mit allen Elementen anzeigen
			Intent i = getIntent();
	        String excelFile = i.getStringExtra(Constants.EXCELFILE);
	        String worksheet = i.getStringExtra(Constants.WORKSHEET);
			item_array=getWorksheetContent(excelFile,worksheet);
			excelItemArrayAdapter = new ItemAdapter(this, R.layout.list_item, item_array);
	        setListAdapter(excelItemArrayAdapter);
	        
	       
	    	
	        //excelItemArrayAdapter.addAll(item_array);
	        //excelItemArrayAdapter.notifyDataSetChanged();
			ListView lv = getListView();
			lv.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					Intent intent = new Intent();
					String itemName = excelItemArrayAdapter.getItem(arg2).getName();
					if(!excelItemArrayAdapter.getItem(arg2).getCount().equals("0"))
						itemName+="("+excelItemArrayAdapter.getItem(arg2).getCount()+")";
					intent.putExtra(Constants.PHOTONAME, itemName);
					setResult(RESULT_OK, intent);
					finish();
				}
			});
	
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
	private ArrayList<Item> getWorksheetContent(String excelFile, String worksheet) throws IOException  {
		ArrayList<Item> resultSet = new ArrayList<Item>();
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
                        String item = cell.getStringCellValue().replace(" ", "");
                        if(!item.equals(""))
                    	{
                        	Item tempitem = new Item(item,getItemIndex(item));
                        	resultSet.add(tempitem);
                    	}                        	
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
	    	Item tempitem = new Item("File not found..!",null);
        	resultSet.add(tempitem);
	        //resultSet.add("File not found..!");
	    }
	    if(resultSet.isEmpty()){
	    	Item tempitem = new Item("Data not found..!",null);
        	resultSet.add(tempitem);
        	tempitem = new Item("Evtl konnte das File nicht gelesen werden",null);
        	resultSet.add(tempitem);
        	tempitem = new Item("Dokument als .xls speichern",null);
        	resultSet.add(tempitem);
	    }
	    return resultSet;
    }
	private String getItemIndex(String item){
		File photo = new File(Constants.FUERTHWIKI_FOLDER+item+".jpg");
		int i=0;
		while(photo.exists())
		{
			i++;
			photo=new File(Constants.FUERTHWIKI_FOLDER+item+"("+i+")"+".jpg");
		}
	    return String.valueOf(i);
	}

}
