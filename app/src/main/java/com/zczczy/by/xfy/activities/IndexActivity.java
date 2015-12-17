package com.zczczy.by.xfy.activities;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.zczczy.by.xfy.R;
import com.zczczy.by.xfy.dao.MessageDao;
import com.zczczy.by.xfy.model.BaseModelJson;
import com.zczczy.by.xfy.model.PagerResult;
import com.zczczy.by.xfy.model.PushMsg;
import com.zczczy.by.xfy.prefs.MyPrefs_;
import com.zczczy.by.xfy.rest.MyErrorHandler;
import com.zczczy.by.xfy.rest.MyRestClient;
import com.zczczy.by.xfy.tools.AndroidTool;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.res.StringRes;
import org.androidannotations.annotations.rest.RestService;
import org.androidannotations.annotations.sharedpreferences.Pref;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by zczczy on 2015/11/18.
 */
@EActivity(R.layout.index_layout)
public class IndexActivity extends Activity {

    @RestService
    MyRestClient myRestClient;

    @Bean
    MessageDao messageDao;
    @Pref
    MyPrefs_ pre;
    @StringRes
    String no_net;
    @Bean
    MyErrorHandler myErrorHandler;

    @AfterInject
    void afterInject(){
        myRestClient.setRestErrorHandler(myErrorHandler);
    }

    @AfterViews
    void  afterView(){
        if (isNetworkAvailable(this)) {
            Get();
        }else{
            AndroidTool.showToast(this,no_net);
            finish();
        }
    }

    @Background()
    void Get(){
        if (!"".equals(pre.token().get())&&pre.token().get()!=null){
            myRestClient.setHeader("Token",pre.token().get());
            BaseModelJson<PagerResult<PushMsg>> bmj =myRestClient.GetPushMessage(pre.username().get(), 0, 10000);
            afterHolding(bmj);
        }else{
            afterD();
        }
    }

    @UiThread
    void afterHolding( BaseModelJson<PagerResult<PushMsg>> bmj){
        if(bmj!=null&&bmj.Successful){
            messageDao.insertOrUpdate(bmj.Data.ListData);
        }
        MainActivity_.intent(this).start();
        finish();
    }
    @UiThread
    void afterD(){
        MainActivity_.intent(this).start();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

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
}
