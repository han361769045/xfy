package com.zczczy.by.xfy.activities;


import android.content.Context;
import android.view.KeyEvent;
import android.view.Window;

import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import com.zczczy.by.xfy.R;

import com.zczczy.by.xfy.model.BaseModel;
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

import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.WindowFeature;
import org.androidannotations.annotations.rest.RestService;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zczczy on 2015/11/16.
 * 修改密码
 */
@EActivity(R.layout.change_pw_layout)
@WindowFeature(value = {Window.FEATURE_NO_TITLE, Window.FEATURE_INDETERMINATE_PROGRESS})
public class ChangePwActivity extends  BaseActivity  {

    @ViewById
    MyTitleView title;
    @ViewById
    EditText edt_pswd, edt_new_pswd, edt_comfirm_paswd;
    @ViewById
    Button btn_register;

    @RestService
    MyRestClient myRestClient;
    @Bean
    MyErrorHandler myErrorHandler;

    @Pref
    MyPrefs_ pre;

    @AfterInject
    void afterInject() {
        close_flag = "PersonalCenterActivity";
        myRestClient.setRestErrorHandler(myErrorHandler);}
    @AfterViews
    void afterView() {
        title.setListener(this);
        title.setTitle("修改密码");

    }

    MyAlertDialog dialog;

    @Click
    void btn_register() {

        if (AndroidTool.checkIsNull(edt_pswd)) {
            MyAlertDialog dialog = new MyAlertDialog(this,"原密码不能为空",null);
            dialog.show();
            dialog.setCanceledOnTouchOutside(false);
            return;


        } else if (AndroidTool.checkIsNull(edt_new_pswd)) {
            MyAlertDialog dialog = new MyAlertDialog(this,"新密码不能为空!",null);
            dialog.show();
            dialog.setCanceledOnTouchOutside(false);
            return;

        }

        String regEx="^[A-Za-z0-9\\u4E00-\\u9FA5_-]+$";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(edt_new_pswd.getText().toString());
        if (!m.matches()){
            MyAlertDialog dialog = new MyAlertDialog(this,"密码不能有特殊符号",null);
            dialog.show();
            dialog.setCanceledOnTouchOutside(false);
            return;
        }

        else if (AndroidTool.checkIsNull(edt_comfirm_paswd)) {
            MyAlertDialog dialog = new MyAlertDialog(this,"确认密码不能为空!",null);
            dialog.show();
            dialog.setCanceledOnTouchOutside(false);
            return;
        }
//        String regx="[^[A-Za-z0-9\\u4E00-\\u9FA5_-]+$]";
//        Pattern pp = Pattern.compile(regx);
//        Matcher mm = pp.matcher(edt_comfirm_paswd.getText().toString());
//        if (!mm.matches()){
//            MyAlertDialog dialog = new MyAlertDialog(this,"密码不能有特殊符号",null);
//            dialog.show();
//            dialog.setCanceledOnTouchOutside(false);
//            return;
//        }

        else if (!edt_new_pswd.getText().toString().equals(edt_comfirm_paswd.getText().toString())) {
            MyAlertDialog dialog = new MyAlertDialog(this,"两次密码不一致!",null);
            dialog.show();
            dialog.setCanceledOnTouchOutside(false);
            return;
        } else {
            AndroidTool.showLoadDialog(this);
            ChangePassword(pre.username().get(), edt_pswd.getText().toString(), edt_new_pswd.getText().toString());
        }
    }

    @Background
    void ChangePassword(String username, String oldPassword, String newPassword) {
        String oldpwd = encryption(oldPassword);
        String newpwd = encryption(newPassword);

        myRestClient.setHeader("Token", pre.token().get());
        BaseModel bm = myRestClient.ChangePassword(username,oldpwd, newpwd);
        afterChangePassword(bm);


    }
    @UiThread
    void afterChangePassword(BaseModel bm) {
        AndroidTool.dismissLoadDialog();
        if (bm == null) {
            AndroidTool.showToast(this, no_net);
        }else if(bm.Successful){
            AndroidTool.showToast(this, "修改成功！");
            PersonalCenterActivity_.intent(this).start();
            myExit();
            finish();
            System.gc();
        }
        else {
            AndroidTool.showToast(this, bm.Error);
        }
    }

    //监听键盘回车键
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if(event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() != KeyEvent.ACTION_UP){
			 /*隐藏软键盘*/
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if(inputMethodManager.isActive()){
                inputMethodManager.hideSoftInputFromWindow(ChangePwActivity.this.getCurrentFocus().getWindowToken(), 0);
            }

            return false;
        }
        return super.dispatchKeyEvent(event);
    }

    //加密
    public String encryption(String pwd) {
        String re_md5 = new String();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(pwd.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            re_md5 = buf.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return re_md5;
    }
}
