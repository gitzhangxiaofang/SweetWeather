package com.example.sweetweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * 		"suggestion": {
 * 			"comf": {
 * 				"type": "comf",
 * 				"brf": "舒适",
 * 				"txt": "白天不太热也不太冷，风力不大，相信您在这样的天气条件下，应会感到比较清爽和舒适。"
 *                        },
 * 			"sport": {
 * 				"type": "sport",
 * 				"brf": "较适宜",
 * 				"txt": "天气较好，但因风力稍强，户外可选择对风力要求不高的运动，推荐您进行室内运动。"
 *            },
 * 			"cw": {
 * 				"type": "cw",
 * 				"brf": "较适宜",
 * 				"txt": "较适宜洗车，未来一天无雨，风力较小，擦洗一新的汽车至少能保持一天。"
 *            }* 		},
 */
public class Suggestion {

   @SerializedName("comf")
   public Comf comf;
   @SerializedName("sport")
   public Sport sport;
   @SerializedName("cw")
   public Cw cw;




   public class  Comf{
      @SerializedName("txt")
      public String txt;
   }

   public class  Cw{
      @SerializedName("txt")
      public String txt;
   }

   public class  Sport{
      @SerializedName("txt")
      public String txt;
   }



}
