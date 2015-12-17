package com.zczczy.by.xfy.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.inputmethod.InputMethodManager;

import com.zczczy.by.xfy.listener.TitleClickListener;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.res.StringRes;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by zczczy on 2015/10/20.
 */
@EBean
public class BaseActivity  extends Activity implements TitleClickListener {

    @StringRes
    String no_net;


    //定义一个广播接收器
    protected BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    };

    //关闭标识，用来关闭指定的Activity
    protected String close_flag="";



    // 返回
    @Override
    public void backClick() {
        closeInputMethod(this);
        finish();

    }

    // 其他点击
    @Override
    public void otherClick() {
        // TODO Auto-generated method stub
    }
    /**
     * 检查当前网络是否可用
     */
    public boolean isNetworkAvailable(Activity activity)
    {
        Context context = activity.getApplicationContext();
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null)
        {
            return false;
        }
        else
        {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0)
            {
                for (int i = 0; i < networkInfo.length; i++)
                {
                    System.out.println(i + "===状态===" + networkInfo[i].getState());
                    System.out.println(i + "===类型===" + networkInfo[i].getTypeName());
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    //隐藏软键盘
    void closeInputMethod(Activity activity){
        /*隐藏软键盘*/
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if(inputMethodManager.isActive()){
            if(activity.getCurrentFocus()!=null){
                inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
            }
        }
    }




    @Override
    protected void onResume(){
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(close_flag);
        JPushInterface.onResume(this);
        this.registerReceiver(broadcastReceiver, filter);
    }
    @Override
    protected void onPause(){
        super.onPause();
        JPushInterface.onPause(this);

    }


    @Override
    protected  void onDestroy(){
        super.onDestroy();
        this.unregisterReceiver(broadcastReceiver);

    }

    protected void myExit() {
        Intent intent = new Intent();
        intent.setAction(close_flag);
        this.sendBroadcast(intent);
        super.finish();
    }
}
