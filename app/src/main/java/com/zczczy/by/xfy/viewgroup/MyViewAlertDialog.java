package com.zczczy.by.xfy.viewgroup;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zczczy.by.xfy.R;

/**
 * Created by zczczy on 2015/11/23.
 */
public class MyViewAlertDialog extends AlertDialog {
    private String message;
    private String title;
    LinearLayout setlayout;
    View view;
    public MyViewAlertDialog(Context context, View view,String title,String meg, View.OnClickListener newclickListener) {
        super(context);
        this.view=view;
        this.message=meg;
        this.title=title;
        if(newclickListener!=null)
            this.clickListener = newclickListener;
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            MyViewAlertDialog.this.dismiss();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.download_dialog_layout);
        setlayout = (LinearLayout)findViewById(R.id.setlayout);
        setlayout.addView(view);
        LinearLayout confirm = (LinearLayout) findViewById(R.id.confirm);
        TextView txtclick = (TextView) findViewById(R.id.txtclick);
        txtclick.setText(message);
        TextView txttitle = (TextView) findViewById(R.id.txttitle);
        txttitle.setText(title);
        confirm.setOnClickListener(clickListener);
    }

}
