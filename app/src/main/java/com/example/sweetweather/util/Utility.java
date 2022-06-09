package com.example.sweetweather.util;

import android.text.TextUtils;

import com.example.sweetweather.db.City;
import com.example.sweetweather.db.County;
import com.example.sweetweather.db.Province;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Utility {
   /**
    * 解析和处理服务器返回省的数据
    */
   public  static boolean handleProvinceResponse(String response){
      if(!TextUtils.isEmpty(response)){
         try {
            JSONArray allProvince = new JSONArray(response);
            for (int i = 0; i < allProvince.length(); i++) {
               JSONObject provinceObject =allProvince.getJSONObject(i);
               Province province =new Province();
               province.setProvinceName(provinceObject.getString("name"));
               province.setProvinceCode(provinceObject.getInt("id"));
               province.save();
            }
            return true;
         } catch (JSONException e) {
            e.printStackTrace();
         }
      }
      return false;
   }
   /**
    * 处理返回市级数据
    */

   public  static boolean handleCityResponse(String response,int provinceId){
      if(!TextUtils.isEmpty(response)){
         try {
           JSONArray allCity = new JSONArray(response);
            for (int i = 0; i < allCity.length(); i++) {
               JSONObject jsonObject = allCity.getJSONObject(i);
               City city = new City();
               city.setCityCode(jsonObject.getInt("id"));
               city.setCityName(jsonObject.getString("name"));
               city.setProvinceId(provinceId);
               city.save();
            }
            return true;
         } catch (JSONException e) {
            e.printStackTrace();
         }
      }

      return false;
   }


   /**
    * 处理返回县级数据
    */
   public  static boolean handleCountyResponse(String response, int cityId){
      if(!TextUtils.isEmpty(response)){
         try {
               JSONArray jsonArray = new JSONArray(response);
            for (int i = 0; i < jsonArray.length(); i++) {
               JSONObject jsonObject = jsonArray.getJSONObject(i);
               County county = new County();
               county.setCountyName(jsonObject.getString("name"));
               county.setCityId(cityId);
               county.setWeatherId(jsonObject.getString("weather_id"));
               county.save();
            }
            return true;
         } catch (JSONException e) {
            e.printStackTrace();
         }
      }
      return false;
   }

}
