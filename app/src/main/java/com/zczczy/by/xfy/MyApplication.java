package com.zczczy.by.xfy;

import android.app.Application;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EApplication;

import cn.jpush.android.api.JPushInterface;


/**
 * Created by zczczy on 2015/11/12.
 */
@EApplication
public class MyApplication extends Application {


    @AfterInject
    void  afterView(){
        JPushInterface.init(this);
    }



}
