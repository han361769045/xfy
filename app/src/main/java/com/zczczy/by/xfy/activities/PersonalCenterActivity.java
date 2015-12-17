package com.zczczy.by.xfy.activities;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.squareup.picasso.Picasso;
import com.zczczy.by.xfy.MyApplication;
import com.zczczy.by.xfy.R;
import com.zczczy.by.xfy.dao.DatabaseHelper;
import com.zczczy.by.xfy.dao.MessageDao;
import com.zczczy.by.xfy.dao.PushMessage;
import com.zczczy.by.xfy.model.BaseModelJson;
import com.zczczy.by.xfy.model.UserLogin;
import com.zczczy.by.xfy.prefs.MyPrefs_;
import com.zczczy.by.xfy.rest.MyErrorHandler;
import com.zczczy.by.xfy.rest.MyRestClient;
import com.zczczy.by.xfy.tools.AndroidTool;
import com.zczczy.by.xfy.viewgroup.BadgeView;
import com.zczczy.by.xfy.viewgroup.MyAlertDialog;
import com.zczczy.by.xfy.viewgroup.MyConfirmAlertDialog;
import com.zczczy.by.xfy.viewgroup.MyTitleView;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.Receiver;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.WindowFeature;
import org.androidannotations.annotations.res.StringRes;
import org.androidannotations.annotations.rest.RestService;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by zczczy on 2015/10/21.
 * 个人中心
 */
@WindowFeature(value = {Window.FEATURE_NO_TITLE, Window.FEATURE_INDETERMINATE_PROGRESS})
@EActivity(R.layout.personal_layout)
public class PersonalCenterActivity extends BaseActivity {

    @ViewById
    MyTitleView title;
    @ViewById
    TextView txt_name,message;
    @StringRes
    String txt_center;
    @ViewById
    RelativeLayout rl_message,rl_equip,rl_changepw,rl_exit;

    @ViewById
    LinearLayout confirm;
    @Pref
    MyPrefs_ pre;

    MyConfirmAlertDialog confirm_dialog;

    @RestService
    MyRestClient myRestClient;
    @Bean
    MyErrorHandler myErrorHandler;
    @Bean
    MessageDao messageDao;
    @Extra
    int reindex;


    //添加圆点
    BadgeView badgeView ;

    DatabaseHelper databaseHelper_;


    @AfterInject
    void afterInject() {
        close_flag = "PersonalCenterActivity";
        myRestClient.setRestErrorHandler(myErrorHandler);
    }


    @AfterViews
    void afterView() {
        databaseHelper_ = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        title.setListener(this);
        title.setTitle(txt_center);
        txt_name.setText(pre.C_name().get());
        if (pre.token().get()==""||pre.token().get()==null||pre.token().get().isEmpty()){
            AlertDialog();
        }

        badgeView = new BadgeView(this,rl_message);
        badgeView.setBadgePosition(BadgeView.POSITION_TOP_LEFT);

    }


    @Receiver(actions = "notify_changed")
    public void reciverB(){
        changeBB();
    }
    public  void changeBB(){
        badgeView.hide();
        long i =messageDao.getStatus();
        if (i>0){
            badgeView.setText(i+"");
            badgeView.setTextSize(10);
            badgeView.show();
        }
    }

    public  void  onResume(){
        super.onResume();
        changeBB();
    }
    AlertDialog dialog;
    public void AlertDialog(){
        View view=(LinearLayout) getLayoutInflater().inflate(R.layout.login_dialog_layout,null);
        AlertDialog.Builder builder =new AlertDialog.Builder(this).setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
            }
        });
        builder.setView(view);
        builder.create();
        dialog = builder.show();
        dialog.setCanceledOnTouchOutside(false);
        //为对话框中的LinearLayout添加点击事件
        this.confirm=(LinearLayout)dialog.findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginActivity_.intent(PersonalCenterActivity.this).start();
                dialog.dismiss();
            }
        });
    }



    //我的消息
    @Click
    void rl_message() {
        if(isNetworkAvailable(this)){
            if (pre.token().get()==""||pre.token().get()==null||pre.token().get().isEmpty()){
                AlertDialog();
            }
            else {
                MyMessageActivity_.intent(this).start();
            }
        }
        else {
            Toast.makeText(this, no_net, Toast.LENGTH_SHORT).show();
        }
    }

    //查询设备

    @Click
    void rl_equip() {
        if(isNetworkAvailable(this)){
            if (pre.token().get()==""||pre.token().get()==null||pre.token().get().isEmpty()){
                AlertDialog();
            }
            else {
                MyEquipmentActivity_.intent(this).start();
            }
        }
        else {
            Toast.makeText(this, no_net, Toast.LENGTH_SHORT).show();
        }
    }

    @Click
    void rl_changepw(){
        if(isNetworkAvailable(this)){
            if (pre.token().get()==""||pre.token().get()==null||pre.token().get().isEmpty()){
                AlertDialog();
            }
            else {
                ChangePwActivity_.intent(this).start();
            }
        }
        else {
            Toast.makeText(this, no_net, Toast.LENGTH_SHORT).show();

        }
    }

    //登出
    @Click
    void rl_exit() {
        confirm_dialog = new MyConfirmAlertDialog(this, "您确认要注销吗", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginActivity_.intent(PersonalCenterActivity.this).start();
                txt_name.setText("");
                pre.clear();
                setAlias();
                databaseHelper_.clearTab();
                confirm_dialog.dismiss();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Intent intent = new Intent();
                        intent.setAction("refresh");
                        sendBroadcast(intent);
                    }
                }).start();
            }
        });
        confirm_dialog.setCanceledOnTouchOutside(false);
        confirm_dialog.show();
    }
    //清除别名
    private void setAlias(){

        String alias ="";

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

            //   ExampleUtil.showToast(logs, getApplicationContext());
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

}
