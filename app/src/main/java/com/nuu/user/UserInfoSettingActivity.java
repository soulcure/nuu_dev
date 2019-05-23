package com.nuu.user;

import android.content.ContentValues;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nuu.config.AppConfig;
import com.nuu.entity.DetailRsp;
import com.nuu.http.IPostListener;
import com.nuu.http.OkHttpConnector;
import com.nuu.nuuinfo.BaseActivity;
import com.nuu.nuuinfo.R;
import com.nuu.util.GsonUtil;


public class UserInfoSettingActivity extends BaseActivity implements View.OnClickListener {

    public static final String TAG = UserInfoSettingActivity.class.getSimpleName();

    private EditText et_email;
    private EditText et_name;
    private RadioButton rb_male;
    private RadioButton rb_female;
    private Spinner sp_age;
    private Spinner sp_country;
    private EditText et_mobile;
    private Spinner sp_im;
    private EditText et_im;

    private int gender = 1;//[1:Male 2:Female]
    private String age;
    private String nationality;
    private String im_type;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userinfo_setting_activity);
        initView();
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_back) {
            onBackPressed();
        } else if (id == R.id.img_right) {
            reqSettingUserInfo();
        }
    }


    private void initView() {
        TextView tv_back = (TextView) findViewById(R.id.tv_back);
        tv_back.setOnClickListener(this);

        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("used data detail");

        ImageView img_right = (ImageView) findViewById(R.id.img_right);
        img_right.setOnClickListener(this);

        et_email = (EditText) findViewById(R.id.et_email);
        et_name = (EditText) findViewById(R.id.et_name);

        rb_male = (RadioButton) findViewById(R.id.rb_male);
        rb_female = (RadioButton) findViewById(R.id.rb_female);

        sp_age = (Spinner) findViewById(R.id.sp_age);
        sp_age.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] countries = mContext.getResources().getStringArray(R.array.age_range);
                if (countries.length > position) {
                    age = countries[position];
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_country = (Spinner) findViewById(R.id.sp_country);
        sp_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] countries = mContext.getResources().getStringArray(R.array.country);
                if (countries.length > position) {
                    nationality = countries[position];
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        et_mobile = (EditText) findViewById(R.id.et_mobile);

        sp_im = (Spinner) findViewById(R.id.sp_im);
        sp_im.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] countries = mContext.getResources().getStringArray(R.array.im_type);
                if (countries.length > position) {
                    im_type = countries[position];
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        et_im = (EditText) findViewById(R.id.et_im);

        RadioGroup rg_gender = (RadioGroup) findViewById(R.id.rg_gender);
        rg_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_male) {
                    gender = 1;
                } else if (checkedId == R.id.rb_female) {
                    gender = 2;
                }
            }
        });

    }


    private void reqSettingUserInfo() {
        String url = AppConfig.getHost();

        ContentValues params = new ContentValues();
        params.put("itf_name", "query_device_package_info");  //API name
        params.put("trans_serial", "1234cde");  //API name
        params.put("login", "tuser");
        params.put("auth_code", "abcd456");
        params.put("device_sn", "354243074362656");

        params.put("email", et_email.getText().toString());
        params.put("name", et_name.getText().toString());
        params.put("sex", gender);
        params.put("age", age);
        params.put("nationality", nationality);
        params.put("mobile_nbr", et_mobile.getText().toString());
        params.put("im_type", im_type);
        params.put("im", et_im.getText().toString());

        OkHttpConnector.httpPost(url, params, new IPostListener() {
            @Override
            public void httpReqResult(String response) {
                DetailRsp rsp = GsonUtil.parse(response, DetailRsp.class);
                if (rsp != null && rsp.getErr_code() == 0) {

                } else if (rsp != null && !TextUtils.isEmpty(rsp.getErr_desc())) {
                    Toast.makeText(mContext, rsp.getErr_desc(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private void reqDetailPeriod(String beginDate, String endDate) {
        String url = AppConfig.getHost();

        ContentValues params = new ContentValues();
        params.put("itf_name", "query_used_package");  //API name
        params.put("trans_serial", "1234cde");  //API name
        params.put("login", "tuser");
        params.put("auth_code", "abcd456");
        params.put("device_sn", "354243074362656");
        params.put("begin_date", beginDate);
        params.put("end_date", endDate);

        OkHttpConnector.httpPost(url, params, new IPostListener() {
            @Override
            public void httpReqResult(String response) {
                //DetailRsp rsp = GsonUtil.parse(response, DetailRsp.class);
            }
        });

    }

}
