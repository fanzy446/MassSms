package com.example.data;
import java.util.ArrayList;
import java.util.HashMap;

public class Person
{
	private int id; //用户ID
	private String name;//姓名
	private ArrayList<Integer> groupId = new ArrayList<Integer>(); //所在组
//	private ArrayList<String> phoneNumber = new ArrayList<String>();//手机号码
//	private HashMap<String,String> phoneType = new HashMap<String,String>(); //号码的种类
	private ArrayList<HashMap<String,Object>> phone = new ArrayList<HashMap<String,Object>>();
	private ArrayList<String> personTag = new ArrayList<String>();//个人标签
	private ArrayList<String>groupTag = new ArrayList<String>(); //组标签
	
	public Person(String name)
	{
		this.name = name;
	}
	public Person(int id, String name)
	{
		this.id = id;
		this.name = name;
	}

	public int getId()
	{
		return this.id;
	}

	public String getName()
	{
		return this.name;
	}

//	public ArrayList<String> getPhoneNumber()
//	{
//		return this.phoneNumber;
//	}

	public ArrayList<Integer> getGroupId()
	{
		return this.groupId;
	}

	public ArrayList<String> getPersonTag()
	{
		return this.personTag;
	}

	public ArrayList<String> getGroupTag()
	{
		return this.groupTag;
	}
	
	public ArrayList<HashMap<String, Object>> getPhone()
	{
		return phone;
	}
//	//添加个人电话
//	public ArrayList<String> appendPhoneNumber(String phoneNumber)
//	{
//		this.phoneNumber.add(phoneNumber);
//		return this.phoneNumber;
//	}
//	//添加电话类型
//	public HashMap<String,String> appendPhoneType(String phoneNumber,String phoneType)
//	{
//		this.phoneType.put(phoneNumber, phoneType);
//		return this.phoneType;
//	}
	//添加电话
	public ArrayList<HashMap<String,Object>> appendPhone(String phoneNumber, int phoneType)
	{
		HashMap<String,Object> temp = new HashMap<String,Object>();
		temp.put("phoneNumber", phoneNumber);
		temp.put("phoneType", phoneType);
		this.phone.add(temp);
		return this.phone;
	}
	
	public ArrayList<HashMap<String,Object>> appendPhone(long id, String phoneNumber, int phoneType)
	{
		HashMap<String,Object> temp = new HashMap<String,Object>();
		temp.put("id", id);
		temp.put("phoneNumber", phoneNumber);
		temp.put("phoneType", phoneType);
		this.phone.add(temp);
		return this.phone;
	}
	//添加个人标签
	public ArrayList<String> appendPersonTag(String tag)
	{
		this.personTag.add(tag);
		return this.personTag;
	}
	
	//添加组标签
	public ArrayList<String> appendGroupTag(String tag)
	{
		this.groupTag.add(tag);
		return this.groupTag;
	}

	public ArrayList<Integer> appendGroupId(int groupId)
	{
		this.groupId.add(groupId);
		return this.groupId;
	}
//	public HashMap<String, String> getPhoneType()
//	{
//		return phoneType;
//	}

//	public void setPhoneType(HashMap<String, String> phoneType)
//	{
//		this.phoneType = phoneType;
//	}

	
	public void setId(int id)
	{
		this.id = id;
	}

	public void setPhone(ArrayList<HashMap<String, Object>> phone)
	{
		this.phone = phone;
	}

	public void setName(String name)
	{
		this.name = name;
	}



//	public void setPhoneNumber(ArrayList<String> phoneNumber)
//	{
//		this.phoneNumber = phoneNumber;
//	}

	public void setGroupId(ArrayList<Integer> groupId)
	{
		this.groupId = groupId;
	}

	public void setPersonTag(ArrayList<String> personTag)
	{
		this.personTag = personTag;
	}

	public void setGroupTag(ArrayList<String> groupTag)
	{
		this.groupTag = groupTag;
	}
	
	
}
