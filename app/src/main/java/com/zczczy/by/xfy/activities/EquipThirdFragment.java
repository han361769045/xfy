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
 * 系统信息
 */
@EFragment(R.layout.equipthird_layout)
public class EquipThirdFragment extends  BaseFragment {

    @StringRes
    String no_net;

    @ViewById
    TextView txt_manufacturer,txt_type,txt_points,txt_state,txt_crt,
            txt_display,txt_dis_type,txt_area,txt_printer,txt_printer_type,
            txt_printer_state,txt_other,txt_explain,txt_hookup,
            txt_alarm_out,txt_trouble_out,txt_relationship;

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
          txt_manufacturer.setText(bmj.Data.U_manufacturer);
            txt_type.setText(bmj.Data.U_type);
            txt_points.setText(bmj.Data.U_points);
            txt_state.setText(bmj.Data.U_state);
            txt_crt.setText(bmj.Data.U_CRT);
            txt_display.setText(bmj.Data.U_display);
            txt_dis_type.setText(bmj.Data.U_display_type);
            txt_area.setText(bmj.Data.U_area);
            txt_printer.setText(bmj.Data.U_printer);
            txt_printer_type.setText(bmj.Data.U_printer_type);
            txt_printer_state.setText(bmj.Data.U_printer_state);
            txt_other.setText(bmj.Data.U_other);
            txt_explain.setText(bmj.Data.U_explain);
            txt_hookup.setText(bmj.Data.U_hookup);
            txt_alarm_out.setText(bmj.Data.U_alarm_out);
            txt_trouble_out.setText(bmj.Data.U_trouble_out);
            txt_relationship.setText(bmj.Data.U_relationship);
        }
        else{
            Toast.makeText(getActivity(), no_net, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void changeBB() {

    }
}
