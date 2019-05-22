package com.nuu.pack;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
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


public class BuyPackageActivity extends BaseActivity implements View.OnClickListener {

    public static final String TAG = BuyPackageActivity.class.getSimpleName();

    public static final String PACKAGE_ID = "package_id";

    private Spinner spinner;
    private EditText et_num;
    private Button btn_data;
    private Button btn_commit;

    private int valid_type;
    private LinearLayout linear_type;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.package_buy_activity);
        initView();
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_back) {
            onBackPressed();
        } else if (id == R.id.btn_data) {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog.OnDateSetListener callBack = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    Calendar c = Calendar.getInstance();
                    c.set(year, monthOfYear, dayOfMonth);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
                    String data = sdf.format(c.getTime());
                    btn_data.setText(data);
                }
            };
            DatePickerDialog dialog = new DatePickerDialog(mContext, callBack,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
            DatePicker datePicker = dialog.getDatePicker();
            datePicker.setMinDate(calendar.getTimeInMillis());

            calendar.add(Calendar.YEAR, 1);
            datePicker.setMaxDate(calendar.getTimeInMillis());//设置日期的上限日期
            dialog.show();
        } else if (id == R.id.btn_commit) {
            int packageId = getIntent().getIntExtra(PACKAGE_ID, 0);
            String beginData = btn_data.getText().toString();
            String total = et_num.getText().toString();
            int num;
            try {
                num = Integer.parseInt(total);
            } catch (NumberFormatException e) {
                num = 0;
                e.printStackTrace();
            }

            if (packageId != 0) {
                reqBuyPackage(packageId, beginData, num, valid_type);
            } else {
                Toast.makeText(mContext, "package id error", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void initView() {
        TextView tv_back = (TextView) findViewById(R.id.tv_back);
        tv_back.setOnClickListener(this);

        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("package info");

        ImageView img_right = (ImageView) findViewById(R.id.img_right);
        img_right.setVisibility(View.INVISIBLE);

        et_num = (EditText) findViewById(R.id.et_num);
        et_num.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String total = s.toString();
                int num;
                try {
                    num = Integer.parseInt(total);
                } catch (NumberFormatException e) {
                    num = 0;
                    e.printStackTrace();
                }

                if (num == 0) {
                    linear_type.setVisibility(View.INVISIBLE);
                    btn_commit.setEnabled(false);
                } else if (num == 1) {
                    linear_type.setVisibility(View.INVISIBLE);
                    btn_commit.setEnabled(true);
                } else if (num > 1) {
                    linear_type.setVisibility(View.VISIBLE);
                    btn_commit.setEnabled(true);
                }

            }
        });
        btn_data = (Button) findViewById(R.id.btn_data);
        btn_data.setOnClickListener(this);

        btn_commit = (Button) findViewById(R.id.btn_commit);
        btn_commit.setOnClickListener(this);


        linear_type = (LinearLayout) findViewById(R.id.linear_type);
        spinner = (Spinner) findViewById(R.id.spinner);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                valid_type = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void reqBuyPackage(int packageId, String beginData, int total_num, int valid_type) {
        String url = AppConfig.getHost();

        ContentValues params = new ContentValues();
        params.put("itf_name", "buy_package");  //API name
        params.put("trans_serial", "1234cde");  //API name
        params.put("login", "tuser");
        params.put("auth_code", "abcd456");
        params.put("device_sn", "354243074362656");
        params.put("package_id", packageId);
        params.put("begin_date", beginData);
        params.put("total_num", total_num);
        params.put("valid_type", valid_type);
        showProgressDialog();

        OkHttpConnector.httpPost(url, params, new IPostListener() {
            @Override
            public void httpReqResult(String response) {
                BuyPackageRsp rsp = GsonUtil.parse(response, BuyPackageRsp.class);
                if (rsp != null && rsp.getErr_code() == 0) {
                    Toast.makeText(mContext, "buy package success", Toast.LENGTH_SHORT).show();
                } else if (rsp != null && !TextUtils.isEmpty(rsp.getErr_desc())) {
                    Toast.makeText(mContext, rsp.getErr_desc(), Toast.LENGTH_SHORT).show();
                }
                dismissProgressDialog();
            }
        });

    }


}
