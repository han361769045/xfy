package com.zczczy.by.xfy.activities;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.zczczy.by.xfy.listener.TitleClickListener;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.Receiver;
import org.androidannotations.annotations.ReceiverAction;

/**
 * Created by zczczy on 2015/10/20.
 */
@EFragment
public abstract class BaseFragment extends Fragment implements TitleClickListener {
    //接口路径
    protected String rootUrl = "http://192.168.0.198:7890/";



    @Receiver(actions = "notify_changed")
    public void reciverB(){
        if(isVisible()){
            changeBB();
        }
    }

    public  abstract void changeBB();


    //返回
    @Override
    public void backClick() {
    }

    //其他点击
    @Override
    public void otherClick() {
    }

    /**
     * 检查当前网络是否可用
     */
    public boolean isNetworkAvailable(Activity activity) {
        Context context = activity.getApplicationContext();
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        } else {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
                    System.out.println(i + "===状态===" + networkInfo[i].getState());
                    System.out.println(i + "===类型===" + networkInfo[i].getTypeName());
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }



    @Override
    public void onPause() {
        super.onPause();
        if (isVisible()){
            onHiddenChanged(isVisible());
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (isVisible()){
            onHiddenChanged(!isVisible());
        }
    }
}
