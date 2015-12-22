package com.zczczy.by.xfy.activities;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zczczy.by.xfy.MyApplication;
import com.zczczy.by.xfy.R;
import com.zczczy.by.xfy.dao.MessageDao;
import com.zczczy.by.xfy.rest.MyErrorHandler;
import com.zczczy.by.xfy.rest.MyRestClient;
import com.zczczy.by.xfy.tools.AndroidTool;
import com.zczczy.by.xfy.tools.L;
import com.zczczy.by.xfy.viewgroup.BadgeView;
import com.zczczy.by.xfy.viewgroup.MyTitleLeftView;
import com.zczczy.by.xfy.viewgroup.MyTitleView;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;
import org.androidannotations.annotations.rest.RestService;

/**
 * Created by zczczy on 2015/10/20.
 */
@EFragment(R.layout.home_layout)
public class HomeFragment extends  BaseFragment {
    @ViewById
    MyTitleLeftView title;
    @ViewById
    ImageView btn_online,btn_joinin;
    @StringRes
    String txt_home;
    @ViewById
    LinearLayout img_back;

    @ViewById
    ImageView txt_phone;
    @Bean
    MessageDao messageDao;
    @StringRes
    String no_net;

    @RestService
    MyRestClient myRestClient;
    @Bean
    MyErrorHandler myErrorHandler;
    @AfterInject
    void afterInject(){
        myRestClient.setRestErrorHandler(myErrorHandler);
    }


    //添加圆点
    BadgeView badgeView ;

    @AfterViews
    void afterView() {
        title.setListener(this);
        title.setTitle(txt_home);
        title.setImg_other();
        badgeView = new BadgeView(this.getActivity(),img_back) ;
        badgeView.setBadgePosition(BadgeView.POSITION_TOP_LEFT);
        changeBB();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden){
            changeBB();
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

    @Click
    public void txt_phone(){
        System.out.println("asdfas");
        Intent intent=new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "400-10-86867"));
        startActivity(intent);
    }


    @Click
    void img_back() {
        if (isNetworkAvailable(getActivity())) {
            PersonalCenterActivity_.intent(this).start();}
        else {
            Toast.makeText(getActivity(), no_net, Toast.LENGTH_SHORT).show();
        }
    }

    @Click
    void btn_online() {
        if (isNetworkAvailable(getActivity())) {
            OnlineApplyActivity_.intent(this).start();
        }else {
            Toast.makeText(getActivity(), no_net, Toast.LENGTH_SHORT).show();
        }
    }

    @Click
    void btn_joinin() {
        if (isNetworkAvailable(getActivity())) {
            JoiningAgentActivity_.intent(this).start();}
        else {
            Toast.makeText(getActivity(), no_net, Toast.LENGTH_SHORT).show();
        }
    }
}
