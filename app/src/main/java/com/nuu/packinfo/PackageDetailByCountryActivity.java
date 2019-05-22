package com.nuu.packinfo;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.nuu.config.AppConfig;
import com.nuu.entity.CurUsingPackageRsp;
import com.nuu.entity.PackageDetailByCountryRsp;
import com.nuu.entity.PackageRsp;
import com.nuu.http.IPostListener;
import com.nuu.http.OkHttpConnector;
import com.nuu.nuuinfo.BaseActivity;
import com.nuu.nuuinfo.R;
import com.nuu.util.GsonUtil;

import java.util.List;


public class PackageDetailByCountryActivity extends BaseActivity implements View.OnClickListener {

    public static final String TAG = PackageDetailByCountryActivity.class.getSimpleName();
    public static final String PACK_LIST = "pack_list";

    private LinearLayout mEmptyView;
    private EmptyRecyclerViewDataObserver mEmpty = new EmptyRecyclerViewDataObserver();

    private PackageDetailAdapter mAdapter;

    private Spinner spinner;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.package_detail_by_country);
        initView();
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_back) {
            onBackPressed();
        }
    }


    private void initView() {
        TextView tv_back = (TextView) findViewById(R.id.tv_back);
        tv_back.setOnClickListener(this);

        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("package info");

        ImageView img_right = (ImageView) findViewById(R.id.img_right);
        img_right.setVisibility(View.INVISIBLE);

        mEmptyView = (LinearLayout) findViewById(R.id.message_empty_view);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new PackageDetailAdapter(this);
        mAdapter.registerAdapterDataObserver(mEmpty);
        recyclerView.setAdapter(mAdapter);

        spinner = (Spinner) findViewById(R.id.spinner);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] countries = mContext.getResources().getStringArray(R.array.country);
                if (countries.length > position) {
                    reqPackageDetailByCountry(countries[position]);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void reqPackageDetailByCountry(String country) {
        String url = AppConfig.getHost();

        ContentValues params = new ContentValues();
        params.put("itf_name", "query_package_for_device");  //API name
        params.put("trans_serial", "1234cde");  //API name
        params.put("login", "tuser");
        params.put("auth_code", "abcd456");
        params.put("device_sn", "354243074362656");
        params.put("country", country);


        OkHttpConnector.httpPost(url, params, new IPostListener() {
            @Override
            public void httpReqResult(String response) {
                PackageDetailByCountryRsp rsp = GsonUtil.parse(response, PackageDetailByCountryRsp.class);
                if (rsp != null && rsp.getErr_code() == 0) {
                    List<PackageDetailByCountryRsp.PackageBean> list = rsp.getPackageX();
                    mAdapter.setList(list);
                }
            }
        });

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


}
