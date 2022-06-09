package com.example.sweetweather.util;

import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class HttpUtil {
   private static final String TAG = "HttpUtil";
   public static void sendOkHttpRequest(String address,okhttp3.Callback callback){
      OkHttpClient client =new OkHttpClient();
      Log.d(TAG, "sendOkHttpRequest:     "+address);
      Request request =new Request.Builder().url(address).build();

      client.newCall(request).enqueue(callback);
   }




}
