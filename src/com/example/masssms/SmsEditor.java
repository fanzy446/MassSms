package com.example.masssms;

import java.util.ArrayList;

import com.example.data.Person;


public class SmsEditor
{
	//基础根据base代号
	public static final int DEFAULT = 0;
	public static final int SPRING_FESTIVAL = 1;
	
	//内容替代标志代号
	private final static String CALL = "<称呼>";
	private final static String PERSON_TAG = "<个人标签>";
	private final static String GROUP_TAG = "<组标签>";
	
	
	static private String base;//生成短信基础根据
	private String content;//短信内容
	private Person person;//联系人
	
	public SmsEditor(Person c, int b)
	{
		person = c;
		switch(b)
		{
		case DEFAULT:break;
		case SPRING_FESTIVAL:base = CALL + "新年快乐！" + PERSON_TAG + "," + GROUP_TAG + "," + PERSON_TAG + "," + GROUP_TAG + "。";break;
		}
		content = getBase();
	}
	public static String getBase()
	{
		return base;
	}
	public static void setBase(String base)
	{
		SmsEditor.base = base;
	}
	//自动生成短信并返回
	public String getContent()
	{
		//根据base生成相应基础短信模版
		content = getBase();
		replaceCall();
		replacePersonTag();
		replaceGroupTag();
		return content;
	}
	//生成称呼
	private void replaceCall()
	{
		String call = person.getName();
		call += "先生";
		content = content.replaceAll(CALL, call);
	}
	
	private void replacePersonTag()
	{
		ArrayList<String> tag = person.getPersonTag();
		
		int tagNum = tag.size();
		int space = getSpaceNum(PERSON_TAG);
		int temp = tagNum;
		if(tagNum < space)
		{
			while(temp > 0)
			{
				int index = (int)(Math.random() * temp);		
				String s = tag.get(index);		
				content = content.replaceFirst(PERSON_TAG, s);
				tag.remove(index);
				temp --;
			}
			content = content.replaceAll(PERSON_TAG, "");
		}
		else
		{
			for(int i = 0; i < space; i++)
			{
				int index = (int)(Math.random() * temp);
				String s = tag.get(index);
				content = content.replaceFirst(PERSON_TAG, s);
				tag.remove(index);
				temp --;
			}
		}
	}
	
	private void replaceGroupTag()
	{
		ArrayList<String> tag = person.getGroupTag();
		int tagNum = tag.size();
		int space = getSpaceNum(GROUP_TAG);
		int temp = tagNum;
		if(tagNum < space)
		{
			while(temp > 0)
			{
				int index = (int)(Math.random() * temp);
				String s = tag.get(index);
				content = content.replaceFirst(GROUP_TAG, s);
				tag.remove(index);
				temp --;
			}
			content = content.replaceAll(GROUP_TAG, "");
		}
		else
		{
			for(int i = 0; i < space; i++)
			{
				int index = (int)(Math.random() * temp);
				String s = tag.get(index);
				content = content.replaceFirst(GROUP_TAG, s);
				tag.remove(index);
				temp --;
			}
		}
	}
	public int getSpaceNum(String s)
	{
		int n = 0;
		int temp = 0;
		while(temp != -1)
		{
			temp = content.indexOf(s, temp);
			if(temp != -1)
			{
				n ++;
				temp += s.length();
			}
		}
		return n;
	}
}
