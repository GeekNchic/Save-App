package com.mumoki.saveapp;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Contacts extends Activity{
DBAdapter myDb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.econtacts);
		openDB();		
		
        Button save = (Button)findViewById(R.id.bSave);
        save.setOnClickListener(onSave);
        
        myDb.insertRow("Emergency Contact 1", "727000000");
        myDb.insertRow("Emergency Contact 2", "700000000");
        myDb.insertRow("Emergency Contact 3", "734000000");
        
		for(int id = 0;id < 4; id++){
			editTextsWithNumbers(id);
		}
		
        TextView contacts = (TextView)findViewById(R.id.tvContacts);
        TextView tutorial = (TextView)findViewById(R.id.tvTutorial);
		Typeface fayet = Typeface.createFromAsset(getAssets(), "fonts/fayet.otf");
		
		contacts.setTypeface(fayet);
		save.setTypeface(fayet);
		tutorial.setTypeface(fayet);
		tutorial.setTextColor(Color.DKGRAY);
		contacts.setTextColor(Color.DKGRAY);
	}
	
	
	public void clearAll() {
		myDb.deleteAll();
		Toast.makeText(getApplicationContext(), "Contacts cleared from DB\n\n*Remember to set up new ones ;)", Toast.LENGTH_SHORT).show();
		Intent mainMenu = new Intent (getApplicationContext(),MainMenu.class);
		startActivity(mainMenu);
	}
	
	private void openDB() {
		myDb = new DBAdapter(this);
		myDb.open();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();	
		closeDB();
	}
	
	//Close the database to avoid a leak
	private void closeDB() {
		myDb.close();
	}
	
	 public View.OnClickListener onSave = new View.OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			TempInfoHolder r = new TempInfoHolder();
			
			EditText name1 = (EditText) findViewById(R.id.etName1);
			EditText name2 = (EditText) findViewById(R.id.etName2);
			EditText name3 = (EditText) findViewById(R.id.etName3);
			EditText phone1 = (EditText) findViewById(R.id.etPhone1);
			EditText phone2 = (EditText) findViewById(R.id.etPhone2);
			EditText phone3 = (EditText) findViewById(R.id.etPhone3);
			
			r.setName1(name1.getText().toString());
			r.setName2(name2.getText().toString());
			r.setName3(name3.getText().toString());
			r.setPhone1(phone1.getText().toString());
			r.setPhone2(phone2.getText().toString());
			r.setPhone3(phone3.getText().toString());
			
			myDb.updateRow(1, r.getName(), "+254" + r.getPhone1());
			myDb.updateRow(2, r.getName2(), "+254" + r.getPhone2());
			myDb.updateRow(3, r.getName3(), "+254" + r.getPhone3());


			
			for(int id = 0;id < 4; id++){
				displayToast(id);
			}
			
			Intent mainMenu = new Intent (getApplicationContext(),MainMenu.class);
			startActivity(mainMenu);
		}
	};
	
	public void editTextsWithNumbers(int id){
		EditText phone3 = (EditText)findViewById(R.id.etPhone3);
		EditText phone2 = (EditText)findViewById(R.id.etPhone2);
		EditText phone1 = (EditText)findViewById(R.id.etPhone1);
		
		EditText name3 = (EditText)findViewById(R.id.etName3);
		EditText name2 = (EditText)findViewById(R.id.etName2);
		EditText name1 = (EditText)findViewById(R.id.etName1);
		
		Cursor cursor = myDb.getRow(id);
		if (cursor.moveToFirst()) {
			String name = cursor.getString(DBAdapter.COL_NAME);
			String phone = cursor.getString(DBAdapter.COL_NUMBER);
			
			if(id == 1){
				phone1.setText(phone);
				name1.setText(name);
			}
			if(id == 2){
				phone2.setText(phone);
				name2.setText(name);
			}
			if(id == 3){
				phone3.setText(phone);
				name3.setText(name);
			}
		}
		cursor.close();

	}
	
	
	private void displayToast(long id) {
		// TODO Auto-generated method stub
		
			Cursor cursor = myDb.getRow(id);
			if (cursor.moveToFirst()) {
				long idDB = cursor.getLong(DBAdapter.COL_ROWID);
				String name = cursor.getString(DBAdapter.COL_NAME);
				String phone = cursor.getString(DBAdapter.COL_NUMBER);
				
				String message = "ID: " + idDB + "\n" 
						+ "Name: " + name + "\n"
						+ "Number: " + "+" + phone;
				Toast.makeText(Contacts.this, message, Toast.LENGTH_LONG).show();
			}
			cursor.close();
	}
	
}