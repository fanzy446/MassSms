package com.example.data;

import java.util.ArrayList;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.util.Log;

public class DataBase
{
	private MyDatabaseHelper dbHelper;
	private SQLiteDatabase db;
	private Context context;
	
	public DataBase(Context context)
	{
		this.context = context;
		dbHelper = new MyDatabaseHelper(this.context, "Contact_db.db");
		try
		{
			db = dbHelper.getWritableDatabase();
		}
		catch (SQLiteException e)
		{
			Log.i("error", e.getMessage());
		}

	}

	public Person getPersonDataById(int personId)
	{
		String name = null;
		int groupId = -1;

		ContentResolver resolver = this.context.getContentResolver();

		Cursor cursor = resolver.query(ContactsContract.Data.CONTENT_URI,
				new String[]{ContactsContract.Data._ID,ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME},
				ContactsContract.Data.RAW_CONTACT_ID + "=? AND "+ContactsContract.Data.MIMETYPE +"='"+ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE+"'",
				new String[]{personId+""},
				null);
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
		{
			name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME));
		}
		
//		cursor = resolver.query(ContactsContract.Data.CONTENT_URI, 
//				new String[]{ContactsContract.Data._ID,ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID},
//				ContactsContract.Data.RAW_CONTACT_ID + "=? AND"+ContactsContract.Data.MIMETYPE +"='"+ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE+"'",
//				new String[]{personId+""},null);
//		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
//		{
//			groupId = cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID));
//		}
		Person person = new Person(personId, name);

		cursor = resolver.query(ContactsContract.Data.CONTENT_URI,
				new String[]{ContactsContract.Data._ID,ContactsContract.CommonDataKinds.Phone.NUMBER,ContactsContract.CommonDataKinds.Phone.TYPE},
				ContactsContract.Data.RAW_CONTACT_ID + "=? AND "+ContactsContract.Data.MIMETYPE +"='"+ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE+"'",
				new String[]{personId+""},null);
		
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
		{
			long phoneId = cursor.getLong(cursor.getColumnIndex(ContactsContract.Data._ID));
			String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
			int phoneType = cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
			person.appendPhone(phoneId, phoneNumber, phoneType);
		}
		
		cursor = db.query("userTag", new String[] { "userTag" }, "personId=?",new String[] { personId + "" }, null, null, null);
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
		{
			String tag = cursor.getString(cursor.getColumnIndex("userTag"));
			person.appendPersonTag(tag);
		}
		
		cursor = db.query("userGroup", new String[]{"groupId"}, "personId = ?", new String[]{personId+""}, null, null, null);
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
		{
			groupId = cursor.getInt(cursor.getColumnIndex("groupId"));
			person.appendGroupId(groupId);
		}
		
		cursor = db.query("groupTag", new String[] { "groupTag" },"groupId=?", new String[] { groupId+"" }, null, null, null);
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
		{
			String tag = cursor.getString(cursor.getColumnIndex("groupTag"));
			person.appendGroupTag(tag);
		}
		
		return person;
	}

	public boolean deletePersonDataById(int personId) 
	{
//		if (db.delete("user", "personId=?", new String[] { personId + "" }) == 0)
//			return false;
//		if(db.delete("userPhone", "personId=?", new String[]{personId+""}) == 0)
//			return false;
//		ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
//		
//		ops.add(ContentProviderOperation.newDelete(ContactsContract.Data.CONTENT_URI)
//				.withSelection(ContactsContract.Data.RAW_CONTACT_ID, new String[]{String.valueOf(personId)})
//				.build());
//		try
//		{
//			this.context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
			this.context.getContentResolver().delete(ContactsContract.RawContacts.CONTENT_URI,ContactsContract.RawContacts.CONTACT_ID+"=?",new String[]{String.valueOf(personId)});
//		}
//		catch (OperationApplicationException e)
//		{
//			return false;
//		}
//		catch(RemoteException e)
//		{
//			return false;
//		}
		
		if (db.delete("userTag", "personId=?", new String[] { personId + "" }) == 0)
			return false;
		if (db.delete("userGroup", "personId=?", new String[] { personId + "" }) == 0)
			return false;
		return true;
	}

	public boolean modifyPersonData(Person person) 
	{
		ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

		ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
					.withSelection(ContactsContract.Data.RAW_CONTACT_ID+"=?", new String[]{String.valueOf(person.getId())})
					.withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,person.getName())
					.build());
		try
		{
			this.context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
		}
		catch(RemoteException e)
		{
			return false;
		}
		catch(OperationApplicationException e)
		{
			return false;
		}
		
		for(int i = 0; i<person.getPhone().size();++i)
		{
			ops.clear();
			Long phoneId = (Long) person.getPhone().get(i).get("id");
			String phoneNumber = (String) person.getPhone().get(i).get("phoneNumber");
			int phoneType = this.getSystemPhoneType((Integer) person.getPhone().get(i).get("phoenType"));
	
			ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
						.withSelection(ContactsContract.Data.RAW_CONTACT_ID+"=? AND"+ContactsContract.Data._ID+"=?", new String[]{String.valueOf(person.getId()),String.valueOf(phoneId)})
						.withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phoneNumber)
						.withValue(ContactsContract.CommonDataKinds.Phone.TYPE,phoneType)
						.build());
			try
			{
				this.context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
			}
			catch(RemoteException e)
			{
				return false;
			}
			catch(OperationApplicationException e)
			{
				return false;
			}
			
		}
		
		
		String tag;
		ContentValues values;
		db.delete("userTag", "personId=?", new String[] { person.getId() + "" });
		for (int i = 0; i < person.getPersonTag().size(); i++)
		{
			values = new ContentValues();
			tag = person.getPersonTag().get(i);
			values.put("personId", person.getId());
			values.put("userTag", tag);
			try
			{
				db.insertOrThrow("userTag", null, values);
			}
			catch (SQLException e)
			{
				return false;
			}
		}

		int groupId ;
		db.delete("userGroup", "personId=?", new String[] { person.getId() + "" });
		for (int i = 0; i < person.getGroupId().size(); ++i)
		{
			values = new ContentValues();
			groupId = person.getGroupId().get(i);
			values.put("personId", person.getId());
			values.put("groupId", groupId);
			try
			{
				db.insertOrThrow("userGroup", null, values);
			}
			catch (SQLException e)
			{
				return false;
			}
		}

		return true;
	}

	public int insertPersonData(Person person)
	{
		int personId ;
		ContentValues values = new ContentValues();
		try
		{
			ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>() ; 
			personId = ops.size();
			ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
					.withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
					.withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
					.build());
			
			ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
						.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, personId)
						.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
						.withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, person.getName())
						.build());

			for(int i = 0;i<person.getPhone().size();++i)
			{
				String phoneNumber = (String) person.getPhone().get(i).get("phoneNumber");
				int phoneType = -1;
				if((Integer) person.getPhone().get(i).get("phoneType") != null)
					{
						phoneType = (Integer) person.getPhone().get(i).get("phoneType");
						phoneType = this.getSystemPhoneType(phoneType);
					}
				
				ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
							.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, personId)
							.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
							.withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phoneNumber)
							.withValue(ContactsContract.CommonDataKinds.Phone.TYPE,phoneType)
							.build());
			}
			ContentProviderResult[]  results = this.context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
			personId = (int) ContentUris.parseId(results[0].uri);
			person.setId(personId);
			
			for (int i = 0; i < person.getPersonTag().size(); ++i)
			{
				String tag = person.getPersonTag().get(i);
				values.put("personId", personId);
				values.put("userTag", tag);
				db.insert("userTag", null, values);
				values.clear();
			}
			
			for (int i = 0; i < person.getGroupId().size(); ++i)
			{
				int groupId = person.getGroupId().get(i);
				values.put("personId", personId);
				values.put("groupId", groupId);
				db.insert("userGroup", null, values);
				values.clear();
			}
			
		}
		catch (SQLiteException e)
		{
			Log.i("error", e.getMessage());
			return -1;
		}
		catch (RemoteException e)
		{
			e.printStackTrace();
			return -1;
		} 
		catch (OperationApplicationException e)
		{
			e.printStackTrace();
			return -1;
		}

		return personId;
	}

	public Cursor getPersonList()
	{
//		Cursor cursor = this.context.getContentResolver().query(ContactsContract.Data.CONTENT_URI, 
//				new String[]{ContactsContract.Data.RAW_CONTACT_ID,ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME}, 
//				ContactsContract.Data.MIMETYPE+"=?", new String[]{ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE}, ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME);
		Cursor cursor = this.context.getContentResolver()
				.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
						new String[] {
								ContactsContract.CommonDataKinds.Phone.NUMBER,
								ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
								ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID},
						null, null,
						ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
		return cursor;
	}

	public Cursor getGroupList()
	{
		Cursor cursor = db.query("groupData", null, null, null, null, null, null);
		return cursor;
	}
	
	public Cursor getuserTagTable()
	{
		Cursor cursor = db.query("userTag", new String[] { "personId",
				"userTag" }, null, null, null, null, null);
		return cursor;
	}

	public Cursor getUserGroupTable()
	{
		Cursor cursor = db.query("userGroup", new String[] { "personId",
				"groupId", "groupName" }, null, null, null, null, null);
		return cursor;
	}

	public Cursor getGroupTagTable()
	{
		Cursor cursor = db.query("groupTag", new String[] { "groupID",
				"groupTag" }, null, null, null, null, null);
		return cursor;
	}

	public Group getGroupDataById(int groupId)
	{
		Cursor cursor = db.query("userGroup", new String[]{"personId"}, "groupId=?", new String[]{String.valueOf(groupId)}, null, null, null);
		Group group = new Group(groupId);

		for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext())
		{
			group.appendMember(cursor.getInt(cursor.getColumnIndex("personId")));
		}
		
		cursor = db.query("groupData", new String[]{"groupName"}, "groupId=?", new String[]{String.valueOf(groupId)}, null, null, null);
		cursor.moveToNext();
		group.setName(cursor.getString(cursor.getColumnIndex("groupName")));
	
		cursor = db.query("groupTag", new String[]{"groupTag"}, "groupId=?", new String[]{String.valueOf(groupId)}, null, null, null);
		for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext())
		{
			group.appendTag(cursor.getString(cursor.getColumnIndex("groupTag")));
		}
		
		return group;
	}
	
	public boolean deleteGroupDataById(int groupId)
	{
		
		if (db.delete("groupTag", "groupId=?", new String[] { String.valueOf(groupId) }) == 0)
			return false;
		ContentValues values = new ContentValues();
		values.put("groupId", -1);
		if(db.update("userGroup", values,"groupId=?",new String[]{ String.valueOf(groupId)}) == 0)
			return false;
		if(db.delete("groupData", "groupId=?", new String[]{String.valueOf(groupId)}) == 0)
			return false;
		return true;
	}
	
	public boolean modifyGroupData(Group group)
	{
		ContentValues values = new ContentValues();
		Cursor cursor = db.query("userGroup", new String[]{"personId"}, "groupId=?", new String[]{String.valueOf(group.getId())}, null, null, null);
		
		for(int i = 0; i<group.getMember().size();++i)
		{
			int flag = 0;
			int newPersonId = group.getMember().get(i);
			for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext())
			{
				int personId = cursor.getInt(cursor.getColumnIndex("personId"));
				if(personId == newPersonId)
				{
					flag = 1;
					group.getMember().remove(i); 
					--i;
					break;
				}
			}
			if(flag == 0)
			{
				values.clear();
				values.put("personId", group.getMember().get(i));
				values.put("groupId", group.getId());
				db.insert("userGroup", null, values);
			}
		}
		//将多余的组员插入表userGroup中
		for(int i = 0; i<group.getMember().size();++i)
		{
			values.clear();
			values.put("personId", group.getMember().get(i));
			values.put("groupId", group.getId());
			db.insert("userGroup", null, values);
		}
		
		values.clear();
		values.put("groupName", group.getName());
		db.update("groupData", values, "groupId=?", new String[]{String.valueOf(group.getId())});
		
		db.delete("groupTag", "groupId=?", new String[]{String.valueOf(group.getId())});
		for(int i = 0;i<group.getTag().size();++i)
		{
			values.clear();
			values.put("groupId", group.getId());
			values.put("groupTag", group.getTag().get(i));
			db.insert("groupTag", null, values);
		}
		return true;
	}
	
	public int insertGroupTag(int groupId,String tag)
	{
		ContentValues values = new ContentValues();
		values.put("groupId", groupId);
		values.put("groupTag", tag);
		db.insert("groupTag", null, values);
		return groupId;
	}

	public int deleteGroupTag(int groupId,String tag)
	{
		db.delete("groupTag", "groupId=? AND groupTag=?", new String[]{String.valueOf(groupId),tag});
		return groupId;
	}
	
	public int deleteMemberFromGroup(int groupId,int personId)
	{
		db.delete("userGroup", "personId=? AND groupId=?", new String[]{String.valueOf(groupId),String.valueOf(personId)});
		return personId;
	}
	
	public int insertMemberToGroup(int groupId, int personId)
	{
		ContentValues values = new ContentValues();
		values.put("personId", personId);
		values.put("groupId", groupId);
		db.insert("userGroup", null, values);
		return groupId;
	}
	
	public int groupRename(int groupId,String groupName)
	{
		ContentValues values = new ContentValues();
		values.put("groupName", groupName);
		db.update("groupData", values, "groupId=?", new String[]{String.valueOf(groupId)});
		return groupId;
	}
	
	public int insertGroupData(Group group)
	{
		ContentValues values = new ContentValues();
		String tag;
				
		values.put("groupName", group.getName());
		int id = (int) db.insert("groupData", null, values);
		values.clear();
		values.put("groupId", id);
		db.update("groupData", values, "_id=?", new String[]{String.valueOf(id)});
		
		for(int i = 0;i<group.getTag().size();++i)
		{
			values.clear();
			tag = group.getTag().get(i);
			values.put("groupTag", tag);
			db.insert("groupTag", null, values);
		}
		
		return id;
	}
	
	public int getSystemPhoneType(Integer i )
	{
		switch (i) 
		{
			case 0:{ return ContactsContract.CommonDataKinds.Phone.TYPE_CUSTOM;}
			case 1: { return ContactsContract.CommonDataKinds.Phone.TYPE_HOME;}
			case 2: { return ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE;}
			case 3:{ return ContactsContract.CommonDataKinds.Phone.TYPE_WORK;}
			case 4:{ return ContactsContract.CommonDataKinds.Phone.TYPE_FAX_WORK;}
			case 5:{ return ContactsContract.CommonDataKinds.Phone.TYPE_FAX_HOME;}
			case 6:{ return ContactsContract.CommonDataKinds.Phone.TYPE_PAGER;}
			case 7:{ return ContactsContract.CommonDataKinds.Phone.TYPE_OTHER;}
			case 8:{ return ContactsContract.CommonDataKinds.Phone.TYPE_CAR;}
			case 9:{ return ContactsContract.CommonDataKinds.Phone.TYPE_COMPANY_MAIN;}
			case 10:{ return ContactsContract.CommonDataKinds.Phone.TYPE_ISDN;}
			case 11:{ return ContactsContract.CommonDataKinds.Phone.TYPE_MAIN;}
			case 12:{ return ContactsContract.CommonDataKinds.Phone.TYPE_OTHER_FAX;}
			case 13:{ return ContactsContract.CommonDataKinds.Phone.TYPE_RADIO;}
			case 14:{ return ContactsContract.CommonDataKinds.Phone.TYPE_TELEX;}
			case 15:{ return ContactsContract.CommonDataKinds.Phone.TYPE_TTY_TDD;}
			case 16:{ return ContactsContract.CommonDataKinds.Phone.TYPE_WORK_MOBILE;}
			case 17:{ return ContactsContract.CommonDataKinds.Phone.TYPE_WORK_PAGER;}
			case 18:{ return ContactsContract.CommonDataKinds.Phone.TYPE_ASSISTANT;}
			case 19:{ return ContactsContract.CommonDataKinds.Phone.TYPE_MMS;}
			default: {return -1;}
		}
	}
}
