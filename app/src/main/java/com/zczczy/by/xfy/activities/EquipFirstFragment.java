package com.zczczy.by.xfy.activities;

import android.widget.TextView;
import android.widget.Toast;

import com.zczczy.by.xfy.R;
import com.zczczy.by.xfy.model.BaseModelJson;
import com.zczczy.by.xfy.model.Equip;
import com.zczczy.by.xfy.prefs.MyPrefs_;
import com.zczczy.by.xfy.rest.MyErrorHandler;
import com.zczczy.by.xfy.rest.MyRestClient;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;
import org.androidannotations.annotations.rest.RestService;
import org.androidannotations.annotations.sharedpreferences.Pref;

/**
 * Created by zczczy on 2015/11/16.
 * 通讯设备
 */
@EFragment(R.layout.equipfirst_layout)
public class EquipFirstFragment extends  BaseFragment{
    @ViewById
    TextView txt_msgnum,txt_num,txt_manufacturer,txt_type,txt_made_date,txt_check_date,txt_check,txt_state,txt_remark,txt_sim;
    @StringRes
    String no_net;

    @Pref
    MyPrefs_ pre;

    @RestService
    MyRestClient myRestClient;
    @Bean
    MyErrorHandler myErrorHandler;

    @AfterViews
    void afterView() {
        getHttp();
    }

    @Background
    void  getHttp(){
        myRestClient.setHeader("Token",pre.token().get());
        BaseModelJson<Equip> bmj = myRestClient.GetDeviceInfo();
        setBind(bmj);
    }
    @UiThread
    void setBind(BaseModelJson<Equip> bmj) {
        if (bmj.Successful) {
            txt_msgnum.setText(bmj.Data.M_D_msg_num);
            txt_num.setText(bmj.Data.M_D_num);
            txt_manufacturer.setText(bmj.Data.M_D_manufacturer);
            txt_type.setText(bmj.Data.M_D_type);
            txt_made_date.setText(bmj.Data.M_D_made_date);
            txt_check_date.setText(bmj.Data.M_D_check_date);
            txt_check.setText(bmj.Data.M_D_check);
            txt_state.setText(bmj.Data.M_D_state);
            txt_sim.setText(bmj.Data.R_SIM_num);
        }
        else{
            Toast.makeText(getActivity(), no_net, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void changeBB() {

    }
}
