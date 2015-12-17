package com.zczczy.by.xfy.activities;

import android.widget.TextView;
import android.widget.Toast;

import com.zczczy.by.xfy.R;
import com.zczczy.by.xfy.model.BaseModelJson;
import com.zczczy.by.xfy.model.FireInfo;
import com.zczczy.by.xfy.prefs.MyPrefs_;
import com.zczczy.by.xfy.rest.MyErrorHandler;
import com.zczczy.by.xfy.rest.MyRestClient;
import com.zczczy.by.xfy.viewgroup.MyTitleView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;
import org.androidannotations.annotations.sharedpreferences.Pref;

/**
 * Created by zczczy on 2015/11/20.
 *  故障详情
 */
@EActivity(R.layout.fault_detail_layout)
public class FaultDetailActivity extends  BaseActivity {
    @ViewById
    MyTitleView title;

    @ViewById
    TextView company,company_address,jq_desc,f_num,bj_time,solve_time,f_desc,solve_state,solve_result;

    @RestService
    MyRestClient myRestClient;
    @Bean
    MyErrorHandler myErrorHandler;
    @Pref
    MyPrefs_ pre;
    @Extra
    Integer A_ID;

    @AfterViews
    void afterView() {
        title.setListener(this);
        title.setTitle("故障详情");
        getHttp();
    }

    @Background
    void  getHttp(){

        myRestClient.setHeader("Token",pre.token().get());
        BaseModelJson<FireInfo> bmj = myRestClient.GetBusiness(A_ID);
        setBind(bmj);
    }
    @UiThread
    void setBind(BaseModelJson<FireInfo> bmj) {
        if (bmj.Successful) {
            company.setText(bmj.Data.B_unit_name);
            company_address.setText(pre.C_address().get());
            if (bmj.Data.A_desc!=null){
                jq_desc.setText(bmj.Data.A_desc);
            }else {
                jq_desc.setText("监控室报警");
            }
            f_num.setText(bmj.Data.A_Num);
            bj_time.setText(bmj.Data.A_Time.replace("/","-"));
            if (bmj.Data.f_desc!=null){
                f_desc.setText(bmj.Data.f_desc);
            }else {
                f_desc.setText("监控室报警");
            }
            if (bmj.Data.A_R_Time!=null){
                solve_time.setText(bmj.Data.A_R_Time.replace("/","-"));
                solve_state.setText("已处理");
            }
            else {
                solve_state.setText("未处理");
                solve_time.setText("");
            }
            solve_result.setText(bmj.Data.A_Result);

        }
        else{
            Toast.makeText(this, no_net, Toast.LENGTH_SHORT).show();
        }
    }
}
