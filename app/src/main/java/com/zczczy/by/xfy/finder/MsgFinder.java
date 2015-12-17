package com.zczczy.by.xfy.finder;

import com.zczczy.by.xfy.model.BaseModelJson;
import com.zczczy.by.xfy.model.Insp;
import com.zczczy.by.xfy.model.PagerResult;
import com.zczczy.by.xfy.model.PushMsg;
import com.zczczy.by.xfy.prefs.MyPrefs_;
import com.zczczy.by.xfy.rest.MyRestClient;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.rest.RestService;
import org.androidannotations.annotations.sharedpreferences.Pref;

/**
 * Created by zczczy on 2015/11/17.
 */
@EBean
public class MsgFinder extends  BaseFinder<PushMsg> {
    @RestService
    MyRestClient myRestClient;

    @Pref
    MyPrefs_ pre;

    @Override
    @Background
    public void pagingQuery(int pageIndex, int pageSize, Object... objects) {

        myRestClient.setHeader("Token",pre.token().get());
        BaseModelJson<PagerResult<PushMsg>> bmj =myRestClient.GetPushMessage((objects[0]).toString(), pageIndex, pageSize);
        afterPagingQuery(bmj);
    }

    @Override
    @UiThread
    public void afterPagingQuery(BaseModelJson<PagerResult<PushMsg>> bmj) {

        if(bmj!=null&&bmj.Successful){
            getList().addAll(bmj.Data.ListData);
            setTotal(bmj.Data.PageCount);
            listener.receive(0);
        }else{
            listener.receive(0);
        }
    }
}
