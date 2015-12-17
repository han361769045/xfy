package com.zczczy.by.xfy.rest;

import android.content.Context;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

/**
 * Created by leo on 2015/6/2.
 */
@EBean
public class MyRequestFactory extends SimpleClientHttpRequestFactory {

    @RootContext
    Context context;

    @AfterInject
    void afterInject(){
        this.setConnectTimeout(30 * 1000);
        this.setReadTimeout(30 * 1000);

    }
}


