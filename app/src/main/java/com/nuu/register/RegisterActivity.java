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
import com.nuu.util.DeviceInfo;
import com.nuu.util.GsonUtil;

import net.rimoto.intlphoneinput.IntlPhoneInput;


public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    TextInputEditText et_display;
    TextInputEditText et_email;
    IntlPhoneInput phoneInputView;
    TextInputEditText et_password;
    TextInputEditText et_retype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }


    private void initView() {
        TextView tv_back = findViewById(R.id.tv_back);
        tv_back.setOnClickListener(this);

        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText(R.string.action_register);

        ImageView img_right = findViewById(R.id.img_right);
        img_right.setVisibility(View.INVISIBLE);


        et_display = findViewById(R.id.et_display);
        et_email = findViewById(R.id.et_email);
        phoneInputView = findViewById(R.id.phone_input);
        et_password = findViewById(R.id.et_password);
        et_retype = findViewById(R.id.et_retype);

        Button btn_register = findViewById(R.id.btn_register);
        btn_register.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_back) {
            finish();
        } else if (id == R.id.btn_register) {
            checkInput();
        }

    }


    private void checkInput() {
        String name = et_display.getText().toString();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(mContext, R.string.name_empty, Toast.LENGTH_SHORT).show();
            return;
        }

        String email = et_email.getText().toString();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(mContext, R.string.email_empty, Toast.LENGTH_SHORT).show();
            return;
        }

        if (!phoneInputView.isValid()) {
            Toast.makeText(mContext, R.string.mobile_invalid, Toast.LENGTH_SHORT).show();
            return;
        }


        String password = et_password.getText().toString();
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(mContext, R.string.password_empty, Toast.LENGTH_SHORT).show();
            return;
        }

        String retype = et_retype.getText().toString();
        if (TextUtils.isEmpty(retype)) {
            Toast.makeText(mContext, R.string.retype_empty, Toast.LENGTH_SHORT).show();
            return;
        }

        if (!retype.equals(password)) {
            Toast.makeText(mContext, R.string.password_retype, Toast.LENGTH_SHORT).show();
            return;
        }
        String mobile = phoneInputView.getText();
        String country = phoneInputView.getSelectedCountry().getName();

        reqRegister(name, email, country, mobile);
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
