package com.example.myfilemanager;


import java.io.File;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageButton;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import android.widget.TextView;
import android.content.Intent;

public class SortActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置界面布局
		setContentView(R.layout.sort);
		
		ImageButton vedios=(ImageButton) findViewById(R.id.videos);
        vedios.setOnClickListener(
    		new OnClickListener(){
    			@Override
    			public void onClick(View v) {
    				Toast.makeText(SortActivity.this, R.string.vedio, Toast.LENGTH_SHORT).show();
    				
    				Intent v_intent = new Intent(SortActivity.this, VedioActivity.class);
    				startActivity(v_intent);
    				}
    		}
		);
        
		ImageButton songs=(ImageButton) findViewById(R.id.songs);
        songs.setOnClickListener(
    		new OnClickListener(){
    			@Override
    			public void onClick(View v) {
    				Toast.makeText(SortActivity.this, R.string.songs, Toast.LENGTH_SHORT).show();
    				
    				Intent s_intent = new Intent(SortActivity.this, SongsActivity.class);
    				startActivity(s_intent);
    				}
    		}
		);
        
		ImageButton images=(ImageButton) findViewById(R.id.images);
        images.setOnClickListener(
    		new OnClickListener(){
    			@Override
    			public void onClick(View v) {
    				Toast.makeText(SortActivity.this, R.string.images, Toast.LENGTH_SHORT).show();
    				
    				Intent i_intent = new Intent(SortActivity.this, ImageActivity.class);
    				startActivity(i_intent);
    				}
    		}
		);
	}
	
}