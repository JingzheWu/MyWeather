package com.example.wujingzhe.myweather.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SearchRecentSuggestionsProvider;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.example.wujingzhe.myweather.model.City;
import com.example.wujingzhe.myweather.model.County;
import com.example.wujingzhe.myweather.model.MyWeatherDB;
import com.example.wujingzhe.myweather.model.Province;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.Locale;

/**
 * Created by wujingzhe on 2016/10/3.
 */

/**
 * 服务器数据处理类
 */
public class Utility {

     //解析处理服务器返回的Province数据
     public synchronized static boolean handleProvinceResponse(MyWeatherDB myWeatherDB,String response){
         if(!TextUtils.isEmpty(response)){
             String[] allProvinces=response.split(",");
             if (allProvinces!=null&&allProvinces.length>0){
                 for(String p:allProvinces){
                     String[] array=p.split("\\|");
                     Province province=new Province();
                     province.setProvinceCode(array[0]);
                     province.setProvinceName(array[1]);
                     myWeatherDB.saveProvince(province);//将解析出来的信息存储到Province表
                 }
                 return true;
             }
         }
         return false;
     }

    //解析处理服务器返回的City数据
    public synchronized static boolean handleCityResponse(MyWeatherDB myWeatherDB,String response,
                                                          int provinceId){
        if (!TextUtils.isEmpty(response)){
            String[] allCities=response.split(",");
            if(allCities!=null&&allCities.length>0){
                for (String c:allCities){
                    String[] array=c.split("\\|");
                    City city=new City();
                    city.setCityCode(array[0]);
                    city.setCityName(array[1]);
                    city.setProvinceId(provinceId);
                    myWeatherDB.saveCity(city);//将解析出来的信息存储到City表
                }
                return true;
            }
        }
        return  false;
    }

    //解析处理服务器返回的County数据
    public synchronized static boolean handleCountiesResponse(MyWeatherDB myWeatherDB,String response,
                                                              int cityId){
        if(!TextUtils.isEmpty(response)){
            String[] allCounties=response.split(",");
            if(allCounties!=null&&allCounties.length>0){
                for(String c:allCounties){
                    String[] array=c.split("\\|");
                    County county=new County();
                    county.setCountyCode(array[0]);
                    county.setCountyName(array[1]);
                    county.setCityId(cityId);
                    myWeatherDB.saveCounty(county);//将解析出来的信息存储到City表
                }
                return true;
            }
        }
        return false;
    }

    //解析服务器返回的JSON数据
    public static void handleWeatherResponse(Context context, String response){
        try{
            JSONObject jsonObject=new JSONObject(response);
            JSONObject weatherInfo=jsonObject.getJSONObject("weatherinfo");
            String cityName=weatherInfo.getString("city");
            String weatherCode=weatherInfo.getString("cityid");
            String temp1=weatherInfo.getString("temp1");
            String temp2=weatherInfo.getString("temp2");
            String weatherDesp=weatherInfo.getString("weather");
            String publishTime=weatherInfo.getString("ptime");
            saveWeatherInfo(context,cityName,weatherCode,temp1,temp2,weatherDesp,publishTime);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    //将解析后的JSON数据存储到SharePreferences文件中
    @TargetApi(Build.VERSION_CODES.N)
    public static void saveWeatherInfo(Context context, String cityName, String weatherCode, String temp1,
                                       String temp2, String weatherDesp, String publishTime){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy年M月d日",Locale.CHINA);
        SharedPreferences.Editor editor= PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean("city_selected",true);
        editor.putString("city_name",cityName);
        editor.putString("weather_code",weatherCode);
        editor.putString("temp1",temp1);
        editor.putString("temp2",temp2);
        editor.putString("weather_desp",weatherDesp);
        editor.putString("publish_time",publishTime);
        editor.putString("current_date",sdf.format(new Date()));
        editor.commit();
    }

}
