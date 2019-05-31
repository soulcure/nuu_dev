package com.nuu.setting;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.nuu.config.AppConfig;
import com.nuu.entity.BuyPackageRsp;
import com.nuu.http.IPostListener;
import com.nuu.http.OkHttpConnector;
import com.nuu.nuuinfo.BaseActivity;
import com.nuu.nuuinfo.R;
import com.nuu.util.GsonUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class NetSettingActivity extends BaseActivity implements View.OnClickListener {

    public static final String TAG = NetSettingActivity.class.getSimpleName();

    public static final String PACKAGE_ID = "package_id";


    private Switch switch_play;
    private Switch switch_vpn;


    private int valid_type;
    private LinearLayout linear_type;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_net);
        initView();
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_back) {
            onBackPressed();
        } else if (id == R.id.btn_data) {

        } else if (id == R.id.btn_commit) {

        }
    }


    private void initView() {
        TextView tv_back = (TextView) findViewById(R.id.tv_back);
        tv_back.setOnClickListener(this);

        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("net setting");

        ImageView img_right = (ImageView) findViewById(R.id.img_right);
        img_right.setVisibility(View.INVISIBLE);

        switch_play = (Switch) findViewById(R.id.switch_play);
        switch_play.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });


        switch_vpn = (Switch) findViewById(R.id.switch_vpn);
        switch_vpn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
    }


}
