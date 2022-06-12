package com.example.sweetweather;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.sweetweather.gson.Forecast;
import com.example.sweetweather.gson.Now;
import com.example.sweetweather.gson.Weather;
import com.example.sweetweather.util.HttpUtil;
import com.example.sweetweather.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {
   private ScrollView weatherLayout;
   private TextView titleCity;
   private TextView titleUpdateTime;
   private TextView degreeText;
   private TextView weatherInfoText;
   private LinearLayout forecastLayout;
   private TextView aqiText;
   private  TextView pm25Text;
   private TextView comfortText;
   private TextView carWashText;
   private TextView sportText;
   private static final String TAG = "WeatherActivity";
   private ImageView bingPicImg;

   @Override
   protected void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      if (Build.VERSION.SDK_INT >= 21){
         View decorView = getWindow().getDecorView();
         decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
         getWindow().setStatusBarColor(Color.TRANSPARENT);
      }
      setContentView(R.layout.activity_weather);
      //初始化各种控件
      weatherLayout = findViewById(R.id.weather_layout);
      titleCity =findViewById(R.id.title_city);
      titleUpdateTime = findViewById(R.id.title_update_time);
      degreeText = findViewById(R.id.degree_text);
      weatherInfoText = findViewById(R.id.wether_info_text);
      forecastLayout = findViewById(R.id.forecast_layout);
      aqiText = findViewById(R.id.aqi_text);
      pm25Text = findViewById(R.id.pm25_text);
      comfortText = findViewById(R.id.comfort_text);
      carWashText =findViewById(R.id.car_wash_text);
      sportText =findViewById(R.id.sport_text);
      bingPicImg  =findViewById(R.id.bing_pic_img);

      SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
      String weatherString = preferences.getString("weather",null);
      String bingPic =preferences.getString("bing_pic",null);
      if (bingPic !=null){
         Glide.with(this).load(bingPic).into(bingPicImg);
      }else {
         loadBingPic();
      }

      if(weatherString != null){
         //有缓存时直接解析天气数据
         Weather weather = Utility.handleWeatherResponse(weatherString);
         showWeatherInfo(weather);
      }else {
         //无缓存时去服务器查询天气
         String weatherid = getIntent().getStringExtra("weather_id");
      //   weatherLayout.setVisibility(View.INVISIBLE);
         requestWeather(weatherid);
      }
   }

   /**
    * 下载bing背景图
    */

   public  void loadBingPic(){
      String requestBingPic = "http://guolin.tech/api/bing_pic";
      HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
         @Override
         public void onFailure(Call call, IOException e) {
            e.printStackTrace();
         }

         @Override
         public void onResponse(Call call, Response response) throws IOException {

            final String bingPic =response.body().string();
            SharedPreferences.Editor editor =PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
            editor.putString("bing_pic",bingPic);
            editor.apply();

            runOnUiThread(new Runnable() {
               @Override
               public void run() {
                  Glide.with(WeatherActivity.this).load(bingPic).into(bingPicImg);
               }
            });
         }
      });

   }

   /**
    * 根据天气id请求天气城市信息
    */
   public void requestWeather(final  String weatherId){
      String weatherUrl = "http://guolin.tech/api/weather?cityid="+weatherId+"&key=80fb2eb48f794167bec433c2fd58131c";
      HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
         @Override
         public void onFailure(Call call, IOException e) {
            e.printStackTrace();
            runOnUiThread(new Runnable() {
               @Override
               public void run() {
                  Toast.makeText(WeatherActivity.this,"onFailure获取天气信息失败",Toast.LENGTH_SHORT).show();
               }
            });
         }

         @Override
         public void onResponse(Call call, Response response) throws IOException {
            Log.d(TAG, "onResponse: "+weatherUrl);
            final String responeText =response.body().string();
            final Weather weather = Utility.handleWeatherResponse(responeText);
            runOnUiThread(new Runnable() {
               @Override
               public void run() {
                  if(weather != null && "ok".equals(weather.status)){
                     SharedPreferences.Editor editor =PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                     editor.putString("weather",responeText);
                     editor.apply();
                     showWeatherInfo(weather);
                  }else {
                     Toast.makeText(WeatherActivity.this,"获取天气信息失败",Toast.LENGTH_SHORT).show();
                  }
               }
            });
         }
      });
   loadBingPic();
   }



   /**
    * 处理并展示Weather实体类得到数据
    */
   private void showWeatherInfo(Weather weather){
      String cityName = weather.basic.cityName;
      String updateTime = weather.basic.update.updateTime.split(" ")[1];
      String degree = weather.now.temperature+"˚C";
      String weatherInfo = weather.now.more.info;
      titleCity.setText(cityName);
      titleUpdateTime.setText(updateTime);
      degreeText.setText(degree);
      weatherInfoText.setText(weatherInfo);
      forecastLayout.removeAllViews();
      for (Forecast forecast  :weather.forecastList
      ) {
         View view = LayoutInflater.from(this).inflate(R.layout.forecast_item,forecastLayout,false);
         TextView dataText =view.findViewById(R.id.data_text);
         TextView infoText = view.findViewById(R.id.info_text);
         TextView maxText = view.findViewById(R.id.max_text);
         TextView minText = view.findViewById(R.id.min_text);
         dataText.setText(forecast.date);
         infoText.setText(forecast.cond.info);
         maxText.setText(forecast.tmp.max);
         minText.setText(forecast.tmp.min);
         forecastLayout.addView(view);
      }
      Log.d(TAG, "showWeatherInfo:  "+weather.aqi.aqIcity.toString());

      if (weather.aqi !=null){
         Log.d(TAG, "showWeatherInfo: "+ weather.aqi.aqIcity.aqi+"  "+weather.aqi.aqIcity.pm25);
         Log.d(TAG, "showWeatherInfo: "+aqiText+"   "+pm25Text);
         aqiText.setText(weather.aqi.aqIcity.aqi);
         pm25Text.setText(weather.aqi.aqIcity.pm25);
      }
      String comfort ="舒适度："+weather.suggestion.comf.txt;
      String carwash = "洗车指数： "+weather.suggestion.cw.txt;
      String sport ="运动建议： "+weather.suggestion.sport.txt;
      comfortText.setText(comfort);
      carWashText.setText(carwash);
      sportText.setText(sport);
      weatherLayout.setVisibility(View.VISIBLE);
   }
}
