package com.zczczy.by.xfy.activities;

import android.view.Window;
import android.widget.TextView;

import com.zczczy.by.xfy.R;
import com.zczczy.by.xfy.prefs.MyPrefs_;
import com.zczczy.by.xfy.viewgroup.MyTitleView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.WindowFeature;
import org.androidannotations.annotations.sharedpreferences.Pref;

/**
 * Created by zczczy on 2015/11/17.
 * 巡检详情页
 */
@EActivity(R.layout.check_detail_layout)
@WindowFeature(value = {Window.FEATURE_NO_TITLE, Window.FEATURE_INDETERMINATE_PROGRESS})
public class CheckDetailActivity  extends  BaseActivity{
    @ViewById
    MyTitleView title;
    @ViewById
    TextView company,company_address,solve_time,check_time,solve_result;

    @Pref
    MyPrefs_ pre;

    @Extra
    String s_time,c_time,s_result;

    @AfterViews
    void afterView() {
        title.setListener(this);
        title.setTitle("查岗详情");
        company.setText(pre.C_name().get());
        company_address.setText(pre.C_address().get());
        solve_time.setText(s_time.replace("/","-"));
        check_time.setText(c_time.replace("/","-"));
        solve_result.setText(s_result);
    }


}
