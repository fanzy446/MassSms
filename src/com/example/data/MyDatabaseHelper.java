package com.example.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseHelper extends SQLiteOpenHelper
{

	public MyDatabaseHelper(Context context, String name,CursorFactory factory, int version)
	{
		super(context, name, factory, version);
	}


	public MyDatabaseHelper(Context context, String name)
	{
		super(context, name, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		System.out.print("create a Database");
		//db.execSQL("create table user(_id INTEGER PRIMARY KEY AUTOINCREMENT, personId int, name varchar(20), sex smallint)");
		//db.execSQL("create table userPhone(_id INTEGER PRIMARY KEY AUTOINCREMENT, personId int,phoneNumber varchar(20),phoneType int)");
		db.execSQL("create table userTag(_id INTEGER PRIMARY KEY AUTOINCREMENT,personId int, userTag varchar(20))");
		db.execSQL("create table userGroup(_id INTEGER PRIMARY KEY AUTOINCREMENT,personId int, groupId int)");
		db.execSQL("create table groupData(_id INTEGER PRIMARY KEY AUTOINCREMENT,groupId int, groupName varchar(20))");
		db.execSQL("create table groupTag(_id INTEGER PRIMARY KEY AUTOINCREMENT,groupId int,groupTag varchar(20))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		System.out.print("update a DataBase");
	}

}
