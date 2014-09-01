package com.mumoki.saveapp;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class Message extends Activity implements OnClickListener, OnMenuItemClickListener{
DBAdapterForMessages myMessageDb;
private PopupMenu popupMenu;
private final static int ONE = 1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.emessage);
		openDB();

        popupMenu = new PopupMenu(this, findViewById(R.id.imageView1));
        popupMenu.getMenu().add(Menu.NONE, ONE, Menu.NONE, "Clear up emergency contacts");
        
        popupMenu.setOnMenuItemClickListener(this);
        findViewById(R.id.imageView1).setOnClickListener(this);
		populateListViewFromDB();
		registerListClickCallback();
	}
	
	@Override
	public void onClick(View v) {
	       popupMenu.show();
	}
	@Override
	public boolean onMenuItemClick(MenuItem item) {
	       switch (item.getItemId()) {
	       case ONE:
	    	  clearAll();
	    	  Toast.makeText(getApplicationContext(), "Emergency messages all deleted :)", Toast.LENGTH_SHORT).show();
	              break;
	       }
	       return false;
	}
	
	public void clearAll() {
		myMessageDb.deleteAll();
		populateListViewFromDB();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();	
		closeDB();
	}

	private void openDB() {
		myMessageDb = new DBAdapterForMessages(this);
		myMessageDb.open();
	}
	private void closeDB() {
		myMessageDb.close();
	}
	
	public void execute(View v){
		EditText message = (EditText) findViewById(R.id.etCustomMessage);
		String _message = message.getText().toString();
		myMessageDb.insertRow(_message);
		populateListViewFromDB();
		Intent mainScreen = new Intent(getApplicationContext(),MainMenu.class);
		startActivity(mainScreen);
		Toast.makeText(getApplicationContext(), "Custom Message saved", Toast.LENGTH_SHORT).show();
	}
	
	
	//what if i replace listViewDumb with box1
	private void populateListViewFromDB() {
		Cursor cursor = myMessageDb.getAllRows();
		
		startManagingCursor(cursor);

		String[] fromFieldNames = new String[] 
				{DBAdapterForMessages.KEY_MESSAGE};
		int[] toViewIDs = new int[]
				{R.id.tvMessageRow};

		SimpleCursorAdapter myCursorAdapter = new SimpleCursorAdapter(this,R.layout.list,cursor,fromFieldNames,toViewIDs);

		
		ListView myList = (ListView) findViewById(R.id.lvTemplateMessages);
		myList.setAdapter(myCursorAdapter);
	}

	
	private void registerListClickCallback() {
		
		ListView myList = (ListView) findViewById(R.id.lvTemplateMessages);
		myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long idInDB) {
				//Do something when list is clicked.....
				
					updateItemForId(idInDB);
					}
		});
	}
	
	//updateItemForId(long idInDB)
	private void updateItemForId(long idInDB) {
		Cursor cursor = myMessageDb.getRow(idInDB);
		if (cursor.moveToFirst()) {
			long idDB = cursor.getLong(DBAdapterForMessages.COL_ROWID);
			String message = cursor.getString(DBAdapterForMessages.COL_MESSAGE);

			message += "!";
			myMessageDb.updateRow(idInDB, message);
		}
		cursor.close();
		populateListViewFromDB();			
	}
	

}

