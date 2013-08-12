package com.example.masssms;

import com.example.data.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class PoGFragment extends Fragment
{
	private View view = null;
	private ListView blank = null;
	private int lastClick = 0;
	private SimpleAdapter personAdapter = null;
	private SimpleAdapter groupAdapter = null;

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
		view = inflater.inflate(R.layout.pog_fragment, container, false);
//		initButton();
		blank = (ListView) view.findViewById(R.id.people_fragment_blank);
		initPersonList();
		this.registerForContextMenu(blank);
		// TODO Auto-generated method stub
		return view;
	}
	
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{	
		// Inflate the menu; this adds items to the action bar if it is present.
		inflater.inflate(R.menu.pog_fragment, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}
	
	public boolean onOptionsItemSelected(MenuItem item) 
	{	 
		super.onOptionsItemSelected(item);
		if(this.isVisible())
			switch(item.getItemId())
			{
			case R.id.menu_pog_fragment_add:
			{
				Intent intent = new Intent();
				if(MainActivity.show)
					intent.setClass(getActivity(), PersonEditor.class);
				else
					intent.setClass(getActivity(), GroupEditor.class);
				startActivityForResult(intent, 1);
				break;
			}
			case R.id.menu_pog_fragment_show:
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
	
	 public void onCreateContextMenu(ContextMenu menu, View v,
	 ContextMenuInfo menuInfo) {
	 // TODO Auto-generated method stub
	 getActivity().getMenuInflater().inflate(R.menu.pog_fragment_c, menu);
	 MenuItem item = menu.getItem(0);
	 if(MainActivity.show)
		 item.setTitle("通话");
	 else
		 item.setTitle("编辑");
	 super.onCreateContextMenu(menu, v, menuInfo);
	 }
	
	 public boolean onContextItemSelected(MenuItem item) {
		 // TODO Auto-generated method stub
		 AdapterContextMenuInfo infor = (AdapterContextMenuInfo)item.getMenuInfo();
		 switch(item.getItemId())
		 {
		 case R.id.menu_pog_fragment_call:
		 {
			 if(MainActivity.show)
			 {
				
			 }
			 else
			 {
				Intent intent = new Intent();
				intent.setClass(getActivity(), GroupEditor.class);
				intent.putExtra("id", MainActivity.group_position_id.get(infor.position));
				startActivityForResult(intent, 2);
			 }
		 }
		 return true;
		 case R.id.menu_pog_fragment_delete:
		 {
			 if(MainActivity.show)
			 {
				MainActivity.db.deletePerson((int)MainActivity.person_position_id.get(infor.position));
				MainActivity.person_id_name_number_List.remove(infor.position);
				personAdapter.notifyDataSetChanged();
			 }
			 else
			 {
				 MainActivity.db.deleteGroup((int)MainActivity.group_position_id.get(infor.position));	
				 MainActivity.group_id_name_number_List.remove(infor.position);
				 groupAdapter.notifyDataSetChanged();
			 }
		 }
		 return true;
		 default:
		 return super.onContextItemSelected(item);
		 }
		 }

//	private void initButton()
//	{
//		Button call_button = (Button) view
//				.findViewById(R.id.people_fragment_call);
//		Button add_button = (Button) view
//				.findViewById(R.id.people_fragment_add);
//		Button edit_button = (Button) view
//				.findViewById(R.id.people_fragment_edit);
//		Button delete_button = (Button) view
//				.findViewById(R.id.people_fragment_delete);
//		call_button.setOnClickListener(new Button.OnClickListener()
//		{
//			public void onClick(View v)
//			{
//				//呼叫
//			}
//		});
//		add_button.setOnClickListener(new Button.OnClickListener()
//		{
//			public void onClick(View v)
//			{
//				Intent intent = new Intent();
//				intent.setClass(getActivity(), PersonEditor.class);
//				startActivityForResult(intent, 1);
//			}
//		});
//		edit_button.setOnClickListener(new Button.OnClickListener()
//		{
//			public void onClick(View v)
//			{
//				Intent intent = new Intent();
//				intent.setClass(getActivity(), PersonEditor.class);
//				intent.putExtra("id", MainActivity.position_id.get(lastClick));
//				startActivityForResult(intent, 2);
//			}
//		});
//		delete_button.setOnClickListener(new Button.OnClickListener()
//		{
//			public void onClick(View v)
//			{
//				MainActivity.db.deletePerson((int)MainActivity.position_id.get(lastClick));				
//				MainActivity.id_name_number_List.remove(lastClick);
//				personAdapter.notifyDataSetChanged();
//			}
//		});
//	}

	private void initPersonList()
	{
		personAdapter = new SimpleAdapter(getActivity(), MainActivity.person_id_name_number_List,
				R.layout.pog_list, new String[] { "name" },
				new int[] { R.id.person_list_name });
		blank.setAdapter(personAdapter);
		blank.setOnItemClickListener(new OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			 {
				lastClick = arg2;
				Intent intent = new Intent();
				intent.setClass(getActivity(), PersonEditor.class);
				intent.putExtra("id", MainActivity.person_position_id.get(arg2));
				startActivityForResult(intent, 1);
			 }
		});			
	}
	
	private void initGroupList()
	{
		groupAdapter = new SimpleAdapter(getActivity(), MainActivity.group_id_name_number_List,
				R.layout.pog_list, new String[] { "groupName" },
				new int[] { R.id.person_list_name });
		blank.setAdapter(groupAdapter);
		blank.setOnItemClickListener(new OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			 {
				lastClick = arg2;
				Intent intent = new Intent();
				intent.setClass(getActivity(), GroupContent.class);
				intent.putExtra("id", MainActivity.group_position_id.get(arg2));
				startActivityForResult(intent, 2);
			 }
		});	
	}
	
	public void modifyData(int requestCode, int resultCode, Intent data)
	{
		int id = data.getIntExtra("id", -1);	
		HashMap<String, Object> map = new HashMap<String, Object>();
		Log.i("error","requestcode:" + requestCode);
		Log.i("error","resultcode:" + resultCode);
		switch(resultCode)
		{
		case 1:
			{
				Person person = MainActivity.db.getPersonData(id);
				map.put("id", id);
				map.put("name", person.getName());
				MainActivity.person_position_id.put(MainActivity.person_id_name_number_List.size(), person.getId());
				MainActivity.person_id_name_number_List.add(map);
				personAdapter.notifyDataSetChanged();
				break;
			}
		case 2:
			{
				Group group = MainActivity.db.getGroupData(id);
				map.put("id", id);
				map.put("name", group.getName());
				MainActivity.group_position_id.put(MainActivity.group_id_name_number_List.size(), group.getId());
				MainActivity.group_id_name_number_List.add(map);
				groupAdapter.notifyDataSetChanged();
				break;
			}
		default:break;
		}
	}
}
