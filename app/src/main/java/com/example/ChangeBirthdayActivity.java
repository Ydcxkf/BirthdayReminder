package com.example;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ChangeBirthdayActivity extends AppCompatActivity implements View.OnClickListener {
    private Button bn1;
    private Button bn2;
    private EditText ed_name;
    private EditText ed_age;
    private EditText ed_month;
    private EditText ed_day;
    private EditText ed_phone;
    private MYSQLiteOpenHelper dbHelper;
    int id=0;
    String name,age,time,phone,month,day;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_birthday);
        bn1=(Button)findViewById(R.id.finish_button);
        bn1.setOnClickListener(this);
        bn2=(Button)findViewById(R.id.forgo_button);
        bn2.setOnClickListener(this);
        ed_name = (EditText) findViewById(R.id.name_edit);
        ed_age = (EditText) findViewById(R.id.age_edit);
        ed_month = (EditText) findViewById(R.id.month_edit);
        ed_day = (EditText)findViewById(R.id.day_edit);
        ed_phone=(EditText)findViewById(R.id.phone_edit);
        Intent intent=getIntent();
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
                    time = cursor.getString(cursor.getColumnIndex("birthday"));
                    age = cursor.getString(cursor.getColumnIndex("age"));
                    phone = cursor.getString(cursor.getColumnIndex("phoneNo"));
                    break;
                }
                i++;
            }while(cursor.moveToNext());
        }
        cursor.close();
        month=time.substring(0, time.indexOf('月'));
        day=time.substring(time.indexOf('月')+1, time.indexOf('日'));
        ed_name.setText(name);
        ed_age.setText(age);
        ed_phone.setText(phone);
        ed_month.setText(month);
        ed_day.setText(day);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.finish_button:
                name= ed_name.getText().toString();
                String birthday=ed_month.getText().toString()+"月"+ed_day.getText().toString()+"日";
                String phoneNo=ed_phone.getText().toString();
                int age=Integer.parseInt(ed_age.getText().toString());
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                ContentValues values = new ContentValues();
                values.put("name",name);
                values.put("birthday",birthday);
                values.put("phoneNo",phoneNo);
                values.put("age",age);
                values.put("notetime",birthday);
                values.put("notifytime",birthday);
                values.put("note","祝您生日快乐");
                values.put("ifnote","false");
                values.put("ifnotify","false");
                db.update("Birthday",values,"phoneNo = ?",new String[]{phone});
                dbHelper.close();
                Intent intent1=new Intent(ChangeBirthdayActivity.this,MainActivity.class);
                startActivity(intent1);
                break;
            case R.id.forgo_button:
                Intent intent2=new Intent(ChangeBirthdayActivity.this,MainActivity.class);
                startActivity(intent2);
                break;
            default :
        }
    }
}
