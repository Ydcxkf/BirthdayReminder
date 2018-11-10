package com.example;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.ServiceCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.util.Log;

import java.util.Calendar;
import java.util.List;

public class MyService extends Service{
    private MYSQLiteOpenHelper dbHelper;
    Calendar c;
    String month,day,time;
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public void onCreate() {
        dbHelper = new MYSQLiteOpenHelper(this, "Birthday1.db", null, 1);
        super.onCreate();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        c = Calendar.getInstance();
        month = String.valueOf(c.get(Calendar.MONTH));
        day = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
        time = month + "月" + day + "日";
        Log.d("MyService:",time);
        int n=0;
        Cursor cursor1 = db.query("Birthday",null,null,null, null, null, null);
        Cursor cursor2 = db.query("activity", null,null, null, null, null, null);
        if (cursor1.moveToFirst()) {
            do {
                String name = cursor1.getString(cursor1.getColumnIndex("name"));
                String time1 = cursor1.getString(cursor1.getColumnIndex("notetime"));
                String time2 = cursor1.getString(cursor1.getColumnIndex("notifytime"));
                String realy1 = cursor1.getString(cursor1.getColumnIndex("ifnote"));
                String realy2 = cursor1.getString(cursor1.getColumnIndex("ifnotify"));
                if (time1.equals(time) && realy1.equals("true")) {
                    String note1 = "已经给" + name + "发送短信成功";
                    String note2 = "已经给" + name + "发送短信失败";
                    int i = 0;
                    if (cursor2.moveToFirst()) {//判断是否发送过短信
                        do {
                            time1 = cursor2.getString(cursor2.getColumnIndex("time"));
                            String activity = cursor2.getString(cursor2.getColumnIndex("activity"));
                            if (time1.equals(time) && (activity.equals(note1)||activity.equals(note2))) {
                                i++;
                                break;
                            }
                        } while (cursor2.moveToNext());
                    }
                    if (i == 0) {//没有就发送短信
                        String phone = cursor1.getString(cursor1.getColumnIndex("phoneNo"));
                        String message = cursor1.getString(cursor1.getColumnIndex("note"));
                        ContentValues values = new ContentValues();
                        if (sendShortMessage(phone, message)) {
                            values.put("activity", note1);
                        } else {
                            values.put("activity", note2);
                        }
                        values.put("time", time);
                        db.insert("activity", null, values);
                    }
                }
                if (time2.equals(time) && realy2.equals("true")) {
                    int i = 0;
                    String note1 = "已经给" + name + "发送通知成功";
                    String note2 = "已经给" + name + "发送通知失败";
                    if (cursor2.moveToFirst()) {
                        do {
                            time1 = cursor2.getString(cursor2.getColumnIndex("time"));
                            String activity = cursor2.getString(cursor2.getColumnIndex("activity"));
                            if (time1.equals(time) && activity.equals(note1)) {
                                i++;
                                break;
                            }
                        } while (cursor2.moveToNext());
                    }
                    if (i == 0) {
                        ContentValues values = new ContentValues();
                        String title = "生日提醒：";
                        String text = name + "将在于" + time + "过生日";
                        Log.d("MyService","n="+n);
                        if (sendnotification(n, title, text)) {
                            values.put("activity", note1);
                        } else {
                            values.put("activity", note2);
                        }

                        values.put("time", time);
                        db.insert("activity", null, values);
                    }
                }
                n++;
            } while (cursor1.moveToNext());
        }
        dbHelper.close();
        cursor1.close();
        cursor2.close();
        stopSelf();
    }
    @Override
    public int onStartCommand(Intent intent,int flags,int startId){
        return  super.onStartCommand(intent,flags,startId);
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
    }
    /*发送短信的函数，phongNo是发送号码，message是发送信息*/
    protected boolean sendShortMessage(String phoneNo,String message) {
        /*if (ContextCompat.checkSelfPermission(MyService.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ServiceCompat.requestPermissions(MyService.this, new String[]{Manifest.permission.SEND_SMS}, 1);
        }*/
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, message, null, null);
            List<String> sms = smsManager.divideMessage(message);
            for (String smslist :sms){
                smsManager.sendTextMessage(phoneNo,null,smslist,null,null);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    /*发送通知的函数，id1是通知的id,不重复就行，title是标题,text是消息*/
    protected boolean sendnotification(int id1,String title,String text){
        try{
            Intent intent = new Intent(this,MainActivity.class);//转到的页面/活动
            Log.d("MyService","id="+id1);
            PendingIntent pi= PendingIntent.getActivity(this,0,intent,0);
            String id = "my_channel_01";
            String name="渠道名字";
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            Notification notification = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel mChannel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_LOW);
                notificationManager.createNotificationChannel(mChannel);
                notification = new Notification.Builder(this)
                        .setChannelId(id)
                        .setContentTitle(title)
                        .setStyle(new Notification.BigTextStyle().bigText(text))
                        .setSmallIcon(R.mipmap.ic_launcher)//通知的小图标
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.happy_birthday))//通知的大图标
                        .setContentIntent(pi)
                        .setAutoCancel(true)
                        .build();
            } else {
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                        .setContentTitle(title)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(text))
                        .setSmallIcon(R.mipmap.ic_launcher)//通知的小图标
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.happy_birthday))//通知的大图标
                        .setOngoing(true)
                        .setContentIntent(pi)
                        .setAutoCancel(true);
                notification = notificationBuilder.build();
            }
            notificationManager.notify(id1, notification);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
