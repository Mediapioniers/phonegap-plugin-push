package com.adobe.phonegap.push;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Paint;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.WearableExtender;
import android.support.v4.app.RemoteInput;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.security.SecureRandom;

@SuppressLint("NewApi")
public class FCMReceiver extends FirebaseMessagingService implements PushConstants {

  private static final String LOG_TAG = "Push_FCMReceiver";

  @Override
  public void onMessageReceived(RemoteMessage message) {
    Bundle extras = new Bundle();

    for (Map.Entry<String, String> entry : message.getData().entrySet()) {
      extras.putString(entry.getKey(), entry.getValue());
    }

    Log.v(LOG_TAG, "Received notification from API" + extras.toString());

    Context applicationContext = getApplicationContext();
    String customReceivers = extras.getString("receiver");
    if (customReceivers != null) {
      try {
        JSONArray receiverArray = new JSONArray(customReceivers);
        for (int i = 0; i < receiverArray.length(); i++) {
          JSONObject receiver = receiverArray.getJSONObject(i);
          Log.v(LOG_TAG, "Received data: " + receiver);

          // This allows use to dynamically start a service from a push.
          String serviceClass = receiver.optString("service", "cordova.plugin.mobilea.push.handler.CallService");

          Intent service = new Intent();
          service.setClassName(applicationContext, serviceClass);
          service.putExtra("message", message);
          service.setAction(receiver.getString("action"));

          Log.v(LOG_TAG, "Starting service '" + serviceClass + "'");
          applicationContext.startService(service);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    } else {
      Intent intent = new Intent();
      intent.setPackage(applicationContext.getPackageName());
      intent.putExtra("message", message);
      intent.setAction(BROADCAST_NOTIFICATION);
      Log.v(LOG_TAG, "Sending default broadcast");
      sendBroadcast(intent);
    }
  }

  @Override
  public void onNewToken(String s) {
    super.onNewToken(s);
    Log.d("NEW_TOKEN", s);
  }

}
