package com.mumoki.saveapp;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class Splash extends Activity {
	private int displayTime = 4000;
	
    /** Called when the activity is first created. */
	/** Native methods, implemented in jni folder */
    public static native void createEngine();
    public static native void createBufferQueueAudioPlayer();
    public static native boolean createAssetAudioPlayer(AssetManager assetManager, String filename);
    //true == PLAYING, false == PAUSED
    public static native void setPlayingAssetAudioPlayer(boolean isPlaying);
    public static native void setPlayingUriAudioPlayer(boolean isPlaying);
    public static native void setMuteUriAudioPlayer(boolean mute);
    public static native void shutdown();
	private boolean isPlayingAsset = false;
	private AssetManager assetManager;
	
    /** Load jni .so on initialization */
    static {
         System.loadLibrary("native-audio-jni");
    }
    

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        
        assetManager = getAssets();
        createEngine();
	    createBufferQueueAudioPlayer();

	    boolean created = false;
	             if (!created) {
	             	
	             	created = createAssetAudioPlayer(assetManager, "pacman.mp3");
	                 
	             }
	             if (created) {
	                 isPlayingAsset = !isPlayingAsset;
	                 setPlayingAssetAudioPlayer(isPlayingAsset);
	             }
	             
        TextView brand = (TextView)findViewById(R.id.tvBrand);
		Typeface copper = Typeface.createFromAsset(getAssets(), "fonts/fayet.otf");
		
		brand.setTypeface(copper);
		brand.setTextColor(Color.DKGRAY);
        
        Thread splashThread = new Thread(){
			int wait = 0;
			
			@Override
			public void run(){
				try{
					super.run();
					
					while(wait<displayTime){
						sleep(100);
						wait += 100;
					}
				}catch(Exception e){}
				finally{
					//Intent intent = new Intent(splashActivity.this,toutMain.class);
					Intent intent = new Intent(Splash.this, MainMenu.class);
					startActivity(intent);
					shutdown();
					finish();
			
				}
				
			}
		};
		splashThread.start();
    }
}
