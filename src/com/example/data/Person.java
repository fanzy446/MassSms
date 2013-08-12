package com.example.data;
import java.util.ArrayList;
import java.util.HashMap;

public class Person
{
	private int id; //�û�ID
	private String name;//����
	private ArrayList<Integer> groupId = new ArrayList<Integer>(); //������
//	private ArrayList<String> phoneNumber = new ArrayList<String>();//�ֻ�����
//	private HashMap<String,String> phoneType = new HashMap<String,String>(); //���������
	private ArrayList<HashMap<String,Object>> phone = new ArrayList<HashMap<String,Object>>();
	private ArrayList<String> personTag = new ArrayList<String>();//���˱�ǩ
	private ArrayList<String>groupTag = new ArrayList<String>(); //���ǩ
	
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
//	//��Ӹ��˵绰
//	public ArrayList<String> appendPhoneNumber(String phoneNumber)
//	{
//		this.phoneNumber.add(phoneNumber);
//		return this.phoneNumber;
//	}
//	//��ӵ绰����
//	public HashMap<String,String> appendPhoneType(String phoneNumber,String phoneType)
//	{
//		this.phoneType.put(phoneNumber, phoneType);
//		return this.phoneType;
//	}
	//��ӵ绰
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
	//��Ӹ��˱�ǩ
	public ArrayList<String> appendPersonTag(String tag)
	{
		this.personTag.add(tag);
		return this.personTag;
	}
	
	//������ǩ
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
