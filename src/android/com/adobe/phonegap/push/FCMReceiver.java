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

    String customReceiver = extras.get("receiver");
    if (customReceiver != null) {
      Object receiverData = extras.get(customReceiver);
      Log.v(LOG_TAG, "Received data type: " + receiverData.getClass());
      Log.v(LOG_TAG, "Received data: " + receiverData);
    }


    Log.d(LOG_TAG, "Received notification from API" + extras.toString());

    Context applicationContext = getApplicationContext();
    String packageName = applicationContext.getPackageName();

    Intent intent = new Intent();
    intent.setPackage(packageName);
    intent.setAction(BROADCAST_NOTIFICATION);
    intent.putExtra("data", message);

    Log.d(LOG_TAG, "Sending local broadcast");
    sendBroadcast(intent);
  }
}
