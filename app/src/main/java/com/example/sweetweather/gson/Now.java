package com.example.sweetweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * 		"now": {
 * 			"cloud": "91",
 * 			"cond_code": "101",
 * 			"cond_txt": "多云",
 * 			"fl": "21",
 * 			"hum": "68",
 * 			"pcpn": "0.0",
 * 			"pres": "1012",
 * 			"tmp": "21",
 * 			"vis": "16",
 * 			"wind_deg": "281",
 * 			"wind_dir": "西北风",
 * 			"wind_sc": "2",
 * 			"wind_spd": "8",
 * 			"cond": {
 * 				"code": "101",
 * 				"txt": "多云"
 *                        }* 		},
 */
public class Now {

   @SerializedName("tmp")
   public String temperature;

   @SerializedName("cond")
   public More more;

   public class More {
      @SerializedName("txt")
      public String info;

   }

}
