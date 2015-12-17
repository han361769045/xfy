package com.zczczy.by.xfy.adapters;

import android.content.Context;

import com.zczczy.by.xfy.MyApplication;
import com.zczczy.by.xfy.activities.ContactsFragment;
import com.zczczy.by.xfy.activities.MessageFragment;
import com.zczczy.by.xfy.activities.SeatFragment;
import com.zczczy.by.xfy.finder.BaseFinder;
import com.zczczy.by.xfy.finder.FireInfoFinder;
import com.zczczy.by.xfy.finder.InspFinder;
import com.zczczy.by.xfy.items.FireInfoItemView_;
import com.zczczy.by.xfy.items.InspItemView;
import com.zczczy.by.xfy.items.InspItemView_;
import com.zczczy.by.xfy.items.ItemView;
import com.zczczy.by.xfy.listener.DataReceiveListener;
import com.zczczy.by.xfy.model.FireInfo;
import com.zczczy.by.xfy.model.Insp;
import com.zczczy.by.xfy.tools.AndroidTool;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.FragmentByTag;
import org.androidannotations.annotations.RootContext;

/**
 * Created by zczczy on 2015/11/13.
 */
@EBean
public class InspAdapter extends MyBaseAdapter<Insp>  implements DataReceiveListener {

    @RootContext
    Context context;

    @Bean(InspFinder.class)
    BaseFinder<Insp> finder;

    @FragmentByTag
    ContactsFragment contactsFragment;



    @AfterInject
    void initData(){
        finder.setDataReceiveListener(this);
        setTotal(finder.getTotal());
        setList(finder.getList());

    }


    @Override
    public void getMoreData(int pageIndex, int pageSize, Object... objects) {
        type= (String) objects[1];
        AndroidTool.showLoadDialog(context);
        if(pageIndex==1){
            finder.cleanListAndCount();
            notifyDataSetChanged();
        }
        finder.pagingQuery(pageIndex, pageSize, objects);
    }

    @Override
    protected ItemView<Insp> getItemView(Context context) {
        return InspItemView_.build(context);
    }

    @Override
    public void receive(int type) {

        setTotal(finder.getTotal());

        setList(finder.getList());

        notifyDataSetChanged();



        contactsFragment.changeLoadTime();
    }


}
