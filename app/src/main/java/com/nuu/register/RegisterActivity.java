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

import com.nuu.entity.LoginRsp;
import com.nuu.entity.RegisterRsp;
import com.nuu.http.IPostListener;
import com.nuu.http.OkHttpConnector;
import com.nuu.nuuinfo.BaseActivity;
import com.nuu.nuuinfo.R;
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
        String username = et_display.getText().toString();
        if (TextUtils.isEmpty(username)) {
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

        String mobile = phoneInputView.getText();
        String iso = phoneInputView.getSelectedCountry().getIso();


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


        reqRegister(username, email, mobile, iso, password);
    }

    private void reqRegister(final String username, String email, String mobile,
                             String iso, final String password) {
        String url = "http://119.23.74.49:8899/register";

        ContentValues params = new ContentValues();
        params.put("username", username);
        params.put("email", email);
        params.put("mobile", mobile);
        params.put("iso", iso);
        params.put("password", password);

        OkHttpConnector.httpPost(url, params, new IPostListener() {
            @Override
            public void httpReqResult(String response) {
                RegisterRsp rsp = GsonUtil.parse(response, RegisterRsp.class);
                if (rsp != null && rsp.isSuccess()) {
                    Toast.makeText(mContext, rsp.getMsg(), Toast.LENGTH_SHORT).show();
                    reqLogin(username, password);
                } else if (rsp != null && !TextUtils.isEmpty(rsp.getMsg())) {
                    Toast.makeText(mContext, rsp.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void reqLogin(String username, String password) {
        String url = "http://119.23.74.49:8899/login";

        ContentValues params = new ContentValues();
        params.put("username", username);
        params.put("password", password);

        OkHttpConnector.httpPost(url, params, new IPostListener() {
            @Override
            public void httpReqResult(String response) {
                LoginRsp rsp = GsonUtil.parse(response, LoginRsp.class);
                if (rsp != null && rsp.isSuccess()) {
                    String token = rsp.getData().getToken();
                    String uuid = rsp.getData().getUuid();
                    long expired = rsp.getData().getExpired();
                    app.setToken(token);
                    app.setUuid(uuid);
                    app.setExpTime(expired);

                    finish();
                } else if (rsp != null && !TextUtils.isEmpty(rsp.getMsg())) {
                    Toast.makeText(mContext, rsp.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


}
