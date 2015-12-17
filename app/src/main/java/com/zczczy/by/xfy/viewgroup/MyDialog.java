package com.zczczy.by.xfy.viewgroup;


import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.zczczy.by.xfy.R;


public class MyDialog extends AlertDialog {

    Button btn_d1, btn_d2, btn_d3;


    public MyDialog(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public MyDialog(Context context, boolean cancelable,
                    OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        // TODO Auto-generated constructor stub
    }

    public MyDialog(Context context, int theme) {
        super(context, theme);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_choice_layout);
        btn_d1 = (Button) findViewById(R.id.btn_d1);
        btn_d2 = (Button) findViewById(R.id.btn_d2);
        btn_d3 = (Button) findViewById(R.id.btn_d3);

        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = getWindow().getWindowManager();
        windowManager.getDefaultDisplay().getMetrics(dm);
        int SCREEN_WIDTH = dm.widthPixels;
        //int SCREEN_HEIGHT = dm.heightPixels;
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = (int) (SCREEN_WIDTH * 0.9);
        //lp.height = (int)(SCREEN_HEIGHT * 0.5);
        getWindow().setGravity(Gravity.BOTTOM);  //此处可以设置dialog显示的位置
        getWindow().setAttributes(lp);
    }

    public void setOnCustomCancleButtonListener(String btnName) {
        if (btnName != "" || btnName != null) {
            btn_d3.setText(btnName);
        }
        btn_d3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                MyDialog.this.dismiss();
            }
        });
    }

    public void setOnCustomConfirmButtonListener(String btnName, View.OnClickListener oncilckListener) {
        if (btnName != "" || btnName != null) {
            btn_d1.setText(btnName);
        }
        btn_d1.setOnClickListener(oncilckListener);
    }

    public void setOnCustomOtherButtonListener(String btnName, View.OnClickListener oncilckListener) {
        if (btnName != "" || btnName != null) {
            btn_d2.setText(btnName);
        }
        btn_d2.setOnClickListener(oncilckListener);
    }
}
