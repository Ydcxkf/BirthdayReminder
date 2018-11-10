package com.example;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class MYSQLiteOpenHelper extends SQLiteOpenHelper{
    public static final String CREATE_Birthday= "create table Birthday ("
            +"name text,"
            +"age int,"
            +"phoneNo text,"
            +"birthday text,"
            +"note text,"
            +"notetime text,"
            +"notifytime text,"
            +"ifnote text,"
            +"ifnotify text)";
    public static final String CREATE_Activity= "create table activity ("
            +"activity text,"
            +"time text)";
    private Context mContext;
    public MYSQLiteOpenHelper(Context context, String name,
                              SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
        mContext=context;
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_Birthday);
        db.execSQL(CREATE_Activity);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){
    }
}
