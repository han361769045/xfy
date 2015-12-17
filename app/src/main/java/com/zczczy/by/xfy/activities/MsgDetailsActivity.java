package com.zczczy.by.xfy.activities;


import android.text.Html;
import android.text.Spanned;
import android.view.Window;
import android.widget.TextView;
import com.zczczy.by.xfy.R;
import com.zczczy.by.xfy.dao.PushMessage;
import com.zczczy.by.xfy.viewgroup.MyTitleView;

import org.androidannotations.annotations.AfterViews;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.WindowFeature;

/**
 * Created by zczczy on 2015/11/17.
 * 我的消息详情
 */
@WindowFeature(value = {Window.FEATURE_NO_TITLE, Window.FEATURE_INDETERMINATE_PROGRESS})
@EActivity(R.layout.msgdetails_layout)
public class MsgDetailsActivity extends  BaseActivity {
    @ViewById
    MyTitleView title;
    //消息内容，时间
    @ViewById
    TextView msg_content,msg_time;
    //接收的值

    @Extra
    PushMessage pushMessage;





    @AfterViews
    void afterView() {
        title.setListener(this);
        title.setTitle("消息详情");
        msg_time.setText(pushMessage.getSend_time().replace("/","-"));
        Spanned text  = Html.fromHtml(pushMessage.getContent());
        msg_content.setText(text);

    }

    @Override
    public void finish() {
        super.finish();
        setResult(1000);
    }
}
