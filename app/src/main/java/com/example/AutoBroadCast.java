package com.example;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AutoBroadCast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent data) {
// 开机启动服务
        Intent service = new Intent(context, MyService.class);
        context.startService(service);
    }
}
