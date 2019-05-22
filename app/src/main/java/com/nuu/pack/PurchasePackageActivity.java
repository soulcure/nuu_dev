package com.nuu.pack;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nuu.entity.PackageRsp;
import com.nuu.nuuinfo.BaseActivity;
import com.nuu.nuuinfo.R;

import java.util.List;


public class PurchasePackageActivity extends BaseActivity implements View.OnClickListener {

    public static final String TAG = PurchasePackageActivity.class.getSimpleName();
    public static final String PACK_LIST = "pack_list";

    private LinearLayout mEmptyView;
    private EmptyRecyclerViewDataObserver mEmpty = new EmptyRecyclerViewDataObserver();

    private PurchasePackageAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.package_purchase_info);
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
        tv_title.setText("data info");

        ImageView img_right = (ImageView) findViewById(R.id.img_right);
        img_right.setVisibility(View.INVISIBLE);

        mEmptyView = (LinearLayout) findViewById(R.id.message_empty_view);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        List<PackageRsp.PackageBean> list = getIntent().getParcelableArrayListExtra(PACK_LIST);

        mAdapter = new PurchasePackageAdapter(this, list);
        mAdapter.registerAdapterDataObserver(mEmpty);
        recyclerView.setAdapter(mAdapter);
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
