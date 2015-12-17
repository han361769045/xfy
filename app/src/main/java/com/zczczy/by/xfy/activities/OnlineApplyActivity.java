package com.zczczy.by.xfy.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.zczczy.by.xfy.R;
import com.zczczy.by.xfy.model.ApplyAccount;
import com.zczczy.by.xfy.model.BaseModelJson;
import com.zczczy.by.xfy.prefs.MyPrefs_;
import com.zczczy.by.xfy.rest.MyErrorHandler;
import com.zczczy.by.xfy.rest.MyRestClient;
import com.zczczy.by.xfy.tools.AndroidTool;
import com.zczczy.by.xfy.viewgroup.MyAlertDialog;
import com.zczczy.by.xfy.viewgroup.MyTitleView;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.WindowFeature;
import org.androidannotations.annotations.res.StringRes;
import org.androidannotations.annotations.rest.RestService;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zczczy on 2015/10/26.
 * 在线申请
 */
@WindowFeature(value = {Window.FEATURE_NO_TITLE, Window.FEATURE_INDETERMINATE_PROGRESS})
@EActivity(R.layout.online_layout)
public class OnlineApplyActivity extends  BaseActivity {

    private String company;
    private String city;
    private String person;
    private String tel;
    private String email;
    private String citycode;


    MyAlertDialog dialog;
    @ViewById
    MyTitleView title;
    @StringRes
    String txt_online;
    @ViewById
    EditText edt_company,edt_address,edt_person,edt_tel,edt_mail;
    @ViewById
    Button btn_register;
    @ViewById
    LinearLayout ll_area;

    @Pref
    MyPrefs_ pre;

    @RestService
    MyRestClient myRestClient;

    @Bean
    MyErrorHandler myErrorHandler;


    @AfterInject
    void afterInject() {
        myRestClient.setRestErrorHandler(myErrorHandler);
    }
    @AfterViews
    void afterView() {
        title.setListener(this);
        title.setTitle(txt_online);

    }
    @OnActivityResult(1000)
    void getBillId(int resultCode, Intent data) {
        if (resultCode == 1001) {
            Bundle bundle = data.getExtras();
            city = bundle.getString("cityname");
            citycode = bundle.getString("citycode");
            edt_address.setText(city);
        }
    }

    @Click
    void edt_address() {
        CityChooseActivity_.intent(this).flag(0).startForResult(1000);

    }

    @Click
    void btn_register(){
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if(inputMethodManager.isActive()){
            inputMethodManager.hideSoftInputFromWindow(OnlineApplyActivity.this.getCurrentFocus().getWindowToken(), 0);
        }
        if(isNetworkAvailable(this)){
            this.company=edt_company.getText().toString();
            this.city = edt_address.getText().toString();
            this.person=edt_person.getText().toString();
            this.tel=edt_tel.getText().toString();
            this.email=edt_mail.getText().toString();

            if(company==""||company==null||company.isEmpty()){
                MyAlertDialog dialog = new MyAlertDialog(this,"公司名称不能为空",null);
                dialog.show();
                dialog.setCanceledOnTouchOutside(false);
                return;
            }

            String regEx="^[A-Za-z0-9\\u4E00-\\u9FA5_-]+$";
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(company);
             if (!m.matches()){
                MyAlertDialog dialog = new MyAlertDialog(this,"公司名称格式错误",null);
                dialog.show();
                dialog.setCanceledOnTouchOutside(false);
                return;
            }

            if(city==""||city==null||city.isEmpty()){
                MyAlertDialog dialog = new MyAlertDialog(this,"公司所在地不能为空",null);
                dialog.show();
                dialog.setCanceledOnTouchOutside(false);
                return;
            }

            if(person==""||person==null||person.isEmpty()){
                MyAlertDialog dialog = new MyAlertDialog(this,"联系人不能为空",null);
                dialog.show();
                dialog.setCanceledOnTouchOutside(false);
                return;
            }
            String contact="^[A-Za-z0-9\\u4E00-\\u9FA5_-]+$";
            Pattern c = Pattern.compile(contact);
            Matcher mc= c.matcher(person);
            if (!mc.matches()){
                MyAlertDialog dialog = new MyAlertDialog(this,"联系人格式错误",null);
                dialog.show();
                dialog.setCanceledOnTouchOutside(false);
                return;
            }

            if(tel==""||tel==null||tel.isEmpty()){
                MyAlertDialog dialog = new MyAlertDialog(this,"联系电话不能为空",null);
                dialog.show();
                dialog.setCanceledOnTouchOutside(false);
                return;
            }

            String MPHONE = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8}$";
            String TPHONE = "^(0\\d{2}-\\d{8}(-\\d{1,4})?)|(0\\d{3}-\\d{7,8}(-\\d{1,4})?)$";
            Pattern pp = Pattern.compile(MPHONE);
            Pattern ppp= Pattern.compile(TPHONE);
            Matcher mm = pp.matcher(tel);
            Matcher mmm = ppp.matcher(tel);

            if (!mm.matches()&&!mmm.matches()){
                MyAlertDialog dialog = new MyAlertDialog(this,"联系电话格式错误",null);
                dialog.show();
                dialog.setCanceledOnTouchOutside(false);
                return;
            }


            if(email==""||email==null||email.isEmpty()){
                MyAlertDialog dialog = new MyAlertDialog(this,"电子邮件不能为空",null);
                dialog.show();
                dialog.setCanceledOnTouchOutside(false);
                return;
            }
            String mail = "^[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
            Pattern pm = Pattern.compile(mail);
            Matcher mp = pm.matcher(email);
            if (!mp.matches()){
                MyAlertDialog dialog = new MyAlertDialog(this,"电子邮件格式错误",null);
                dialog.show();
                dialog.setCanceledOnTouchOutside(false);
                return;
            }

            AndroidTool.showLoadDialog(this);
            getHttp();
        }
        else{
            Toast.makeText(this, no_net, Toast.LENGTH_SHORT).show();
        }

    }
    @Background
    void getHttp(){
        ApplyAccount applyAccount = new ApplyAccount(company,citycode,person,tel,email);
        BaseModelJson<String> bmj =  myRestClient.ApplyAccount(applyAccount);
        showsuccess(bmj);
    }
    @UiThread
    void showsuccess(BaseModelJson<String> bmj){
        AndroidTool.dismissLoadDialog();
        if (bmj!=null) {
            if (bmj.Successful) {
                dialog = new MyAlertDialog(this, "申请成功", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.close();
                        finish();
                    }
                });
                dialog.setCancelable(false);
                dialog.show();
            } else {
                MyAlertDialog dialog = new MyAlertDialog(this, bmj.Error, null);
                dialog.show();
            }
        }
    }

}
