package com.example.masssms;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ListView;

public class GroupContent extends Activity
{
	private int id;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		id = getIntent().getIntExtra("id", -1);
		setContentView(R.layout.group_content);
		initList();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.group_content, menu);
		return true;
	}

	private void initList()
	{
		ListView blank = (ListView)findViewById(R.id.group_content_blank);
	    
	}
}
