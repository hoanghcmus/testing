package com.hoangvnit.stackoverflow.base;

import android.app.Application;

import com.hoangvnit.stackoverflow.rest.RestClient;

/**
 * Bootstrap of the application
 *
 * @author Nguyen Ngoc Hoang (www.hoangvnit.com)
 */
public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        RestClient.getInstance().init(this);
    }
}
