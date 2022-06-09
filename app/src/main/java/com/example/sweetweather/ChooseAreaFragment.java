package com.example.sweetweather;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sweetweather.db.City;
import com.example.sweetweather.db.County;
import com.example.sweetweather.db.Province;
import com.example.sweetweather.util.HttpUtil;
import com.example.sweetweather.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.FileDescriptor;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ChooseAreaFragment  extends Fragment {

    private static final String TAG = "ChooseAreaFragment";
   public static final  int LEVEL_PROVINCE =0;
   public static final int  LEVEL_CITY=1;
   public static final int LEVEL_COUNTY = 2;

   private ProgressDialog progressDialog;
   private TextView textView;
   private Button backButton;
   private ListView listView;
   private ArrayAdapter<String>  adapter;
   private List<String> dataList =new ArrayList<>();
   /**
    * 省市县列表
    */
   private List<Province> provinceList;
    private List<City> cityList;
    private List<County> countyList;
   /**
    * 选中的省份、选中的城市、选中的级别
    */
   private  Province selectedProvince;
   private  City   selectedCity;
   private  int currentLevel;

   @Override
   public void onActivityCreated(@Nullable Bundle savedInstanceState) {
      super.onActivityCreated(savedInstanceState);
      listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
          @Override
          public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
              if(currentLevel ==LEVEL_PROVINCE){
                  selectedProvince = provinceList.get(i);
                  queryCities();
              }else if(currentLevel ==LEVEL_CITY){
                  selectedCity =cityList.get(i);
                  queryCounties();
              }

          }
      });
      backButton.setOnClickListener(view -> {
          if (currentLevel == LEVEL_COUNTY){
               queryCities();
          }else if(currentLevel ==LEVEL_CITY){
               queryProvnices();
          }
      });
       queryProvnices();
   }




   @Nullable
   @Override
   public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

       View view = inflater.inflate(R.layout.choose_area,container,false);
       textView =view.findViewById(R.id.title_text);
       backButton = view.findViewById(R.id.back_button);
       listView = view.findViewById(R.id.list_view);
       adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1,dataList);
       listView.setAdapter(adapter);
       return view;
   }

    /**
     * 查询全国的省，优先从数据库查，数据库没有再去服务器上查询
     */
   private  void queryProvnices(){

       textView.setText("中国");
       backButton.setVisibility(View.GONE);
       provinceList = DataSupport.findAll(Province.class);
       if(provinceList.size()>0){
           dataList.clear();
           for (Province provnice:provinceList
                ) {
               dataList.add(provnice.getProvinceName());
           }
           adapter.notifyDataSetChanged();
           listView.setSelection(0);
           currentLevel = LEVEL_PROVINCE;
       }else{
           String adress = "http://guolin.tech/api/china";
           queryFromSever(adress,"province");
       }
   }

    /**
     * 查询市区
     */

    private  void queryCities(){
        textView.setText(selectedProvince.getProvinceName());
        backButton.setVisibility(View.VISIBLE);
        cityList =DataSupport.where("provinceid = ?",String.valueOf(selectedProvince.getId())).find(City.class);
        if(cityList.size()>0){
            dataList.clear();
            for (City city:cityList
                 ) {
                dataList.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel =LEVEL_CITY;
        }else {
            int proinceCode = selectedProvince.getProvinceCode();
            String address = "http://guolin.tech/api/china/"+proinceCode;
            queryFromSever(address,"city");
        }

    }
    /**
     *
     * 查询县
     */
    private  void queryCounties(){
        textView.setText(selectedCity.getCityName());
        backButton.setVisibility(View.VISIBLE);
        countyList  =DataSupport.where("cityid=?",String.valueOf(selectedCity.getId())).find(County.class);
        if (countyList.size()>0){
            dataList.clear();
            for (County county:countyList
                 ) {
                dataList.add(county.getCountyName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel =LEVEL_COUNTY;
        }else {
            int province = selectedProvince.getProvinceCode();
            int city =selectedCity.getCityCode();
            String adress ="http://guolin.tech/api/china/"+province+"/"+city;
            queryFromSever(adress,"county");
        }

    }

    /**
     * 根据传入的地址和类型从服务器上查询省市县数据
     */
    private void queryFromSever(String address,final String type){
        Log.d(TAG, "queryFromSever: type  "+type);
        showProgressDialog();
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            //失败
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                //t通过runOnUiThread（）方法回到主线成处理逻辑
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(getContext(),"加载失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String responseText = response.body().string();
                boolean result =false;
                if("province".equals(type)){
                    result = Utility.handleProvinceResponse(responseText);
                }else if ("city".equals(type)){
                    result =Utility.handleCityResponse(responseText,selectedProvince.getId());
                }else if("county".equals(type)){
                    result =Utility.handleCountyResponse(responseText,selectedCity.getId());
                }

                if(result){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if ("province".equals(type)){
                                queryProvnices();
                            }else if("city".equals(type)){
                                queryCities();
                            }else if("county".equals(type)){
                                queryCounties();
                            }
                        }
                    });
                }
            }
        });

    }

    private void showProgressDialog(){
        if(progressDialog ==null){
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("正在加载。。。");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    private void closeProgressDialog(){
        if(progressDialog != null){
            progressDialog.dismiss();
        }
    }


}
