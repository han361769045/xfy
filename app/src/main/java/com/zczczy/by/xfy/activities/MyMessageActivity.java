package com.zczczy.by.xfy.activities;

import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.zczczy.by.xfy.MyApplication;
import com.zczczy.by.xfy.R;
import com.zczczy.by.xfy.adapters.FireInfoAdapter;
import com.zczczy.by.xfy.adapters.MyBaseAdapter;
import com.zczczy.by.xfy.adapters.MyMsgAdapter;
import com.zczczy.by.xfy.dao.MessageDao;
import com.zczczy.by.xfy.dao.PushMessage;
import com.zczczy.by.xfy.model.FireInfo;
import com.zczczy.by.xfy.model.PushMsg;
import com.zczczy.by.xfy.prefs.MyPrefs_;
import com.zczczy.by.xfy.pullableview.PullToRefreshLayout;
import com.zczczy.by.xfy.pullableview.PullableListView;
import com.zczczy.by.xfy.rest.MyErrorHandler;
import com.zczczy.by.xfy.rest.MyRestClient;
import com.zczczy.by.xfy.tools.AndroidTool;
import com.zczczy.by.xfy.viewgroup.BadgeView;
import com.zczczy.by.xfy.viewgroup.MyTitleView;
import com.zczczy.by.xfy.viewgroup.NoDataView;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.Receiver;
import org.androidannotations.annotations.ReceiverAction;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.WindowFeature;
import org.androidannotations.annotations.res.StringRes;
import org.androidannotations.annotations.rest.RestService;
import org.androidannotations.annotations.sharedpreferences.Pref;

/**
 * Created by zczczy on 2015/10/21.
 * 我的消息
 */
@WindowFeature(value = {Window.FEATURE_NO_TITLE, Window.FEATURE_INDETERMINATE_PROGRESS})
@EActivity(R.layout.mymessage_layout)
public class MyMessageActivity extends BaseActivity{

    @StringRes
    String txt_mymessage;

    @ViewById
    MyTitleView title;

    @ViewById
    NoDataView no_data;

    @ViewById
    ListView lv_list;

    @Pref
    MyPrefs_ pre;


    String flag ="0";

    @Bean(MyMsgAdapter.class)
    MyBaseAdapter<PushMessage> myAdapter;

    @Bean
    MessageDao messageDao;

    @AfterViews
    void afterView() {
        title.setListener(this);
        title.setTitle(txt_mymessage);
        lv_list.setAdapter(myAdapter);
        myAdapter.getMoreData(0, 1000, pre.username().get());
        //
    }
    @Receiver(actions = "notify_changed")
    public void reciverB(Intent intent){
        PushMessage pushMessage = (PushMessage) intent.getExtras().get("pushMessage");
        myAdapter.getList().add(0,pushMessage);
        myAdapter.notifyDataSetChanged();
    }


    //item点击事件
    @ItemClick
    void  lv_list(PushMessage pushMessage){
        pushMessage.setReadStatus("1");
        messageDao.updateMessage(pushMessage);
        if(isNetworkAvailable(this)){
            MsgDetailsActivity_.intent(this).pushMessage(pushMessage).startForResult(1000);
        }else{
            Toast.makeText(this, no_net, Toast.LENGTH_SHORT).show();
        }
    }

    @OnActivityResult(1000)
    public void OnResult(){
        myAdapter.notifyDataSetChanged();
    }



}
