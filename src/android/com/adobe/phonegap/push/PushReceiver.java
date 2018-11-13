package com.adobe.phonegap.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.google.firebase.messaging.RemoteMessage;

public class PushReceiver extends BroadcastReceiver implements PushConstants {

    @Override
    public void onReceive(Context context, Intent intent) {
        RemoteMessage message = intent.getParcelableExtra("message");
        Intent pIntent = new Intent(context, FCMService.class);
        pIntent.putExtra("message", message);
        context.startService(pIntent);
    }
}
