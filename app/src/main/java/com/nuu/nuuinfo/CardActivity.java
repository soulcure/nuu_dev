package com.nuu.nuuinfo;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.nuu.config.AppConfig;
import com.nuu.entity.PackageRsp;
import com.nuu.http.IPostListener;
import com.nuu.http.OkHttpConnector;
import com.nuu.util.GsonUtil;


public class CardActivity extends BaseActivity {
    private RecyclerView mRecyclerView;
    private CardAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        initView();
        initData();
    }

    private void initView() {
        // 获取RecyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        // 设置LinearLayoutManager
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, RecyclerView.VERTICAL));
        // 设置ItemAnimator
        //mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        // 设置固定大小
        //mRecyclerView.setHasFixedSize(true);
        // 初始化自定义的适配器
        mAdapter = new CardAdapter(this);
        // 为mRecyclerView设置适配器
        mRecyclerView.setAdapter(mAdapter);
    }


    private void initData() {
        reqPurchasedPackage();
    }


    private void reqPurchasedPackage() {
        String url = AppConfig.getHost();

        ContentValues params = new ContentValues();
        params.put("itf_name", "query_package");  //API name
        params.put("trans_serial", "1234cde");  //API name
        params.put("login", "tuser");
        params.put("auth_code", "abcd456");
        params.put("device_sn", "354243074362656");

        OkHttpConnector.httpPost(url, params, new IPostListener() {
            @Override
            public void httpReqResult(String response) {
                PackageRsp rsp = GsonUtil.parse(response, PackageRsp.class);
                if (rsp != null && rsp.getErr_code() == 0) {
                    mAdapter.setPackageUsed(rsp);
                }

            }
        });

    }
}
