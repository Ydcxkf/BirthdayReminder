package com.example;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class AddActivity extends AppCompatActivity implements View.OnClickListener{
    private Button bn1;
    private Button bn2;
    private EditText ed_name;
    private EditText ed_age;
    private EditText ed_month;
    private EditText ed_day;
    private EditText ed_phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        bn1=(Button)findViewById(R.id.finish_button);
        bn1.setOnClickListener(this);
        bn2=(Button)findViewById(R.id.forgo_button);
        bn2.setOnClickListener(this);
        ed_name = (EditText) findViewById(R.id.name_edit);
        ed_age = (EditText) findViewById(R.id.age_edit);
        ed_month = (EditText) findViewById(R.id.month_edit);
        ed_day = (EditText)findViewById(R.id.day_edit);
        ed_phone=(EditText)findViewById(R.id.phone_edit);
    }
    @Override
    public void onClick(View v) {
        Intent intent=new Intent(AddActivity.this,MainActivity.class);
        switch (v.getId()){
            case R.id.finish_button:
                MYSQLiteOpenHelper dbHelper;
                dbHelper = new MYSQLiteOpenHelper(this,"Birthday1.db",null,1);
                String name= ed_name.getText().toString();
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
                db.insert("Birthday",null,values);
                dbHelper.close();
                startActivity(intent);
                break;
            case R.id.forgo_button:
                startActivity(intent);
                break;
            default :
        }
    }
}
