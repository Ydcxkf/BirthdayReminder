package com.example;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ToggleButton;

public class ChangeNotifyActivity extends AppCompatActivity implements View.OnClickListener
    {
    private Button bn1;
    private Button bn2;
    private EditText ed_note;
    private EditText ed_month1;
    private EditText ed_day1;
    private EditText ed_month2;
    private EditText ed_day2;
    private MYSQLiteOpenHelper dbHelper;
    int id=0;
    String note,time1,time2,ifnote,ifnotify,month,day,phone;
    private ToggleButton toggle1,toggle2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_notify);
        bn1=(Button)findViewById(R.id.finish_button);
        bn1.setOnClickListener(this);
        bn2=(Button)findViewById(R.id.forgo_button);
        bn2.setOnClickListener(this);
        ed_note = (EditText) findViewById(R.id.note_edit);
        ed_month1 = (EditText) findViewById(R.id.month1_edit);
        ed_day1 = (EditText)findViewById(R.id.day1_edit);
        ed_month2 = (EditText) findViewById(R.id.month2_edit);
        ed_day2 = (EditText)findViewById(R.id.day2_edit);
        toggle1=(ToggleButton)findViewById(R.id.note_togglebutton);
        toggle2=(ToggleButton)findViewById(R.id.notify_togglebutton);
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
                    note = cursor.getString(cursor.getColumnIndex("note"));
                    time1 = cursor.getString(cursor.getColumnIndex("notetime"));
                    time2 = cursor.getString(cursor.getColumnIndex("notifytime"));
                    ifnote = cursor.getString(cursor.getColumnIndex("ifnote"));
                    ifnotify = cursor.getString(cursor.getColumnIndex("ifnotify"));
                    phone = cursor.getString(cursor.getColumnIndex("phoneNo"));
                    break;
                }
                i++;
            }while(cursor.moveToNext());
        }
        cursor.close();
        ed_note.setText(note);
        month=time1.substring(0, time1.indexOf('月'));
        day=time1.substring(time1.indexOf('月')+1, time1.indexOf('日'));
        ed_month1.setText(month);
        ed_day1.setText(day);
        month=time1.substring(0, time2.indexOf('月'));
        day=time2.substring(time2.indexOf('月')+1, time2.indexOf('日'));
        ed_month2.setText(month);
        ed_day2.setText(day);
        Log.d("ChangeNotify:","ifnote="+ifnote);
        Log.d("ChangeNotify:","ifnotify="+ifnotify);
        Log.d("ChangeNotify:","ifnote="+Boolean.parseBoolean(ifnote));
        Log.d("ChangeNotify:","ifnotify="+Boolean.parseBoolean(ifnotify));

        toggle1.setChecked(Boolean.getBoolean(ifnote));

        toggle2.setChecked(Boolean.getBoolean(ifnotify));
        toggle1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!buttonView.isPressed())return;
                if(isChecked)
                    ifnote="true";
                else
                    ifnote="false";
            }
        });
        toggle2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!buttonView.isPressed())return;
                if(isChecked)
                    ifnotify="true";
                else
                    ifnotify="false";
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.finish_button:
                note= ed_note.getText().toString();
                time1=ed_month1.getText().toString()+"月"+ed_day1.getText().toString()+"日";
                time2=ed_month2.getText().toString()+"月"+ed_day2.getText().toString()+"日";
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                ContentValues values = new ContentValues();
                values.put("note",note);
                values.put("notetime",time1);
                values.put("notifytime",time2);
                values.put("ifnotify",ifnotify);
                values.put("ifnote",ifnote);
                Log.d("happy",phone);
                db.update("Birthday",values,"phoneNo = ?",new String[]{phone});
                dbHelper.close();
                Intent intent1=new Intent(ChangeNotifyActivity.this,MainActivity.class);
                startActivity(intent1);
                break;
            case R.id.forgo_button:
                Intent intent2=new Intent(ChangeNotifyActivity.this,MainActivity.class);
                startActivity(intent2);
                break;
            default :
        }
    }
}
