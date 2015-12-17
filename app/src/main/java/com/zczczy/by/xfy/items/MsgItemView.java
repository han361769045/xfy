package com.zczczy.by.xfy.items;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zczczy.by.xfy.R;
import com.zczczy.by.xfy.dao.MessageDao;
import com.zczczy.by.xfy.dao.PushMessage;
import com.zczczy.by.xfy.model.PushMsg;
import com.zczczy.by.xfy.prefs.MyPrefs_;
import com.zczczy.by.xfy.viewgroup.BadgeView;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.Receiver;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

/**
 * Created by zczczy on 2015/11/17.
 * 我的消息
 */
@EViewGroup(R.layout.msg_item_layout)
public class MsgItemView extends ItemView<PushMessage> {

    @ViewById
    TextView msg_content,msg_time;
    @ViewById
    LinearLayout ll_area;

    @Pref
    MyPrefs_ pref;

    @Bean
    MessageDao messageDao;
    //添加圆点
    BadgeView badgeView ;

    public MsgItemView(Context context) {
        super(context);
    }
    @Override
    protected void init(Object... objects) {

        msg_time.setText(_data.getSend_time().replace("/","-"));
        if(pref.username().get().equals(_data.getUsername())){
            Spanned text  = Html.fromHtml(_data.getContent());
            msg_content.setText(text);
        }else{
            msg_content.setText("系统公告");
        }
        if(badgeView==null){
            badgeView = new BadgeView(context, ll_area);
        }
        badgeView.hide();
        if("0".equals(_data.getReadStatus())){
            badgeView.setText("1");
            badgeView.setTextSize(10);
            badgeView.setBadgePosition(BadgeView.POSITION_TOP_LEFT);
            badgeView.setBadgeMargin(3, 2);
            badgeView.show();
        }
    }
}
