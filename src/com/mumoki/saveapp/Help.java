package com.mumoki.saveapp;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

public class Help extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);
		
		
        TextView help = (TextView)findViewById(R.id.tvHelp);
        TextView saveHelp = (TextView)findViewById(R.id.tvSaveHelp);
		Typeface fayet = Typeface.createFromAsset(getAssets(), "fonts/fayet.otf");
		
		help.setTypeface(fayet);
		saveHelp.setTypeface(fayet);
		help.setTextColor(Color.DKGRAY);
		saveHelp.setTextColor(Color.DKGRAY);
	}

}
