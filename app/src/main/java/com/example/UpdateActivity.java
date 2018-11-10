package com.example;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class UpdateActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView textView;
    private Button button1;
    private Button button2;
    private Button button3;
    String name,age,time0,time1,time2,phone,note,ifnote,ifnotify,sum;
    int id=0;
    //使用的数据库
    private MYSQLiteOpenHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        textView=(TextView)findViewById(R.id.update_textview);
        button1=(Button)findViewById(R.id.back_button);
        button2=(Button)findViewById(R.id.changeBirthday_button);
        button3=(Button)findViewById(R.id.changeNote_button);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        Intent intent=getIntent();;
        String tmp = intent.getStringExtra("id");
        id = Integer.valueOf(tmp);
        dbHelper = new MYSQLiteOpenHelper(this,"Birthday1.db",null,1);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("Birthday",null,null,null,null,null,null);
        if (cursor.moveToFirst()){
            int i=0;
            do{
                if(i == id){
                    name = cursor.getString(cursor.getColumnIndex("name"));
                    time0 = cursor.getString(cursor.getColumnIndex("birthday"));
                    age = cursor.getString(cursor.getColumnIndex("age"));
                    phone = cursor.getString(cursor.getColumnIndex("phoneNo"));
                    note = cursor.getString(cursor.getColumnIndex("note"));
                    time1 = cursor.getString(cursor.getColumnIndex("notetime"));
                    time2 = cursor.getString(cursor.getColumnIndex("notifytime"));
                    ifnote = cursor.getString(cursor.getColumnIndex("ifnote"));
                    ifnotify = cursor.getString(cursor.getColumnIndex("ifnotify"));
                    break;
                }
                i++;
            }while(cursor.moveToNext());
        }
        cursor.close();
        sum="姓名："+name+"\n生日："+time0+"\n年龄："+age+"\n电话："+phone;
        Log.d("IF:",ifnote);
        if(ifnote.equals("true")){
            sum=sum+"\n短信内容："+note+"\n短信发送时间："+time1;
        }
        Log.d("IF:",ifnotify);
        if(ifnotify.equals("true")){
            sum=sum+"\n提醒发送时间："+time2;
        }
        Log.d("A:","OK");
        textView.setText(sum);
        dbHelper.close();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_button:
                Intent intent1=new Intent(UpdateActivity.this,MainActivity.class);
                startActivity(intent1);
                break;
            case R.id.changeBirthday_button:
                Intent intent2=new Intent(UpdateActivity.this,ChangeBirthdayActivity.class);
                intent2.putExtra("id",String.valueOf(id));
                startActivity(intent2);
                break;
            case R.id.changeNote_button:
                Log.d("A123","OK");
                Intent intent3=new Intent(UpdateActivity.this,ChangeNotifyActivity.class);
                intent3.putExtra("id",String.valueOf(id));
                startActivity(intent3);
                break;
            default :
        }
    }
}
