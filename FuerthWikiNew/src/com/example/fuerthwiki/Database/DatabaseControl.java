package com.example.fuerthwiki.Database;


import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;


public class DatabaseControl {
	private SQLiteDatabase database;
	private MySQLiteDatabase sqlite;
	private String[] datacolumns = { "ID", "Excelfile"};
	public DatabaseControl (Context context) {
		sqlite = MySQLiteDatabase.getInstance(context);
	}
	public void open() throws SQLException {
		database = sqlite.getWritableDatabase();
	}
	public void close() {
		sqlite.close();
	}
	public DatabaseEntry createEntry (String excelfile)
	{
		ContentValues values = new ContentValues();
		values.put ("Excelfile", excelfile);
		long insertID = database.insert ("DATA", null, values);
		Cursor cursor = database.query("DATA", datacolumns, "ID = " + insertID, null, null, null, null);
		cursor.moveToFirst();
		return cursorToEntry(cursor);
	}
	private DatabaseEntry cursorToEntry (Cursor cursor){
		DatabaseEntry e = new DatabaseEntry();
		e.setID(cursor.getLong(0));
		e.setExcelfile(cursor.getString(1));
		return e;
	}
	protected List<DatabaseEntry> getAllEntries() {
		List<DatabaseEntry> EntryList = new ArrayList<DatabaseEntry>();
		Cursor cursor = database.query("DATA", datacolumns, null, null, null, null, null);
		cursor.moveToFirst();
		if(cursor.getCount() == 0) return EntryList;
		while (cursor.isAfterLast() == false) {
			DatabaseEntry entry = cursorToEntry(cursor);
			EntryList.add(entry);
			cursor.moveToNext();
		}
		cursor.close();
		return EntryList;
	}
	
	public void deleteEntry (long ID){
		database.delete("DATA", "ID=" + ID, null);
	}
	public void deleteAllEntries(){
		List<DatabaseEntry> all= this.getAllEntries();
		for(int i=1;i<=all.size();i++)
		{
			this.deleteEntry(i);
	}
	}
	public void updateEntry (DatabaseEntry e){
		ContentValues values = new ContentValues();
		values.put ("ID", e.getID());
		values.put ("Excelfile", e.getExcelfile());
		database.update("DATA", values, "ID=+" + e.getID(), null);
	}
	public DatabaseEntry getEntry (long ID){
		Cursor cursor = database.query("DATA", datacolumns, "ID=" + ID, null, null, null, null);
		cursor.moveToFirst();
		while(cursor.isAfterLast() == false){
			if(cursor.getLong(0) == ID){
				return cursorToEntry(cursor);
			}
			cursor.moveToNext();
		}
		return null;
	}
	public int Size(){
		Cursor cursor = database.query("DATA", datacolumns, null, null, null, null, null);
		cursor.moveToLast();
		return cursor.getCount();
	}
	public DatabaseEntry getLastEntry(){
		Cursor cursor = database.query("DATA", datacolumns, null, null, null, null, null);
		cursor.moveToLast();
		if(cursor.getCount() == 0)
			return null;
		else
		{
			DatabaseEntry entry=cursorToEntry(cursor);
			cursor.close();
			return entry;
		}
	}
}