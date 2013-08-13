package com.example.masssms;

import java.util.ArrayList;

import com.example.data.Person;


public class SmsEditor
{
	//��������base����
	public static final int DEFAULT = 0;
	public static final int SPRING_FESTIVAL = 1;
	
	//���������־����
	private final static String CALL = "<�ƺ�>";
	private final static String PERSON_TAG = "<���˱�ǩ>";
	private final static String GROUP_TAG = "<���ǩ>";
	
	
	static private String base;//���ɶ��Ż�������
	private String content;//��������
	private Person person;//��ϵ��
	
	public SmsEditor(Person c, int b)
	{
		person = c;
		switch(b)
		{
		case DEFAULT:break;
		case SPRING_FESTIVAL:base = CALL + "������֣�" + PERSON_TAG + "," + GROUP_TAG + "," + PERSON_TAG + "," + GROUP_TAG + "��";break;
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
	//�Զ����ɶ��Ų�����
	public String getContent()
	{
		//����base������Ӧ��������ģ��
		content = getBase();
		replaceCall();
		replacePersonTag();
		replaceGroupTag();
		return content;
	}
	//���ɳƺ�
	private void replaceCall()
	{
		String call = person.getName();
		call += "����";
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
