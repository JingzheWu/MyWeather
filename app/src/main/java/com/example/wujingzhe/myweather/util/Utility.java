package com.example.wujingzhe.myweather.util;

import android.text.TextUtils;

import com.example.wujingzhe.myweather.model.City;
import com.example.wujingzhe.myweather.model.County;
import com.example.wujingzhe.myweather.model.MyWeatherDB;
import com.example.wujingzhe.myweather.model.Province;

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

}
