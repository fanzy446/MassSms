package com.example.data;
import java.util.ArrayList;



public class Group
{
	private String name;//����
	private int id; //��id
	private ArrayList<String> tag = new ArrayList<String>();//���ǩ
	private ArrayList<Integer> member = new ArrayList<Integer>(); //���Ա
	
	public Group(int id)
	{
		this.id = id;
	}
	
	public ArrayList<String>  appendTag(String tag)
	{
		this.tag.add(tag);
		return this.tag;
	}
	
	public ArrayList<Integer> appendMember(int id)
	{
		this.member.add(id);
		return this.member;
	}

	public String getName()
	{
		return name;
	}

	public int getId()
	{
		return id;
	}

	public ArrayList<String> getTag()
	{
		return tag;
	}

	public ArrayList<Integer> getMember()
	{
		return member;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public void setTag(ArrayList<String> tag)
	{
		this.tag = tag;
	}

	public void setMember(ArrayList<Integer> member)
	{
		this.member = member;
	}
	
	
}
