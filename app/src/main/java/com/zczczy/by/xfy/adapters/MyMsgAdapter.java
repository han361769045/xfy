package com.zczczy.by.xfy.adapters;


import android.content.Context;

import com.zczczy.by.xfy.activities.ContactsFragment;
import com.zczczy.by.xfy.activities.MyMessageActivity;
import com.zczczy.by.xfy.dao.MessageDao;
import com.zczczy.by.xfy.dao.PushMessage;
import com.zczczy.by.xfy.finder.BaseFinder;
import com.zczczy.by.xfy.finder.InspFinder;
import com.zczczy.by.xfy.finder.MsgFinder;
import com.zczczy.by.xfy.items.InspItemView_;
import com.zczczy.by.xfy.items.ItemView;
import com.zczczy.by.xfy.items.MsgItemView;
import com.zczczy.by.xfy.items.MsgItemView_;
import com.zczczy.by.xfy.listener.DataReceiveListener;
import com.zczczy.by.xfy.model.Insp;
import com.zczczy.by.xfy.model.PushMsg;
import com.zczczy.by.xfy.tools.AndroidTool;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.FragmentByTag;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.UiThread;


/**
 * Created by zczczy on 2015/11/17.
 */
@EBean
public class MyMsgAdapter extends  MyBaseAdapter<PushMessage>  {

    @RootContext
    Context context;

    @RootContext
    MyMessageActivity myMessageActivity;

    @Bean
    MessageDao messageDao;


    @Override
    @Background
    public void getMoreData(int pageIndex, int pageSize, Object... objects) {
        setList(messageDao.getList());
        afterGetData();
    }

    @UiThread
    void afterGetData(){
        notifyDataSetChanged();
    }


    @Override
    protected ItemView<PushMessage> getItemView(Context context) {
        return MsgItemView_.build(context);
    }


}
