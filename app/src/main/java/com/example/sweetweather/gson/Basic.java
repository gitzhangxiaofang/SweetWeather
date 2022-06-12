package com.example.sweetweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * 		"basic": {
 * 			"cid": "CN101190407",
 * 			"location": "吴江",
 * 			"parent_city": "苏州",
 * 			"admin_area": "江苏",
 * 			"cnty": "中国",
 * 			"lat": "30.5843544",
 * 			"lon": "114.29856873",
 * 			"tz": "+8.00",
 * 			"city": "吴江",
 * 			"id": "CN101190407",
 * 			"update": {
 * 				"loc": "2022-06-09 14:09",
 * 				"utc": "2022-06-09 14:09"
 *                        }* 		},
 *
 */
public class Basic {
    @SerializedName("cid")
    public String weatherid;
   @SerializedName("city")
   public String cityName;

   public Update update;
   public class Update{

      @SerializedName("loc")
      public String updateTime;
   }










}
