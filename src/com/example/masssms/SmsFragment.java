package com.example.masssms;

import com.example.data.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class SmsFragment extends Fragment
{
	static HashMap<Integer, Boolean> choice = null;
	private MessageAdapter personAdapter = null;
	private MessageAdapter groupAdapter = null;
	private View view = null;
	private ListView blank = null;
	public void onCreate(Bundle savedInstanceState)
	{
		setHasOptionsMenu(true);
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		view = inflater.inflate(R.layout.sms_fragment, container, false);
		blank = (ListView)view.findViewById(R.id.sms_fragment_blank);	
		initButton();
		initPersonList();
		// TODO Auto-generated method stub
		return view;
	}
	
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{	
		// Inflate the menu; this adds items to the action bar if it is present.
		inflater.inflate(R.menu.sms_fragment, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}
	
	public boolean onOptionsItemSelected(MenuItem item) 
	{	 
		super.onOptionsItemSelected(item);
		if(this.isVisible())
			switch(item.getItemId())
			{
			case R.id.menu_sms_fragment_show:
			{
				if(item.getTitle() == "组")
				{
					initPersonList();	
					MainActivity.show = true;
					item.setTitle("人");
				}
				else
				{
					initGroupList();			
					MainActivity.show = false;
					item.setTitle("组");
				}
				break;
			}
			default:break;				
			}
		return true;
	}
	
	private void initButton()
	{
		Button add_button = (Button)view.findViewById(R.id.sms_fragment_add);
		Button send_button = (Button)view.findViewById(R.id.sms_fragment_send);
		add_button.setOnClickListener(new Button.OnClickListener()
		{
			public void onClick(View v)
			{
				
			}
		});
		send_button.setOnClickListener(new Button.OnClickListener()
		{
			public void onClick(View v)
			{
				if(MainActivity.show)
					choice = personAdapter.isSelected;
				else
					choice = groupAdapter.isSelected;
				Intent intent = new Intent();
				intent.setClass(getActivity(), SmsSender.class);
				startActivity(intent);
			}
		});
	}
	private void initPersonList()
	{	
		personAdapter = new MessageAdapter(getActivity(), MainActivity.person_id_name_number_List, R.layout.list, 
				new String[]{"name"}, new int[]{R.id.list_tv, R.id.list_cb});
		blank.setAdapter(personAdapter);
		blank.setOnItemClickListener(new ListView.OnItemClickListener() 
		{ 
			public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) 
			{ 
				ViewHolder holder = (ViewHolder) view.getTag(); 
				holder.cb.toggle();// 在每次获取点击的item时改变checkbox的状态 
				personAdapter.isSelected.put(position, holder.cb.isChecked());
			}
		});
	}
	
	private void initGroupList()
	{	
		groupAdapter = new MessageAdapter(getActivity(), MainActivity.person_id_name_number_List, R.layout.list, 
				new String[]{"groupName"}, new int[]{R.id.list_tv, R.id.list_cb});
		blank.setAdapter(personAdapter);
		blank.setOnItemClickListener(new ListView.OnItemClickListener() 
		{ 
			public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) 
			{ 
				ViewHolder holder = (ViewHolder) view.getTag(); 
				holder.cb.toggle();// 在每次获取点击的item时改变checkbox的状态 
				groupAdapter.isSelected.put(position, holder.cb.isChecked());
			}
		});
	}
}
