package com.example.wujingzhe.myweather.util;

/**
 * Created by wujingzhe on 2016/10/3.
 */
public interface HttpCallbackListener {
    void onFinish(String response);
    void onError(Exception e);
}
