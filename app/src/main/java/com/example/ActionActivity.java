package com.example;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ActionActivity extends AppCompatActivity{
    private List<String> list= new ArrayList();//存数据
    private MYSQLiteOpenHelper dbHelper;
    private ListView listView = null;
    //用于存方列表的信息
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action);
        dbHelper = new MYSQLiteOpenHelper(this,"Birthday1.db",null,1);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("activity",null,null,null,null,null,null);
        if (cursor.moveToFirst()){
            do{
                String name = cursor.getString(cursor.getColumnIndex("activity"));
                String time = cursor.getString(cursor.getColumnIndex("time"));
                list.add(name+"\n"+time);
            }while(cursor.moveToNext());
        }
        cursor.close();
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                ActionActivity.this,android.R.layout.simple_list_item_1,list);
        listView = (ListView) findViewById(R.id.list_item);
        listView.setAdapter(adapter);
        dbHelper.close();
    }

}
