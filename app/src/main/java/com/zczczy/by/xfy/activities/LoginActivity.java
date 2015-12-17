package com.zczczy.by.xfy.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zczczy.by.xfy.R;
import com.zczczy.by.xfy.dao.MessageDao;
import com.zczczy.by.xfy.model.BaseModelJson;

import com.zczczy.by.xfy.model.PagerResult;
import com.zczczy.by.xfy.model.PushMsg;
import com.zczczy.by.xfy.model.UserLogin;
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

import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.WindowFeature;
import org.androidannotations.annotations.res.StringRes;
import org.androidannotations.annotations.rest.RestService;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Set;


import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by zczczy on 2015/10/21.
 * 登陆
 */
@WindowFeature(value = {Window.FEATURE_NO_TITLE, Window.FEATURE_INDETERMINATE_PROGRESS})
@EActivity(R.layout.login_layout)
public class LoginActivity extends BaseActivity {

    public static final int REQUEST_CODE = 0;

    String username;

    String password;
    @Extra
    int flag;

    @ViewById
    MyTitleView title;
    @ViewById
    Button btn_login;

    @StringRes
    String txt_login;
    @ViewById
    EditText edt_username,edt_password;

    @Bean
    MyErrorHandler myErrorHandler;

    @Bean
    MessageDao messageDao;

    @RestService
    MyRestClient myRestClient;
    @Pref
    MyPrefs_ pre;

    @AfterInject
    void afterInject(){
        myRestClient.setRestErrorHandler(myErrorHandler);
    }

    @AfterViews
    void afterView() {
        title.setListener(this);
        title.setTitle(txt_login);

        edt_password.setOnKeyListener(listener_psd);
    }
    //设置别名
    private void setAlias(){
        EditText aliasEdit = (EditText) findViewById(R.id.edt_username);
        String alias = aliasEdit.getText().toString().trim();
        if (TextUtils.isEmpty(alias)) {
            Toast.makeText(LoginActivity.this,"alias不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!ExampleUtil.isValidTagAndAlias(alias)) {
            Toast.makeText(LoginActivity.this,"格式不对", Toast.LENGTH_SHORT).show();
            return;
        }

        //调用JPush API设置Alias
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias));
    }
    private static final String TAG = "JPush";

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    Log.d(TAG, "Set alias in handler.");
                    JPushInterface.setAliasAndTags(getApplicationContext(), (String) msg.obj, null, mAliasCallback);
                    break;

                case MSG_SET_TAGS:
                    Log.d(TAG, "Set tags in handler.");
                    JPushInterface.setAliasAndTags(getApplicationContext(), null, (Set<String>) msg.obj, mTagsCallback);
                    break;

                default:
                    Log.i(TAG, "Unhandled msg - " + msg.what);
            }
        }
    };

    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs ;
            switch (code) {
                case 0:
                    logs = "登陆成功";
                    Log.i(TAG, logs);
                    break;

                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    Log.i(TAG, logs);
                    if (ExampleUtil.isConnected(getApplicationContext())) {
                        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    } else {
                        Log.i(TAG, "No network");
                    }
                    break;

                default:
                    logs = "Failed with errorCode = " + code;
                    Log.e(TAG, logs);
            }

          //  ExampleUtil.showToast(logs, getApplicationContext());
        }

    };

    private final TagAliasCallback mTagsCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs ;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    Log.i(TAG, logs);
                    break;

                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    Log.i(TAG, logs);
                    if (ExampleUtil.isConnected(getApplicationContext())) {
                        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_TAGS, tags), 1000 * 60);
                    } else {
                        Log.i(TAG, "No network");
                    }
                    break;

                default:
                    logs = "Failed with errorCode = " + code;
                    Log.e(TAG, logs);
            }

            ExampleUtil.showToast(logs, getApplicationContext());
        }

    };

    private static final int MSG_SET_ALIAS = 1001;
    private static final int MSG_SET_TAGS = 1002;




    View.OnKeyListener listener_psd=new View.OnKeyListener(){
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if(keyCode==event.KEYCODE_ENTER &&  event.getAction() == KeyEvent.ACTION_DOWN){
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if(inputMethodManager.isActive()){
                    inputMethodManager.hideSoftInputFromWindow(LoginActivity.this.getCurrentFocus().getWindowToken(), 0);
                }
            }
            return false;
        }
    };

    //点击登录
    @Click
    void btn_login(){
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if(inputMethodManager.isActive()){
            inputMethodManager.hideSoftInputFromWindow(LoginActivity.this.getCurrentFocus().getWindowToken(), 0);
        }
        if(isNetworkAvailable(this)){
            username = edt_username.getText().toString();
            password = edt_password.getText().toString();
            if(username==""||username==null||username.isEmpty()){
                MyAlertDialog dialog = new MyAlertDialog(this,"请输入用户名",null);
                dialog.show();
                dialog.setCanceledOnTouchOutside(false);
                return;
            }
            if(password==""||password==null||password.isEmpty()){
                MyAlertDialog dialog = new MyAlertDialog(this,"请输入密码",null);
                dialog.show();
                dialog.setCanceledOnTouchOutside(false);
                return;
            }
            AndroidTool.showLoadDialog(this);
            loginHttp();

        }
        else{
            Toast.makeText(this, no_net, Toast.LENGTH_SHORT).show();
        }
    }

    @Background
    void loginHttp(){
        String pwd = encryption(password);
        BaseModelJson<UserLogin> bmj = myRestClient.Login(username, pwd);
        setPred(bmj);
    }

    @UiThread
    void setPred(BaseModelJson<UserLogin> bmj) {
        if (bmj != null) {
            if (bmj.Successful) {
                pre.username().put(bmj.Data.username);
                pre.password().put(bmj.Data.password);
                pre.C_address().put(bmj.Data.C_address);
                pre.token().put(bmj.Data.Token);
                pre.city().put(bmj.Data.city);
                pre.N_phone().put(bmj.Data.N_phone);
                pre.N_address().put(bmj.Data.N_address);
                pre.C_linkman().put(bmj.Data.C_linkman);
                pre.C_phone().put(bmj.Data.C_phone);
                pre.N_unit_name().put(bmj.Data.N_unit_name);
                pre.tid().put(bmj.Data.tid);
                pre.C_name().put(bmj.Data.C_name);
                getData();
                setAlias();

            } else {
                AndroidTool.dismissLoadDialog();
                MyAlertDialog dialog = new MyAlertDialog(this, "用户名密码错误！", null);
                dialog.show();
                dialog.setCanceledOnTouchOutside(false);
                return;
            }
        }else{
            AndroidTool.dismissLoadDialog();
        }
    }

    @Background()
    void  getData(){
        myRestClient.setHeader("Token",pre.token().get());
        BaseModelJson<PagerResult<PushMsg>> bmj =myRestClient.GetPushMessage(pre.username().get(), 0, 10000);
        afterHolding(bmj);
    }

    @UiThread
    void afterHolding( BaseModelJson<PagerResult<PushMsg>> bmj){
        AndroidTool.dismissLoadDialog();
        if(bmj!=null&&bmj.Successful){
            messageDao.insertOrUpdate(bmj.Data.ListData);
        }
        finish();
    }
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
