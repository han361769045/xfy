package com.zczczy.by.xfy.rest;

import android.content.Context;

import com.zczczy.by.xfy.tools.AndroidTool;
import com.zczczy.by.xfy.tools.L;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.res.StringRes;
import org.androidannotations.api.rest.RestErrorHandler;
import org.springframework.core.NestedRuntimeException;

/**
 * 异常类
 * Created by Leo on 2015/3/9.
 */
@EBean
public class MyErrorHandler implements RestErrorHandler {

    @RootContext
    Context context;

    @StringRes
    String no_net;

    @Override
    public void onRestClientExceptionThrown(NestedRuntimeException arg0) {
        // TODO Auto-generated method stub
        L.e("=================", arg0.getMessage());

        //开启 线程运行 否者报错
        showTos();
    }

    @Background
    void showTos(){
        afterShowTos();
    }
    @UiThread
    void afterShowTos(){
        AndroidTool.showToast(context, no_net);
    }

}
