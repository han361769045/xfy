package com.zczczy.by.xfy.adapters;

import android.content.Context;

import com.zczczy.by.xfy.activities.MessageFragment;
import com.zczczy.by.xfy.activities.SeatFragment;
import com.zczczy.by.xfy.finder.BaseFinder;
import com.zczczy.by.xfy.finder.FireInfoFinder;
import com.zczczy.by.xfy.items.FireInfoItemView;
import com.zczczy.by.xfy.items.FireInfoItemView_;
import com.zczczy.by.xfy.items.ItemView;
import com.zczczy.by.xfy.listener.DataReceiveListener;
import com.zczczy.by.xfy.model.FireInfo;
import com.zczczy.by.xfy.tools.AndroidTool;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.FragmentByTag;
import org.androidannotations.annotations.RootContext;

/**
 * Created by zczczy on 2015/11/12.
 * 火警信息
 */

@EBean
public class FireInfoAdapter extends MyBaseAdapter<FireInfo>  implements DataReceiveListener {

    @RootContext
    Context context;

    @Bean(FireInfoFinder.class)
    BaseFinder<FireInfo> finder;

    @FragmentByTag
    MessageFragment messageFragment;

    @FragmentByTag
    SeatFragment seatFragment;

    @AfterInject
    void initData(){
        finder.setDataReceiveListener(this);
        setTotal(finder.getTotal());
        setList(finder.getList());
    }

    @Override
    public void getMoreData(int pageIndex, int pageSize, Object... objects) {

        AndroidTool.showLoadDialog(context);

        if(pageIndex==1){
            finder.cleanListAndCount();
            notifyDataSetChanged();
        }
        finder.pagingQuery(pageIndex, pageSize, objects);
    }

    @Override
    protected ItemView<FireInfo> getItemView(Context context) {
        return FireInfoItemView_.build(context);
    }

    @Override
    public void receive(int type) {

        setTotal(finder.getTotal());

        setList(finder.getList());

        notifyDataSetChanged();

        if(messageFragment!=null){

            messageFragment.changeLoadTime();
        }
        if(seatFragment!=null){

            seatFragment.changeLoadTime();
        }
    }



}
