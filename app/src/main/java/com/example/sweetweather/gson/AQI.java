package com.example.sweetweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 *		"aqi": {
 * 			"city": {
 * 				"aqi": "83",
 * 				"pm25": "61",
 * 				"qlty": "良"
 *                        }* 		},
 *
 */

public class AQI {
   @SerializedName("city")
   public AQIcity aqIcity;
   
   public class  AQIcity{
      public String aqi;
      public String pm25;
   }

}
