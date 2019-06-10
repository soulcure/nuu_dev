package com.nuu.register;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nuu.config.AppConfig;
import com.nuu.entity.RegisterRsp;
import com.nuu.http.IPostListener;
import com.nuu.http.OkHttpConnector;
import com.nuu.nuuinfo.BaseActivity;
import com.nuu.nuuinfo.R;
import com.nuu.util.AppUtils;
import com.nuu.util.DeviceInfo;
import com.nuu.util.GsonUtil;

import net.rimoto.intlphoneinput.IntlPhoneInput;


public class ForgetActivity extends BaseActivity implements View.OnClickListener {

    public static final String FORGET_TYPE = "forget_type";

    public static final int TYPE_PASSWORD = 0;
    public static final int TYPE_EMAIL = 1;
    int type;

    TextInputEditText et_email;

    TextView tv_mobile;


    IntlPhoneInput phoneInputView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
        initView();
    }


    private void initView() {
        TextView tv_back = findViewById(R.id.tv_back);
        tv_back.setOnClickListener(this);

        TextView tv_title = findViewById(R.id.tv_title);


        ImageView img_right = findViewById(R.id.img_right);
        img_right.setVisibility(View.INVISIBLE);


        et_email = findViewById(R.id.et_email);

        tv_mobile = findViewById(R.id.tv_mobile);
        phoneInputView = findViewById(R.id.phone_input);


        type = getIntent().getIntExtra(FORGET_TYPE, 0);
        if (type == TYPE_PASSWORD) {
            tv_title.setText(R.string.forget_password);

            tv_mobile.setVisibility(View.GONE);
            phoneInputView.setVisibility(View.GONE);
        } else if (type == TYPE_EMAIL) {
            tv_title.setText(R.string.forget_email);

            et_email.setVisibility(View.GONE);
        }


        Button btn_submit = findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_back) {
            finish();
        } else if (id == R.id.btn_submit) {
            checkInput();
        }

    }


    private void checkInput() {

        if (type == TYPE_PASSWORD) {
            String email = et_email.getText().toString();
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(this, R.string.email_empty, Toast.LENGTH_SHORT).show();
                return;
            }

            if (!AppUtils.isEmail(email)) {
                Toast.makeText(this, R.string.email_error, Toast.LENGTH_SHORT).show();
                return;
            }
        } else if (type == TYPE_EMAIL) {
            if (!phoneInputView.isValid()) {
                Toast.makeText(mContext, R.string.mobile_invalid, Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }

    private void reqRegister(String name, String email, String country, String mobile) {
        String url = AppConfig.getHost();

        ContentValues params = new ContentValues();
        params.put("itf_name", "device_customer_register");  //API name
        params.put("trans_serial", "1234cde");  //API name
        params.put("login", "tuser");
        params.put("auth_code", "abcd456");
        params.put("device_sn", DeviceInfo.getDeviceSN());
        params.put("email", email);
        params.put("name", name);
        //params.put("sex", 1);
        //params.put("age", "30-35");
        params.put("nationality", country);
        params.put("mobile_nbr", mobile);
        //params.put("im_type", "WeChat");
        //params.put("im", "kenny@nuumobile");

        OkHttpConnector.httpPost(url, params, new IPostListener() {
            @Override
            public void httpReqResult(String response) {
                RegisterRsp rsp = GsonUtil.parse(response, RegisterRsp.class);
                if (rsp != null && rsp.getErr_code() == 0) {
                    Toast.makeText(mContext, R.string.success, Toast.LENGTH_SHORT).show();
                    finish();
                } else if (rsp != null && !TextUtils.isEmpty(rsp.getErr_desc())) {
                    Toast.makeText(mContext, rsp.getErr_desc(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
