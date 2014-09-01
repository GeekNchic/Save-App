package com.mumoki.saveapp;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;
import android.widget.Toast;
public class MainMenu extends Activity implements OnClickListener, OnMenuItemClickListener{

	private PopupMenu popupMenu;
	private MenuItem mHelp;
	private MenuItem mSettings;
	private MenuItem mAbout;
    private final static int ONE = 1;
    private final static int TWO = 2;
    DBAdapterSaveMe mySaveDb;
    String phoneNo = "", smsMessage = "";
    
       @Override
       protected void onCreate(Bundle savedInstanceState) {
             super.onCreate(savedInstanceState);
             setContentView(R.layout.mainmenu);
             popupMenu = new PopupMenu(this, findViewById(R.id.anchor));
             popupMenu.getMenu().add(Menu.NONE, ONE, Menu.NONE, "Set up emergency contacts");
             popupMenu.getMenu().add(Menu.NONE, TWO, Menu.NONE, "Set up emergency message");
             openDB();
             popupMenu.setOnMenuItemClickListener(this);
             
            TextView saveApp = (TextView)findViewById(R.id.tvSaveApp);
            TextView selSpanner = (TextView)findViewById(R.id.tvSelectSpanner);
            TextView setContacts = (TextView)findViewById(R.id.tvSelectEmergencyContacts);
            TextView setMessages = (TextView)findViewById(R.id.tvSetUpMessages);
            Button saveMeNow = (Button)findViewById(R.id.bSaveMeNow);
     		Typeface fayet = Typeface.createFromAsset(getAssets(), "fonts/fayet.otf");
     		
     		selSpanner.setTypeface(fayet);
     		saveApp.setTypeface(fayet);
     		saveMeNow.setTypeface(fayet);
     		setContacts.setTypeface(fayet);
     		setMessages.setTypeface(fayet);
     		saveApp.setTextColor(Color.DKGRAY);
       }
       
       public void initiateGetSMS(int id){
			Cursor cursor = mySaveDb.getRow(id);
			if (cursor.moveToFirst()) {
				//String name = cursor.getString(DBAdapterSaveMe.COL_NAME);
				phoneNo = cursor.getString(DBAdapterSaveMe.COL_NUMBER);
				smsMessage = cursor.getString(DBAdapterSaveMe.COL_MESSAGE);
				
			}
			cursor.close();
       }
       
       @Override
   	protected void onResume() {
   		// TODO Auto-generated method stub
   		super.onResume();
   		openDB();
   	}
   	@Override
   	protected void onDestroy() {
   		super.onDestroy();	
   		closeDB();
   	}

   	public void openDB() {
   		mySaveDb = new DBAdapterSaveMe(this);
   		mySaveDb.open();
   	}
   	
   	private void closeDB() {
   		mySaveDb.close();
   	}
       
       @Override
       public boolean onCreateOptionsMenu(Menu menu) {
           
           mHelp = menu.add("Save App Help");
           mSettings = menu.add("Settings");
           mAbout = menu.add("About");
           return true;
       }
       
       @Override
       public boolean onOptionsItemSelected(MenuItem item) {
    	   if(item == mHelp){
    		   Intent help = new Intent(getApplicationContext(),Help.class);
    		   startActivity(help);
    	   }
    	   else if(item == mSettings){
    		   Intent settings = new Intent(getApplicationContext(),Settings.class);
    		   startActivity(settings);
    	   }
    	   else if(item == mAbout){
    		   Intent about = new Intent(getApplicationContext(),About.class);
    		   startActivity(about);
    	   }
    	  return true; 
       }
    
    public void sendSms(View v){
    	for(int id = 0; id < 2; id++){
    		initiateGetSMS(id);
    	}
    	try{
    		SmsManager smsManager = SmsManager.getDefault();
    		smsManager.sendTextMessage("+" + phoneNo, null, smsMessage, null, null);
    		Toast.makeText(getApplicationContext(), "Sending SMS to:\n" + "+" + phoneNo, Toast.LENGTH_SHORT).show();
    	}
    	catch(Exception e){
    		Toast.makeText(getApplicationContext(), "SMS sending failed, please try again.", Toast.LENGTH_SHORT).show();
    	}
    }
	
	@Override
	public void onClick(View v) {
	       popupMenu.show();
	}
	@Override
	public boolean onMenuItemClick(MenuItem item) {
	       switch (item.getItemId()) {
	       case ONE:
	    	   startActivity(new Intent("com.mumoki.saveapp.Contacts"));
	           
	              break;
	       case TWO:
	    	   startActivity(new Intent("com.mumoki.saveapp.Emessage"));
	              break;
	       }
	       return false;
	}
}
