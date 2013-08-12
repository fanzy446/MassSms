package com.example.masssms;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.data.Contact;
import com.example.data.Person;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.TabHost.TabContentFactory;

public class MainActivity extends FragmentActivity
{

	static Contact db = null;
	static ArrayList<HashMap<String, Object>> person_id_name_number_List = null;
	static ArrayList<HashMap<String, Object>> group_id_name_number_List = null;
	static HashMap<Integer, Integer> person_position_id = new HashMap<Integer, Integer>();
	static HashMap<Integer, Integer> group_position_id = new HashMap<Integer, Integer>();
	static boolean show = true;//true代表按人显示，false代表按组显示
	private TabHost tabHost = null;
	private DailFragment dailFragment = null;
	private PoGFragment pogFragment = null;
	private SmsFragment smsFragment = null;
	private int currentTab = 0;
	private FragmentTransaction ft = null;
	private RelativeLayout tabIndicator1, tabIndicator2, tabIndicator3;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		initDb();
		db.getGroupData(7);
		initTabHost();
		
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		switch (resultCode / 10)
		{
		case 1:
		{
			FragmentManager fm = getSupportFragmentManager();
			pogFragment = (PoGFragment) fm.findFragmentByTag("pog");
			pogFragment.modifyData(requestCode, resultCode % 10, data);
			break;
		}

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{	
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_activity, menu);
		super.onCreateOptionsMenu(menu);
		return true;
	}

	private void initDb()
	{
		db = new Contact(this);
		person_id_name_number_List = db.getPerson_id_name_number_List();
		group_id_name_number_List = db.getGroup_id_name_List();
		int p = 0;
		for (int i = 0; i < person_id_name_number_List.size(); i++)
		{
			person_position_id.put(p, (Integer)person_id_name_number_List.get(i).get("id"));
			p++;
		}
		p = 0;
		for (int i = 0; i < group_id_name_number_List.size(); i++)
		{
			group_position_id.put(p, (Integer)group_id_name_number_List.get(i).get("groupId"));
			p++;
		}
	}

	private void initTabHost()
	{
		tabHost = (TabHost) findViewById(android.R.id.tabhost);
		findTabView();
		tabHost.setup();
		tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener()
		{
			@Override
			public void onTabChanged(String tabId)
			{
				// TODO Auto-generated method stub
				FragmentManager fm = getSupportFragmentManager();
				dailFragment = (DailFragment) fm.findFragmentByTag("dail");
				pogFragment = (PoGFragment) fm
						.findFragmentByTag("person");
				smsFragment = (SmsFragment) fm.findFragmentByTag("sms");
				ft = fm.beginTransaction();
				if (dailFragment != null)
				{
					ft.detach(dailFragment);
				}
				if (pogFragment != null)
				{
					ft.detach(pogFragment);
				}
				if (smsFragment != null)
				{
					ft.detach(smsFragment);
				}
				if (tabId.equalsIgnoreCase("dail"))
				{
					if (dailFragment == null)
						ft.add(android.R.id.tabcontent, new DailFragment(),
								"dail");
					else
						ft.attach(dailFragment);
					currentTab = 0;
				}
				else if (tabId.equalsIgnoreCase("pog"))
				{
					if (pogFragment == null)
						ft.add(android.R.id.tabcontent, new PoGFragment(),
								"pog");
					else
						ft.attach(pogFragment);
					currentTab = 1;
				}
				else if (tabId.equalsIgnoreCase("sms"))
				{
					if (smsFragment == null)
						ft.add(android.R.id.tabcontent, new SmsFragment(),
								"sms");
					else
						ft.attach(smsFragment);
					currentTab = 2;
				}
				else
					switch (currentTab)
					{
					case 0:
						ft.attach(dailFragment);
						break;
					case 1:
						ft.attach(pogFragment);
						break;
					case 2:
						ft.attach(smsFragment);
						break;
					default:
						ft.attach(dailFragment);
						break;
					}
				ft.commit();
			}
		});
		initTab();
		tabHost.setCurrentTab(0);
	}
	
	private void findTabView()
	{
		LinearLayout layout = (LinearLayout) tabHost.getChildAt(0);
		TabWidget tw = (TabWidget) layout.getChildAt(1);

		tabIndicator1 = (RelativeLayout) LayoutInflater.from(this).inflate(
				R.layout.tab, tw, false);
		TextView tvTab1 = (TextView) tabIndicator1.getChildAt(0);
		tvTab1.setText("拨号盘");

		tabIndicator2 = (RelativeLayout) LayoutInflater.from(this).inflate(
				R.layout.tab, tw, false);
		TextView tvTab2 = (TextView) tabIndicator2.getChildAt(0);
		tvTab2.setText("通讯录");

		tabIndicator3 = (RelativeLayout) LayoutInflater.from(this).inflate(
				R.layout.tab, tw, false);
		TextView tvTab3 = (TextView) tabIndicator3.getChildAt(0);
		tvTab3.setText("信息");
	}

	private void initTab()
	{
		TabHost.TabSpec tSpecDail = tabHost.newTabSpec("dail");
		tSpecDail.setIndicator(tabIndicator1).setContent(
				new DummyTabContent(this));
		tabHost.addTab(tSpecDail);

		TabHost.TabSpec tSpecPog = tabHost.newTabSpec("pog");
		tSpecPog.setIndicator(tabIndicator2).setContent(
				new DummyTabContent(this));
		tabHost.addTab(tSpecPog);

		TabHost.TabSpec tSpecMessage = tabHost.newTabSpec("sms");
		tSpecMessage.setIndicator(tabIndicator3).setContent(
				new DummyTabContent(this));
		tabHost.addTab(tSpecMessage);
	}
}
class DummyTabContent implements TabContentFactory
{
	private Context mContext;
	public DummyTabContent(Context context)
	{
		mContext = context;
	}

	@Override
	public View createTabContent(String tag)
	{
		View v = new View(mContext);
		return v;
	}
}