package com.zczczy.by.xfy.listener;

/**
 * Created by Leo on 2015/3/9.
 */
public interface PhotoCallBack {

    void Success(String path);

    void Failed(String errormsg);
}
