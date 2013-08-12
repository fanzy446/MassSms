package com.example.masssms;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.data.*;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemClickListener;

public class PersonEditor extends Activity
{
	private int id = 0;
	private int lastClick = 0;
	private Person person = null;
	
	private EditText name_et = null;
	private Spinner sex_s = null;
	private EditText number_et = null;
	private ListView tag_lv = null;	
	private ArrayAdapter<String> tagAdapter = null;
	private EditText tag_add_et = null;
	private ArrayList<String> tag = new ArrayList<String>();
	private ArrayList<HashMap<String ,Object>> phone = new ArrayList<HashMap<String ,Object>>();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		id = this.getIntent().getIntExtra("id", -1);
		setContentView(R.layout.person_editor);
		initButton();
		initList();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.person_editor, menu);
		return true;
	}

	private void initButton()
	{
		Button tag_add_button = (Button) findViewById(R.id.person_editor_tag_add);
		Button tag_delete_button = (Button) findViewById(R.id.person_editor_tag_delete);
		Button save_button = (Button) findViewById(R.id.person_editor_save);
		Button cancle_button = (Button) findViewById(R.id.person_editor_cancle);
		tag_add_button.setOnClickListener(new Button.OnClickListener()
		{
			public void onClick(View v)
			{
				tag_add_et = new EditText(PersonEditor.this);
				new AlertDialog.Builder(PersonEditor.this).setTitle("新标签")
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
					person = new Person(id, name_et.getText().toString());
					person.setPhone(phone);
					person.setPersonTag(tag);
					intent.putExtra("id", (int)MainActivity.db.craeteNewPerson(person));
					setResult(11, intent);
				}
				else
				{
					person.setName(name_et.getText().toString());
					person.setPhone(phone);
					person.setPersonTag(tag);
					MainActivity.db.modifyPerson(person);
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
		name_et = (EditText) findViewById(R.id.person_editor_name);
		sex_s = (Spinner) findViewById(R.id.person_editor_sex);
		number_et = (EditText) findViewById(R.id.person_editor_number);
		tag_lv = (ListView) findViewById(R.id.person_editor_tag_list);
		tag_lv.setSelector(android.R.drawable.list_selector_background);
		
		tagAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1 , tag);

		ArrayList<String> choice = new ArrayList<String>();
		
		choice.add("-性别-");
		choice.add("男");
		choice.add("女");
		ArrayAdapter<String> sexAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, choice);
		sexAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sex_s.setAdapter(sexAdapter);
		
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
			person = MainActivity.db.getPersonData(id);
			name_et.setText(person.getName());
			sex_s.setSelection(1);
			number_et.setText("111");
			tag.addAll(person.getPersonTag());
			tagAdapter.notifyDataSetChanged();			
		}
		tag_lv.setAdapter(tagAdapter);
	}

}
