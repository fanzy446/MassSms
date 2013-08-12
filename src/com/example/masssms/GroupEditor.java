package com.example.masssms;

import java.util.ArrayList;

import com.example.data.Group;
import com.example.data.Person;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class GroupEditor extends Activity
{
	private int id;
	private int lastClick = 0;
	private Group group = null;
	
	private EditText name_et = null;
	private ListView tag_lv = null;	
	private ArrayAdapter<String> tagAdapter = null;
	private EditText tag_add_et = null;
	private ArrayList<String> tag = new ArrayList<String>();
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		id = getIntent().getIntExtra("id", -1);
		setContentView(R.layout.group_editor);
		initButton();
		initList();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.group_editor, menu);
		return true;
	}
	
	private void initButton()
	{
		// TODO Auto-generated method stub
		Button tag_add_button = (Button) findViewById(R.id.group_editor_tag_add);
		Button tag_delete_button = (Button) findViewById(R.id.group_editor_tag_delete);
		Button save_button = (Button) findViewById(R.id.group_editor_save);
		Button cancle_button = (Button) findViewById(R.id.group_editor_cancle);
		tag_add_button.setOnClickListener(new Button.OnClickListener()
		{
			public void onClick(View v)
			{
				tag_add_et = new EditText(GroupEditor.this);
				new AlertDialog.Builder(GroupEditor.this).setTitle("新标签")
						.setIcon(android.R.drawable.ic_dialog_info)
						.setView(tag_add_et)
						.setNegativeButton("取消", null)
						.setPositiveButton("确定", new OnClickListener()
						{
							public void onClick(DialogInterface dialog,
									int which)
							{
								tag.add(tag_add_et.getText().toString());
								tagAdapter.notifyDataSetChanged();
								dialog.dismiss();
							}
						}).show();
			}
		});
		tag_delete_button.setOnClickListener(new Button.OnClickListener()
		{
			public void onClick(View v)
			{				
				tag.remove(lastClick);
				tagAdapter.notifyDataSetChanged();
			}
		});
		save_button.setOnClickListener(new Button.OnClickListener()
		{
			public void onClick(View v)
			{
				Intent intent = new Intent();				
				if(id == -1)
				{
					group = new Group(id);
					group.setName(name_et.getText().toString());
					group.setTag(tag);
					group.setMember(new ArrayList<Integer>());
					int temp = (int)MainActivity.db.createNewGroup(group);
					intent.putExtra("id", temp);
					setResult(12, intent);
				}
				else
				{
					group.setName(name_et.getText().toString());
					group.setTag(tag);					
					MainActivity.db.modifyGroup(group);
					intent.putExtra("id", id);
					setResult(10, intent);
				}
				finish();
			}
		});
		cancle_button.setOnClickListener(new Button.OnClickListener()
		{
			public void onClick(View v)
			{
				finish();
			}
		});
	}
	
	private void initList()
	{
		// TODO Auto-generated method stub
		name_et = (EditText) findViewById(R.id.group_editor_name);
		tag_lv = (ListView) findViewById(R.id.group_editor_tag_list);
		tag_lv.setSelector(android.R.drawable.list_selector_background);
		tagAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1 , tag);
		
		tag_lv.setOnItemClickListener(new OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id)
			{
				if(lastClick != position)
				{
//					arg0.getChildAt(lastClick).setBackground(
//					arg0.getBackground());
//					arg1.setBackgroundResource(android.R.color.holo_blue_bright);
					lastClick = position;
					tag_lv.setSelection(position);
				}

			}
		});
		if (id != -1)
		{
			group = MainActivity.db.getGroupData(id);
			name_et.setText(group.getName());
			tag.addAll(group.getTag());
			tagAdapter.notifyDataSetChanged();			
		}
		tag_lv.setAdapter(tagAdapter);
	}

	

	

}
