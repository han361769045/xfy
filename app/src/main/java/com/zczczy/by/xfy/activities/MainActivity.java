package com.zczczy.by.xfy.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.zczczy.by.xfy.R;
import com.zczczy.by.xfy.model.BaseModelJson;
import com.zczczy.by.xfy.model.UpdateApp;
import com.zczczy.by.xfy.prefs.MyPrefs_;
import com.zczczy.by.xfy.rest.MyErrorHandler;
import com.zczczy.by.xfy.rest.MyRestClient;
import com.zczczy.by.xfy.viewgroup.BadgeView;
import com.zczczy.by.xfy.viewgroup.MyAlertDialog;
import com.zczczy.by.xfy.viewgroup.MyViewAlertDialog;

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
import org.androidannotations.annotations.rest.RestService;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by zczczy on 2015/10/20.
 */
@WindowFeature(value = {Window.FEATURE_NO_TITLE, Window.FEATURE_INDETERMINATE_PROGRESS})
@EActivity(R.layout.main_layout)
public class MainActivity  extends BaseActivity {

    public static boolean isForeground = false;
    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.zczczy.leo.fuwuwangapp.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        registerReceiver(mMessageReceiver, filter);
    }


    AlertDialog dialog;
    //设置初始时间
    private long firstTime=0;

    //控件id依次为：首页，火警信息，巡检查岗，火警座席
    @ViewById
    LinearLayout ll_home, ll_message, ll_contact, ll_seat,confirm;
    @ViewById
    ImageView img_home, img_message, img_contact, img_seat;
    @ViewById
    TextView txt_home, txt_message, txt_contact, txt_seat;

    @RestService
    MyRestClient myRestClient;
    @Bean
    MyErrorHandler myErrorHandler;

    String mag="";

    @Pref
    MyPrefs_ pre;
    //实例化
    FragmentManager fragmentManager;
    HomeFragment homeFragment;
    MessageFragment messageFragment;
    ContactsFragment contactsFragment;
    SeatFragment seatFragment;

    //控制  选中状态时再被点中
    int flag = 0;
    @Extra
    int index=0;


    @AfterInject
    void afterInject(){
        myRestClient.setRestErrorHandler(myErrorHandler);
    }


    @AfterViews
    void afterView() {
        fragmentManager = getFragmentManager();
        setTabSelection(index);
        mContext=this;
        if(isNetworkAvailable(this)){
            getupdateapp();
        }
    }

    //更新
    @Background
    void getupdateapp()
    {

        BaseModelJson<UpdateApp> bmj= myRestClient.GetVersion(1);
        if(bmj!=null&&bmj.Successful)
        {
            GetUpdate(bmj.Data.versioncode,bmj.Data.versionurl);
        }

    }
    @UiThread
    void GetUpdate(int version,String Mag)
    {
        mag=Mag;
        int versionCode = 0;
        String versionname="";
        try
        {
            // 获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionCode = this.getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
            versionname=this.getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }
        if(version>versionCode)
        {
            MyAlertDialog dialog = new MyAlertDialog(MainActivity.this,"存在新版本！请更新！",listener);
            dialog.show();
            dialog.setCancelable(false);
            return;
        }

    }
    //监听下载对话框按钮
    View.OnClickListener listener =new View.OnClickListener()
    {
        @Override
        public void onClick(View view) {
            mHashMap=new HashMap<String, String>();
            mHashMap.put("name","xfy.apk");
            mHashMap.put("url",mag);

            // 给下载对话框增加进度条
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View v = inflater.inflate(R.layout.softupdate_progress, null);
            mProgress = (ProgressBar) v.findViewById(R.id.update_progress);

            dialogxia = new MyViewAlertDialog(MainActivity.this,v,"下载更新","",downloadlistener);
            dialogxia.show();
            dialogxia.setCanceledOnTouchOutside(false);

            new downloadApkThread().start();
        }
    };
    MyViewAlertDialog dialogxia ;
    View.OnClickListener downloadlistener =new View.OnClickListener()
    {
        @Override
        public void onClick(View view) {

        }
    };

     /* 下载中 */
    private static final int DOWNLOAD = 1;
    /* 下载结束 */
    private static final int DOWNLOAD_FINISH = 2;
    /* 保存解析的XML信息 */
    HashMap<String, String> mHashMap;
    /* 下载保存路径 */
    private String mSavePath;
    /* 记录进度条数量 */
    private int progress;
    /* 是否取消更新 */
    private boolean cancelUpdate = false;

    private Context mContext;
    /* 更新进度条 */
    private ProgressBar mProgress;
    private Dialog mDownloadDialog;

    private Handler mHandler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                // 正在下载
                case DOWNLOAD:
                    // 设置进度条位置
                    mProgress.setProgress(progress);
                    break;
                case DOWNLOAD_FINISH:
                    // 安装文件
                    installApk();
                    break;
                default:
                    break;
            }
        };
    };


    //安装apk文件
    private void installApk()
    {
        String filename=mHashMap.get("name");
        String sdpath = Environment.getExternalStorageDirectory() + "/";
        String savepath = sdpath + "download_cache/";
        File file = new File(savepath+filename);
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);     //浏览网页的Action(动作)
        String type = "application/vnd.android.package-archive";
        intent.setDataAndType(Uri.fromFile(file), type);  //设置数据类型
        startActivity(intent);
    }


    /**
     * 下载文件线程
     *
     * @author coolszy
     *@date 2012-4-26
     *@blog http://blog.92coding.com
     */
    private class downloadApkThread extends Thread
    {
        @Override
        public void run()
        {
            try
            {
                // 判断SD卡是否存在，并且是否具有读写权限
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
                {
                    // 获得存储卡的路径
                    String sdpath = Environment.getExternalStorageDirectory() + "/";
                    mSavePath = sdpath + "download_cache";
                    URL url = new URL(mHashMap.get("url"));
                    // 创建连接
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect();
                    // 获取文件大小
                    int length = conn.getContentLength();
                    // 创建输入流
                    InputStream is = conn.getInputStream();

                    File file = new File(mSavePath);
                    // 判断文件目录是否存在
                    if (!file.exists())
                    {
                        file.mkdir();
                    }
                    File apkFile = new File(mSavePath, mHashMap.get("name"));
                    FileOutputStream fos = new FileOutputStream(apkFile);
                    int count = 0;
                    // 缓存
                    byte buf[] = new byte[1024];
                    // 写入到文件中
                    do
                    {
                        int numread = is.read(buf);
                        count += numread;
                        // 计算进度条位置
                        progress = (int) (((float) count / length) * 100);
                        // 更新进度
                        mHandler.sendEmptyMessage(DOWNLOAD);
                        if (numread <= 0)
                        {
                            // 下载完成
                            mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
                            break;
                        }
                        // 写入文件
                        fos.write(buf, 0, numread);
                    } while (!cancelUpdate);// 点击取消就停止下载.
                    fos.close();
                    is.close();
                }
            } catch (MalformedURLException e)
            {
                e.printStackTrace();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    //首页
    @Click
    void ll_home() {
        if (flag == 0) {
            return;
        }
        setTabSelection(0);
    }

    //火警信息
    @Click
    void ll_message() {
        //badgeView.toggle(true);//启用默认动画
        if (flag == 1) {
            return;
        }
        setTabSelection(1);
    }

    //巡检查岗
    @Click
    void ll_contact() {
        if (flag == 2) {
            return;
        }
        setTabSelection(2);
    }

    //火警座席
    @Click
    void ll_seat() {
        if (flag == 3) {
            return;
        }
        setTabSelection(3);
    }

    /**
     * 根据传入的index参数来设置选中的tab页。
     *
     * @param index
     */
    public void setTabSelection(int index) {

        if (index!=0&&(pre.token().get()==""||pre.token().get()==null||pre.token().get().isEmpty())){
            showAlertDialog(1);
           return;
        }

        // 每次选中之前先清楚掉上次的选中状态
        clearSelection();
        // 开启一个Fragment事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);
        transaction.setCustomAnimations(R.anim.left_in, R.anim.left_out, R.anim.right_in, R.anim.right_out);

        switch (index) {
            case 0:
                // 当点击了消息tab时，改变控件的图片和文字颜色
                img_home.setSelected(true);
                txt_home.setSelected(true);
                if (homeFragment == null) {
                    // 如果homeFragment为空，则创建一个并添加到界面上
                    homeFragment = HomeFragment_.builder().build();
                    transaction.add(R.id.fl_content, homeFragment);
                } else {
                    // 如果homeFragment不为空，则直接将它显示出来
                    transaction.show(homeFragment);
                }
                flag = 0;
                break;
            case 1:
                // 当点击了消息tab时，改变控件的图片和文字颜色
                img_message.setSelected(true);
                txt_message.setSelected(true);
                if (messageFragment == null) {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    messageFragment = MessageFragment_.builder().build();
                    transaction.add(R.id.fl_content, messageFragment,"messageFragment");
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(messageFragment);
                }
                flag = 1;
                break;
            case 2:
                // 当点击了消息tab时，改变控件的图片和文字颜色
                img_contact.setSelected(true);
                txt_contact.setSelected(true);
                if (contactsFragment == null) {
                    // 如果contactsFragment为空，则创建一个并添加到界面上
                    contactsFragment = ContactsFragment_.builder().build();
                    transaction.add(R.id.fl_content, contactsFragment,"contactsFragment");
                } else {
                    // 如果contactsFragment不为空，则直接将它显示出来
                    transaction.show(contactsFragment);
                }
                flag = 2;
                break;
            case 3:
                // 当点击了消息tab时，改变控件的图片和文字颜色
                img_seat.setSelected(true);
                txt_seat.setSelected(true);
                if (seatFragment == null) {
                    // 如果meFragment为空，则创建一个并添加到界面上
                    seatFragment = SeatFragment_.builder().build();
                    transaction.add(R.id.fl_content, seatFragment,"seatFragment");
                } else {
                    // 如果meFragment不为空，则直接将它显示出来
                    transaction.show(seatFragment);
                }
                flag = 3;
                break;
            default:
                break;
        }
        transaction.commit();
    }

    /**
     * 清除掉所有的选中状态。
     */
    void clearSelection() {
        img_home.setSelected(false);
        img_message.setSelected(false);
        img_contact.setSelected(false);
        img_seat.setSelected(false);

        txt_home.setSelected(false);
        txt_message.setSelected(false);
        txt_contact.setSelected(false);
        txt_seat.setSelected(false);
    }

    /**
     * 将所有的Fragment都置为隐藏状态。
     *
     * @param transaction 用于对Fragment执行操作的事务
     */
    void hideFragments(FragmentTransaction transaction) {
        if (homeFragment != null) {
            transaction.hide(homeFragment);
        }
        if (messageFragment != null) {
            transaction.hide(messageFragment);
        }
        if (contactsFragment != null) {
            transaction.hide(contactsFragment);
        }
        if (seatFragment != null) {
            transaction.hide(seatFragment);
        }
    }


    /**
     * 创建对话框
     * wh
     * 2015-7-3
     */
    public void showAlertDialog(final int i){
        View view=(LinearLayout) getLayoutInflater().inflate(R.layout.login_dialog_layout,null);
        AlertDialog.Builder builder =new AlertDialog.Builder(this).setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                clearSelection();
                // 开启一个Fragment事务
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
                hideFragments(transaction);
                transaction.setCustomAnimations(R.anim.left_in, R.anim.left_out, R.anim.right_in, R.anim.right_out);
                // 当点击了消息tab时，改变控件的图片和文字颜色
                img_home.setSelected(true);
                txt_home.setSelected(true);

                if (homeFragment == null) {
                    // 如果homeFragment为空，则创建一个并添加到界面上
                    homeFragment = HomeFragment_.builder().build();
                    transaction.add(R.id.fl_content, homeFragment);
                } else {
                    // 如果homeFragment不为空，则直接将它显示出来
                    transaction.show(homeFragment);
                }
                flag=0;
                transaction.commit();
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
                LoginActivity_.intent(MainActivity.this).flag(i).start();
                dialog.dismiss();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if ((keyCode == KeyEvent.KEYCODE_BACK)	&& (event.getAction() == KeyEvent.ACTION_DOWN)) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //如果intent不指定category，那么无论intent filter的内容是什么都应该是匹配的。
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                String messge = intent.getStringExtra(KEY_MESSAGE);
                String extras = intent.getStringExtra(KEY_EXTRAS);
                StringBuilder showMsg = new StringBuilder();
                showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                if (!ExampleUtil.isEmpty(extras)) {
                    showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                }
                setCostomMsg(showMsg.toString());
            }

        }
    }
    private EditText msgText;
    private void setCostomMsg(String msg){
        if (null != msgText) {
            msgText.setText(msg);
            msgText.setVisibility(android.view.View.VISIBLE);
        }
    }
}
