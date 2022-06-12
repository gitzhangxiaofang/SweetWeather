package com.example.sweetweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * 		"daily_forecast": [{
 * 			"date": "2022-06-10",
 * 			"cond": {
 * 				"txt_d": "多云"
 *                        },
 * 			"tmp": {
 * 				"max": "22",
 * 				"min": "10"
 *            }* 		}, {
 * 			"date": "2022-06-11",
 * 			"cond": {
 * 				"txt_d": "小雨"* 			},
 * 			"tmp": {
 * 				"max": "18",
 * 				"min": "8"
 *                    }
 * 		}, {
 * 			"date": "2022-06-12",
 * 			"cond": {
 * 				"txt_d            多云"
 * 			},
 * 			"tmp": {
 * 				"max": "19",
 * 				"mi            "6"
 *        }
 * 		}, {
 * 			"date": "2022-06-13",
 * 			"cond": {
 * 				"txt_d":            雨"
 * 			},
 * 			"tmp": {
 * 				"max": "16",
 * 				"min"            0"
 *        }
 * 		}, {
 * 			"date": "2022-06-14",
 * 			"cond": {
 * 				"txt_d":            "
 * 			},
 * 			"tmp": {
 * 				"max": "18",
 * 				"min":            "
 *        }
 * 		}, {
 * 			"date": "2022-06-15",
 * 			"cond": {
 * 				"txt_d": "多云"
 * 			},
 * 			"tmp": {
 * 				"max": "17",
 * 				"min": "8"
 *                    }
 * 		}],
 */


public class Forecast {


   @SerializedName("date")
   public String date;
   @SerializedName("cond")
   public Cond cond;
   @SerializedName("tmp")
   public Tmp tmp;


   public class Cond{
      @SerializedName("txt_d")
      public String info;

   }

   public class  Tmp{

      public String max;
      public String min;

   }


}
