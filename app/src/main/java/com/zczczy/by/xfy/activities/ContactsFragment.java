package com.zczczy.by.xfy.activities;

import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zczczy.by.xfy.MyApplication;
import com.zczczy.by.xfy.R;

import com.zczczy.by.xfy.adapters.InspAdapter;
import com.zczczy.by.xfy.adapters.MyBaseAdapter;
import com.zczczy.by.xfy.dao.MessageDao;
import com.zczczy.by.xfy.model.BaseModelJson;
import com.zczczy.by.xfy.model.FireInfo;
import com.zczczy.by.xfy.model.Insp;
import com.zczczy.by.xfy.model.UserLogin;
import com.zczczy.by.xfy.prefs.MyPrefs_;
import com.zczczy.by.xfy.pullableview.PullToRefreshLayout;
import com.zczczy.by.xfy.pullableview.PullableListView;
import com.zczczy.by.xfy.rest.MyErrorHandler;
import com.zczczy.by.xfy.rest.MyRestClient;
import com.zczczy.by.xfy.tools.AndroidTool;
import com.zczczy.by.xfy.viewgroup.BadgeView;
import com.zczczy.by.xfy.viewgroup.MyAlertDialog;
import com.zczczy.by.xfy.viewgroup.MyTitleLeftView;
import com.zczczy.by.xfy.viewgroup.MyTitleView;
import com.zczczy.by.xfy.viewgroup.NoDataView;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;
import org.androidannotations.annotations.rest.RestService;
import org.androidannotations.annotations.sharedpreferences.Pref;

/**
 * Created by zczczy on 2015/10/21.
 * 巡检查岗
 */
@EFragment(R.layout.inspection_layout)
public class ContactsFragment extends  BaseFragment  implements PullToRefreshLayout.OnRefreshListener {

    @ViewById
    MyTitleLeftView title;
    @ViewById
    RadioGroup rg_rg;
    @ViewById
    RadioButton txt_online,txt_notonline;
    @ViewById
    LinearLayout ll_nosolve,ll_solve,img_back;

    @StringRes
    String txt_contact;
    @ViewById
    NoDataView no_data;

    @ViewById
    PullToRefreshLayout refresh_view;

    @ViewById
    RelativeLayout ps_msg;

    @ViewById
    PullableListView lv_list;

    @RestService
    MyRestClient myRestClient;

    @Pref
    MyPrefs_ pre;

    int pageIndex=1;

    public String flag ="1";


    @Bean(InspAdapter.class)
    MyBaseAdapter<Insp> myAdapter;

    @Bean
    MyErrorHandler myErrorHandler;
    @Bean
    MessageDao messageDao;

    @StringRes
    String no_net;
    //添加圆点
    BadgeView badgeView ;

    @AfterInject
    void afterInject() {
        myRestClient.setRestErrorHandler(myErrorHandler);
    }

    @AfterViews
    void afterView() {
        title.setListener(this);
        title.setTitle(txt_contact);
        refresh_view.setOnRefreshListener(this);
        lv_list.setAdapter(myAdapter);
        badgeView = new BadgeView(this.getActivity(),img_back);
        badgeView.setBadgePosition(BadgeView.POSITION_TOP_LEFT);

    }

    //个人中心
    @Click
    void img_back() {
        if(isNetworkAvailable(this.getActivity())){
            PersonalCenterActivity_.intent(this).start();}
        else {
            Toast.makeText(getActivity(), no_net, Toast.LENGTH_SHORT).show();
        }
    }

    //列表点击进入详情
    @ItemClick
    void  lv_list(Insp insp){
        if(isNetworkAvailable(this.getActivity())){
            CheckDetailActivity_.intent(getActivity()).s_time(insp.c_send_time).c_time(insp.c_time).s_result(insp.c_result).start();}
        else{
            Toast.makeText(this.getActivity(), no_net, Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden){
            changeBB();
            String tonken = pre.token().get();
            if (tonken == null || tonken == "" || tonken.isEmpty()) {
                myAdapter.getList().clear();
                myAdapter.notifyDataSetChanged();
                ((MainActivity)getActivity()).setTabSelection(0);
            } else {
                myAdapter.getMoreData(pageIndex, 10, pre.C_name().get(),flag);
            }
        }

    }

    @Override
    public  void changeBB(){
        badgeView.hide();
        long i =messageDao.getStatus();
        if (i>0){
            badgeView.setText(i+"");
            badgeView.setTextSize(10);
            badgeView.show();
        }
    }


    //在岗
    @CheckedChange
    void txt_online(boolean checked){
        String tonken = pre.token().get();
        if (tonken != null && tonken != "" && !tonken.isEmpty()){
            if(isNetworkAvailable(this.getActivity())){
                pageIndex=1;
                if(checked){
                    flag="1";
                    myAdapter.getMoreData(pageIndex, 10, pre.C_name().get(),flag);
                    ll_nosolve.setVisibility(View.VISIBLE);
                }else {
                    flag="0";
                    ll_nosolve.setVisibility(View.GONE);
                }}}
        else {
            Toast.makeText(getActivity(), no_net, Toast.LENGTH_SHORT).show();
        }
    }
    //未在岗
    @CheckedChange
    void txt_notonline(boolean checked){

            if(isNetworkAvailable(this.getActivity())) {
                String tonken = pre.token().get();
                if (tonken != null && tonken != "" && !tonken.isEmpty()){
                pageIndex = 1;
                if (checked) {
                    flag = "0";

                    myAdapter.getMoreData(pageIndex, 10, pre.C_name().get(),flag);
                    ll_solve.setVisibility(View.VISIBLE);

                } else {
                    flag = "1";
                    ll_solve.setVisibility(View.GONE);
                }
            }
        }
        else {
            Toast.makeText(getActivity(), no_net, Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        String tonken = pre.token().get();
        if (tonken != null && tonken != "" && !tonken.isEmpty()) {
            pageIndex = 1;
            myAdapter.getMoreData(pageIndex, 10, pre.C_name().get(), flag);
        }
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        if(myAdapter.getList().size()>=myAdapter.getTotal()){
            AndroidTool.showToast(this.getActivity(), "没有更多数据了");
            changeLoadTime();
        }else{
            pageIndex++;
            myAdapter.getMoreData(pageIndex, 10,pre.C_name().get(),flag);
        }

    }

    public void changeLoadTime(){
        if (pageIndex == 1) {
            refresh_view.refreshFinish(PullToRefreshLayout.SUCCEED);
        } else {
            refresh_view.loadmoreFinish(PullToRefreshLayout.SUCCEED);
        }
        if(myAdapter.getCount()==0){
            no_data.setVisibility(View.VISIBLE);
        }else{
            no_data.setVisibility(View.GONE);
        }
        AndroidTool.dismissLoadDialog();
    }


}
