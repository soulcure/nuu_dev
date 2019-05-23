package com.nuu.user;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nuu.config.AppConfig;
import com.nuu.data.UsedDataAdapter;
import com.nuu.entity.DetailRsp;
import com.nuu.http.IPostListener;
import com.nuu.http.OkHttpConnector;
import com.nuu.nuuinfo.BaseActivity;
import com.nuu.nuuinfo.R;
import com.nuu.util.AppUtils;
import com.nuu.util.GsonUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class UserInfoSettingActivity extends BaseActivity implements View.OnClickListener {

    public static final String TAG = UserInfoSettingActivity.class.getSimpleName();

    private LinearLayout mEmptyView;
    private EmptyRecyclerViewDataObserver mEmpty = new EmptyRecyclerViewDataObserver();

    private UsedDataAdapter mAdapter;


    private Button btn_today;
    private Button btn_begin_data;
    private Button btn_end_data;
    private Button btn_query;


    private TextView tv_date;
    private TextView tv_used;
    private TextView tv_total;

    private RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userinfo_setting_activity);
        initView();

        reqDetailToday();
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_back) {
            onBackPressed();
        } else if (id == R.id.btn_begin_data) {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog.OnDateSetListener callBack = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    Calendar c = Calendar.getInstance();
                    c.set(year, monthOfYear, dayOfMonth);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
                    String data = sdf.format(c.getTime());
                    btn_begin_data.setText(data);
                }
            };
            DatePickerDialog dialog = new DatePickerDialog(mContext, callBack,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
            DatePicker datePicker = dialog.getDatePicker();
            datePicker.setMaxDate(calendar.getTimeInMillis());

            calendar.add(Calendar.YEAR, -1);
            datePicker.setMinDate(calendar.getTimeInMillis());//设置日期的上限日期
            dialog.show();
        } else if (id == R.id.btn_end_data) {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog.OnDateSetListener callBack = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    Calendar c = Calendar.getInstance();
                    c.set(year, monthOfYear, dayOfMonth);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
                    String data = sdf.format(c.getTime());
                    btn_end_data.setText(data);
                }
            };
            DatePickerDialog dialog = new DatePickerDialog(mContext, callBack,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
            DatePicker datePicker = dialog.getDatePicker();
            datePicker.setMaxDate(calendar.getTimeInMillis());

            calendar.add(Calendar.YEAR, -1);
            datePicker.setMinDate(calendar.getTimeInMillis());//设置日期的上限日期
            dialog.show();
        } else if (id == R.id.btn_query) {
            String beginDate = btn_begin_data.getText().toString();
            String endDate = btn_end_data.getText().toString();
            if (!TextUtils.isEmpty(beginDate)
                    && !TextUtils.isEmpty(endDate)
                    && beginDate.compareTo(endDate) < 0) {
                reqDetailPeriod(beginDate, endDate);
            }
        } else if (id == R.id.btn_today) {
            reqDetailToday();
        }
    }


    private void initView() {
        TextView tv_back = (TextView) findViewById(R.id.tv_back);
        tv_back.setOnClickListener(this);

        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("used data detail");

        ImageView img_right = (ImageView) findViewById(R.id.img_right);
        img_right.setOnClickListener(this);


    }


    private class EmptyRecyclerViewDataObserver extends RecyclerView.AdapterDataObserver {
        @Override
        public void onChanged() {
            checkIfEmpty();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            checkIfEmpty();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            checkIfEmpty();
        }
    }

    private void checkIfEmpty() {
        if (mAdapter != null) {
            int count = mAdapter.getItemCount();
            //正常状态
            if (count == 0) {
                mEmptyView.setVisibility(View.VISIBLE);
            } else {
                mEmptyView.setVisibility(View.GONE);
            }
        }
    }


    private void reqDetailToday() {
        String url = AppConfig.getHost();

        ContentValues params = new ContentValues();
        params.put("itf_name", "query_device_package_info");  //API name
        params.put("trans_serial", "1234cde");  //API name
        params.put("login", "tuser");
        params.put("auth_code", "abcd456");
        params.put("device_sn", "354243074362656");


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
