package com.zczczy.by.xfy.finder;

import android.content.Context;
import android.net.ConnectivityManager;

import com.zczczy.by.xfy.model.BaseModelJson;
import com.zczczy.by.xfy.model.FireInfo;
import com.zczczy.by.xfy.model.Insp;
import com.zczczy.by.xfy.model.PagerResult;
import com.zczczy.by.xfy.prefs.MyPrefs_;
import com.zczczy.by.xfy.rest.MyRestClient;
import com.zczczy.by.xfy.tools.AndroidTool;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.SystemService;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.res.StringRes;
import org.androidannotations.annotations.rest.RestService;
import org.androidannotations.annotations.sharedpreferences.Pref;

/**
 * Created by zczczy on 2015/11/12.
 */
@EBean
public class FireInfoFinder extends  BaseFinder<FireInfo> {
    @RestService
    MyRestClient myRestClient;

    @Pref
    MyPrefs_ pre;

    @SystemService
    ConnectivityManager connectivityManager;

    @RootContext
    Context context;

    @StringRes
    String no_net;



    @Override
    @Background
    public void pagingQuery(int pageIndex, int pageSize, Object... objects) {

        myRestClient.setHeader("Token",pre.token().get());

        if(AndroidTool.isNetworkAvailable(connectivityManager)){
            BaseModelJson<PagerResult<FireInfo>> bmj =myRestClient.GetBusinessList((objects[0]).toString(), pageIndex, pageSize, (objects[1]).toString(), (objects[2]).toString());
            afterPagingQuery(bmj);
        }else{
            afterPagingQuery(null);
        }

    }

    @Override
    @UiThread
    public void afterPagingQuery(BaseModelJson<PagerResult<FireInfo>> bmj) {

        if(bmj!=null&&bmj.Successful){
            getList().addAll(bmj.Data.ListData);
            setTotal(bmj.Data.PageCount);
            listener.receive(0);
        }else if(bmj==null){
            AndroidTool.showToast(context,no_net);
            AndroidTool.dismissLoadDialog();
        } else{
            listener.receive(0);
        }
    }
}
