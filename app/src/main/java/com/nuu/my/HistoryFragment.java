package com.nuu.my;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.nuu.entity.News;
import com.nuu.entity.PackageRsp;
import com.nuu.http.IPostListener;
import com.nuu.news.NewsAdapter;
import com.nuu.nuuinfo.BasePermissionFragment;
import com.nuu.nuuinfo.R;
import com.nuu.util.GsonUtil;

import java.util.ArrayList;
import java.util.List;


public class HistoryFragment extends BasePermissionFragment {

    public final static String TAG = HistoryFragment.class.getSimpleName();


    private LinearLayout mEmptyView;
    private EmptyRecyclerViewDataObserver mEmpty = new EmptyRecyclerViewDataObserver();

    private PurchasePackageAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null && bundle.size() > 0) {
        }
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        initView(view);
        reqData();
    }


    private void initView(View view) {
        mEmptyView = view.findViewById(R.id.message_empty_view);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new PurchasePackageAdapter(mContext);
        mAdapter.registerAdapterDataObserver(mEmpty);

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);
    }


    private void reqData() {
        app.reqPurchasedPackage(new IPostListener() {
            @Override
            public void httpReqResult(String response) {
                PackageRsp rsp = GsonUtil.parse(response, PackageRsp.class);
                if (rsp != null && rsp.getErr_code() == 0) {
                    mAdapter.setList(rsp.getPackageX());
                } else {
                    mAdapter.setList(null);
                }
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
