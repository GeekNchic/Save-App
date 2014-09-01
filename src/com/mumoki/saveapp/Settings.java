package com.mumoki.saveapp;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class Settings extends Activity{
DBAdapter myDb;
DBAdapterSaveMe mySaveDb;
DBAdapterForMessages myMessageDb;
String message = "Sample Message";
String number1 = "",number2 = "", number3 = "";
String name1 = "", name2 = "", name3 = "";
String name, number;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		openDB();
		//mySaveDb.insertRow("Emergency Contact 1", "+254" + "0727000000","sample message");
		populateListViewFromDB();
		registerListClickCallback();
		for(int id = 0;id < 4; id++){
			radioButtonsWithNumbers(id);
		}
		//execute();
	}
	public void radioButtonsWithNumbers(int id){
		RadioButton phone3 = (RadioButton)findViewById(R.id.rbPhone3);
		RadioButton phone2 = (RadioButton)findViewById(R.id.rbPhone2);
		RadioButton phone1 = (RadioButton)findViewById(R.id.rbPhone1);
		
		Cursor cursor = myDb.getRow(id);
		if (cursor.moveToFirst()) {
			long idDB = cursor.getLong(DBAdapter.COL_ROWID);
			String name = cursor.getString(DBAdapter.COL_NAME);
			String phone = cursor.getString(DBAdapter.COL_NUMBER);
			
			if(id == 1){
				phone1.setText(name + ": [+"+ phone + "]");
			}
			if(id == 2){
				phone2.setText(name + ": [+"+ phone + "]");
			}
			if(id == 3){
				phone3.setText(name + ": [+"+ phone + "]");
			}
//			String message = "ID: " + idDB + "\n" 
//					+ "Name: " + name + "\n"
//					+ "Number: " + "+" + phone;
//			Toast.makeText(Settings.this, message, Toast.LENGTH_LONG).show();
		}
		cursor.close();

	}
	
	public void clearAll() {
		mySaveDb.deleteAll();
		myMessageDb.deleteAll();
		populateListViewFromDB();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		openDB();
		for(int id = 0;id < 4; id++){
			radioButtonsWithNumbers(id);
			//pushToDB(id);
		}
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();	
		closeDB();
	}

	public void openDB() {
		mySaveDb = new DBAdapterSaveMe(this);
		myMessageDb = new DBAdapterForMessages(this);
		myDb = new DBAdapter(this);
		mySaveDb.open();
		myMessageDb.open();
		myDb.open();
	}
	
	private void closeDB() {
		mySaveDb.close();
		myMessageDb.close();
		myDb.close();
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

		
		ListView myList = (ListView) findViewById(R.id.lvGotMessages);
		myList.setAdapter(myCursorAdapter);
	}
	
	private void registerListClickCallback() {
		
		ListView myList = (ListView) findViewById(R.id.lvGotMessages);
		myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long idInDB) {
				//Do something when list is clicked.....
				getTextFromListView(idInDB);
				}
		});
	}
	
	public void getTextFromListView(long idInDB){
			Cursor cursor = myMessageDb.getRow(idInDB);
			EditText messageGot = (EditText)findViewById(R.id.etMessageGot);
			
			if (cursor.moveToFirst()) {
				long idDB = cursor.getLong(DBAdapterForMessages.COL_ROWID);
				message = cursor.getString(DBAdapterForMessages.COL_MESSAGE);
				messageGot.setText(message);
			}
			cursor.close();
		}
	
	
	
	public void execute(View v){
		for(int id = 0; id < 4; id++){
			pushToDB(id);
		}
	}
	
	public void pushToDB(int id){

		RadioGroup phones = (RadioGroup)findViewById(R.id.phones);

		Cursor cursor = myDb.getRow(id);
		if (cursor.moveToFirst()) {
		number = cursor.getString(DBAdapter.COL_NUMBER);
		name = cursor.getString(DBAdapter.COL_NAME);
		
		if(id == 1){
			number1 = number;
			name1 = name;
		}
		if(id == 2){
			number2 = number;
			name2 = name;
		}
		if(id == 3){
			number3 = number;
			name3 = name;
		}
		
		switch(phones.getCheckedRadioButtonId()){
		
		case R.id.rbPhone1:
			mySaveDb.updateRow(1, name1, number1, message);
			if(id == 1){
				String info = "You have set up: \nID: " + id + "\n" 
				+ "Name: " + name1 + "\n"
				+ "Number: " + "+" + number1 + "\n"
				+ "Message: " + "+" + message;
				Toast.makeText(Settings.this, info, Toast.LENGTH_LONG).show();
				Intent main = new Intent(getApplicationContext(),MainMenu.class);
				startActivity(main);
			}
			break;
		case R.id.rbPhone2:
			mySaveDb.updateRow(1, name2, number2, message);
			if(id == 2){
				String _info = "You have set up: \nID: " + id + "\n" 
				+ "Name: " + name2 + "\n"
				+ "Number: " + "+" + number2 + "\n"
				+ "Message: " + "+" + message;
				Toast.makeText(Settings.this, _info, Toast.LENGTH_LONG).show();
				Intent _main = new Intent(getApplicationContext(),MainMenu.class);
				startActivity(_main);
			}
			break;
		case R.id.rbPhone3:
			mySaveDb.updateRow(1, name3, number3, message);
			if(id ==3){
				String __info = "You have set up: \nID: " + id + "\n" 
				+ "Name: " + name3 + "\n"
				+ "Number: " + "+" + number3 + "\n"
				+ "Message: " + "+" + message;
				Toast.makeText(Settings.this, __info, Toast.LENGTH_LONG).show();
				Intent __main = new Intent(getApplicationContext(),MainMenu.class);
				startActivity(__main);
			}

			break;
		}

		cursor.close();
		}
	}
}
