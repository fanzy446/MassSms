package com.example.data;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

public class Contact
{
	
	private DataBase db;
	private Context context;
	@SuppressLint("UseSparseArrays")
	private ArrayList<HashMap<String,Object>> person_id_name_number_List = new ArrayList<HashMap<String,Object>>();
//	private HashMap<String,ArrayList<Integer>> name_id_List = new HashMap<String,ArrayList<Integer>>();  //暂时不实现这个表
	private ArrayList<Integer> personIdList = new ArrayList<Integer>();
	private ArrayList<HashMap<String,Object>> group_id_name_List = new ArrayList<HashMap<String,Object>>();
	
	public Contact(Context context)
	{
		this.context = context;
		db = new DataBase(this.context);
		this.makePersonList();
		this.makeGroupList();
	}
	
	private void makePersonList()
	{
		Cursor cursor = db.getPersonList();
//		ArrayList<Integer> tempList ;
		for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext())
		{
			HashMap<String,Object> tempHashMap = new HashMap<String,Object>();
			tempHashMap.put("id", cursor.getInt(cursor.getColumnIndex(ContactsContract.Data.RAW_CONTACT_ID)));
			tempHashMap.put("name", cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
			tempHashMap.put("number", cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
			this.person_id_name_number_List.add(tempHashMap);
			
			this.personIdList.add(cursor.getInt(cursor.getColumnIndex(ContactsContract.Data.RAW_CONTACT_ID)));
// name_id相关，暂时不实现
//			tempList = this.name_id_List.get(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME)));
//			if(tempList == null)
//				tempList = new ArrayList<Integer>();
//			tempList.add(cursor.getInt(cursor.getColumnIndex(ContactsContract.Data.RAW_CONTACT_ID)));
//			this.name_id_List.put(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME)),tempList);
		}
	}
	
	private void makeGroupList()
	{
		Cursor cursor = db.getGroupList();
		for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext())
		{
			HashMap<String,Object> tempHashMap = new HashMap<String,Object>();
			tempHashMap.put("groupId",cursor.getInt(cursor.getColumnIndex("groupId")));
			tempHashMap.put("groupName", cursor.getString(cursor.getColumnIndex("groupName")));
			this.group_id_name_List.add(tempHashMap);
		}
	}
	
    public int craeteNewPerson(Person person)
    {
    	return db.insertPersonData(person);
    }
    
	public boolean deletePerson(Person person)
	{
		return db.deletePersonDataById(person.getId());
	}
	
	public boolean deletePerson(int id)
	{
		return db.deletePersonDataById(id);
	}
	
	public boolean modifyPerson(Person person)
	{
		return db.modifyPersonData(person);
	}
	
	public Person getPersonData(int id)
	{
		return db.getPersonDataById(id);
	}
	
	public int createNewGroup(Group group)
	{
		return db.insertGroupData(group);
	}
	
	public boolean deleteGroup(Group group)
	{
		return db.deleteGroupDataById(group.getId());
	}
	
	public boolean deleteGroup(int id)
	{
		return db.deleteGroupDataById(id);
	}
	
	public boolean modifyGroup(Group group)
	{
		return db.modifyGroupData(group);
	}
	
	public int modifyGroup(int groupId, int personId,int flag)
	{
		if(flag==1)
		{
			return db.insertMemberToGroup(groupId, personId);
		}
		else if(flag == -1)
		{
			return db.deleteMemberFromGroup(groupId, personId);
		}
		else return -1;
	}
	
	public int modifyGroup(int groupId,String tag,int flag)
	{
		if(flag==1)
		{
			return db.insertGroupTag(groupId, tag);
		}
		else if(flag == -1)
		{
			return db.deleteGroupTag(groupId, tag);
		}
		else return -1;
	}
	
	public Group getGroupData(int id)
	{
		return db.getGroupDataById(id);
	}
	

	public ArrayList<HashMap<String, Object>> getPerson_id_name_number_List()
	{
		return person_id_name_number_List;
	}
	

	public ArrayList<Integer> getPersonIdList()
	{
		return personIdList;
	}
	

	public ArrayList<HashMap<String, Object>> getGroup_id_name_List()
	{
		return group_id_name_List;
	}
	
	
// name_id相关，暂时不实现
//    public ArrayList<Person> searchPersonByName(String name)
//    {
//    	ArrayList<Integer> tempList ;
//    	ArrayList<Person> personList = new ArrayList<Person>();
//    	tempList = name_id_List.get(name);
//    	for(int i  = 0;i < tempList.size();i++)
//    	{
//    		int id = tempList.get(i);
//    		personList.add(db.getDataById(id));
//    	}
//    	return personList;
//    }
// name_id相关，暂时不实现
//	public HashMap<String, ArrayList<Integer>> getName_id_List()
//	{
//		return name_id_List;
//	}


}
