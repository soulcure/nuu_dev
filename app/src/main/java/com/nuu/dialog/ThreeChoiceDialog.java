package com.nuu.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.nuu.nuuinfo.R;


public class ThreeChoiceDialog extends Dialog implements View.OnClickListener {
    private Context mContext;

    private int gravity = Gravity.CENTER;

    private String optionOneTitle;
    private String optionTwoTitle;

    private View.OnClickListener optionOneListener;
    private View.OnClickListener optionTwoListener;


    private Button btn_option1;
    private Button btn_option2;
    private Button btn_cancel;


    private ThreeChoiceDialog(Builder builder) {
        super(builder.context, R.style.custom_dialog);
        setCanceledOnTouchOutside(false);

        mContext = builder.context;
        optionOneTitle = builder.optionOneTitle;
        optionTwoTitle = builder.optionTwoTitle;
        optionOneListener = builder.optionOneListener;
        optionTwoListener = builder.optionTwoListener;
        gravity = builder.gravity;
    }


    public static class Builder {
        private Context context;
        private String optionOneTitle;
        private String optionTwoTitle;

        private View.OnClickListener optionOneListener;
        private View.OnClickListener optionTwoListener;

        private int gravity = Gravity.CENTER;

        public Builder(Context context) {
            this.context = context;
        }

        public ThreeChoiceDialog builder() {
            return new ThreeChoiceDialog(this);
        }


        public Builder setOptionOneTitle(String optionOneTitle) {
            this.optionOneTitle = optionOneTitle;
            return this;
        }

        public Builder setOptionTwoTitle(String optionTwoTitle) {
            this.optionTwoTitle = optionTwoTitle;
            return this;
        }

        public Builder setOptionOneListener(View.OnClickListener optionOneListener) {
            this.optionOneListener = optionOneListener;
            return this;
        }

        public Builder setOptionTwoListener(View.OnClickListener optionTwoListener) {
            this.optionTwoListener = optionTwoListener;
            return this;
        }

        public Builder setGravity(int gravity) {
            this.gravity = gravity;
            return this;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_select_gender);
        setDialogFeature();
        initView();
    }

    /**
     * 设置对话框特征
     */
    private void setDialogFeature() {
        // 设置宽度为屏宽、靠近屏幕底部。
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams wlp = window.getAttributes();
            wlp.gravity = gravity;
            wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(wlp);
        }
    }


    public void initView() {
        btn_option1 = (Button) findViewById(R.id.btn_option1);
        btn_option2 = (Button) findViewById(R.id.btn_option2);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);

        btn_option1.setOnClickListener(this);
        btn_option2.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_option1) {//选择菜单1
            if (optionOneListener != null) {
                optionOneListener.onClick(v);
            }
            dismiss();
        } else if (id == R.id.btn_option2) { //选择菜单1
            if (optionTwoListener != null) {
                optionTwoListener.onClick(v);
            }
            dismiss();
        } else if (id == R.id.btn_cancel) {
            dismiss();
        }
    }


}
