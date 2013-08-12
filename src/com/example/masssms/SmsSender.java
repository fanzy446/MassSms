package com.example.masssms;

import com.example.data.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

public class SmsSender extends Activity
{
	private MessageAdapter adapter = null;
	private ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
	Person contact = null;
	Group group = null;
	Contact db = null;
	SmsManager smsManager = SmsManager.getDefault();
	// �༭����ʼ��
	private SmsEditor editor = new SmsEditor(null, SmsEditor.SPRING_FESTIVAL);
	private EditText message = null;
	private String tagContent = null;
	//test
	private String[] temp = {"<����>","<����>","����","����","����","����","����","����","����","����","����","����"};

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sms_send);
		db = new Contact(this);
		message = (EditText) findViewById(R.id.sms_send_content);
		registerReceiver(sendMessage, new IntentFilter("SENT_SMS_ACTION"));
		registerReceiver(receiver, new IntentFilter("DELIVERED_SMS_ACTION"));
		initButton();
		initList();
	}

	// ��ʼ����ť
	private void initButton()
	{
		Button send_button = (Button) findViewById(R.id.sms_send_send);
		Button return_button = (Button) findViewById(R.id.sms_send_return);
		Button generate_button = (Button) findViewById(R.id.sms_send_generate);
		Button tag_button = (Button)findViewById(R.id.sms_send_tag);
		send_button.setOnClickListener(new Button.OnClickListener()
		{
			public void onClick(View v)
			{
				for (int i = 0; i < list.size(); i++)
				{		
					SmsEditor.setBase(message.getText().toString());
					if(MainActivity.show)
					{
						contact = db.getPersonData((Integer)list.get(i).get("id"));
						editor = new SmsEditor(contact, SmsEditor.DEFAULT);
						sendPersonSms(contact, editor.getContent());
					}
					else
					{
						group = db.getGroupData((Integer)list.get(i).get("id"));
						ArrayList<Integer> id_list = group.getMember();
						for(int j = 0; j < id_list.size(); j ++)
						{
							contact = db.getPersonData(id_list.get(j));
							editor = new SmsEditor(contact, SmsEditor.DEFAULT);
							sendPersonSms(contact, editor.getContent());
						}
					}
						
					
				}

			}
		});
		return_button.setOnClickListener(new Button.OnClickListener()
		{
			public void onClick(View v)
			{
				finish();
			}
		});
		generate_button.setOnClickListener(new Button.OnClickListener()
		{
			public void onClick(View v)
			{
				message.setText(SmsEditor.getBase());
			}
		});
		tag_button.setOnClickListener(new Button.OnClickListener()
		{
			public void onClick(View v)
			{
				GridView grid = new GridView(SmsSender.this);
				grid.setNumColumns(4);
				grid.setGravity(Gravity.TOP);
				ArrayAdapter<String> tagAdapter = new ArrayAdapter<String>(SmsSender.this, android.R.layout.select_dialog_item, temp);
				grid.setAdapter(tagAdapter);
				new AlertDialog.Builder(SmsSender.this)
				.setView(grid)
				.setNegativeButton("ȡ��", null)
				.show();
				grid.setOnItemClickListener(new GridView.OnItemClickListener()
				{
					public void onItemClick(AdapterView<?> parent, View view, int position, long id)
					{
						tagContent = temp[position];
						message.getText().insert(message.getSelectionStart(), getClickableSpan(tagContent));
						message.setMovementMethod(LinkMovementMethod.getInstance());
					}
				});			
			}
		});
	}

	// �����ϵ�˲���
	private void initList()
	{
		ListView blank = (ListView) findViewById(R.id.sms_send_contact);
		if(MainActivity.show)
			for (int i = 0; i < MainActivity.person_id_name_number_List.size(); i++)
			{
				if(SmsFragment.choice.get(i))
					list.add(MainActivity.person_id_name_number_List.get(i));
			}
		else
			for (int i = 0; i < MainActivity.group_id_name_number_List.size(); i++)
			{
				if(SmsFragment.choice.get(i))
					list.add(MainActivity.group_id_name_number_List.get(i));
			}
		adapter = new MessageAdapter(this, list, R.layout.list, new String[] {
				"name"}, new int[] { R.id.list_tv, R.id.list_cb });
		adapter.initAllTrue();
		blank.setAdapter(adapter);
		blank.setOnItemClickListener(new ListView.OnItemClickListener() 
		{ 
			public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) 
			{ 
				list.remove(position);
				adapter.notifyDataSetChanged();
			}
		});
	}

	// �����㲥����
	private BroadcastReceiver sendMessage = new BroadcastReceiver()
	{
		public void onReceive(Context context, Intent intent)
		{
			// �ж϶����Ƿ��ͳɹ�
			switch (getResultCode())
			{
			case Activity.RESULT_OK:
				Toast.makeText(context, "���ŷ��ͳɹ�", Toast.LENGTH_SHORT).show();
				break;
			default:
				Toast.makeText(context, "����ʧ��", Toast.LENGTH_LONG).show();
				break;
			}
		}
	};
	private BroadcastReceiver receiver = new BroadcastReceiver()
	{
		public void onReceive(Context context, Intent intent)
		{
			// ��ʾ�Է��ɹ��յ�����
			Toast.makeText(context, "�Է����ճɹ�", Toast.LENGTH_LONG).show();
		}
	};

	// ���Ͷ���
	private void sendPersonSms(Person contact, String sms)
	{
		Intent sentIntent = new Intent("SENT_SMS_ACTION");
		PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, sentIntent,
				0);
		Intent deliverIntent = new Intent("DELIVERED_SMS_ACTION");
		PendingIntent deliverPI = PendingIntent.getBroadcast(this, 0,
				deliverIntent, 0);
		if (sms.length() > 70)
		{
			ArrayList<String> msgs = smsManager.divideMessage(sms);
			for (String msg : msgs)
			{
				smsManager.sendTextMessage((String)contact.getPhone().get(0).get("phoneNumber"), null, msg,
						sentPI, deliverPI);
			}
		}
		else
		{
			smsManager.sendTextMessage((String)contact.getPhone().get(0).get("phoneNumber"), null, sms,
					sentPI, deliverPI);
		}
	}
	
	private SpannableString getClickableSpan(String s)
	{
		View.OnClickListener l = new View.OnClickListener()
		{
			// ���¶����Լ��Ķ���
			public void onClick(View v)
			{
				Toast.makeText(SmsSender.this, "Click Success",
						Toast.LENGTH_LONG).show();

				// ������Ϳ�������ת��activity���ߵ����Ի���Ĳ�����
			}
		};

		SpannableString spanableInfo = new SpannableString(s);
		spanableInfo.setSpan(new Clickable(l), 0, spanableInfo.length(),
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return spanableInfo;
	}
}

class Clickable extends ClickableSpan implements OnClickListener
{
	private final View.OnClickListener mListener;

	public Clickable(View.OnClickListener l)
	{
		mListener = l;
	}

	@Override
	public void onClick(View v)
	{
		mListener.onClick(v);
	}
}
