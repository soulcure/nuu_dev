package com.nuu.devices;

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

import com.nuu.entity.Devices;
import com.nuu.news.NewsFragment;
import com.nuu.nuuinfo.BasePermissionFragment;
import com.nuu.nuuinfo.R;

import java.util.ArrayList;
import java.util.List;


public class DevicesFragment extends BasePermissionFragment {

    public final static String TAG = DevicesFragment.class.getSimpleName();

    private LinearLayout mEmptyView;
    private EmptyRecyclerViewDataObserver mEmpty = new EmptyRecyclerViewDataObserver();
    private DevicesAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null && bundle.size() > 0) {
        }
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_devices, container, false);
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
        mAdapter = new DevicesAdapter(mContext);
        mAdapter.registerAdapterDataObserver(mEmpty);

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);
    }


    private void reqData() {
        List<Devices> list = new ArrayList<>();
        Devices devices1 = new Devices();
        devices1.setStatus(true);
        devices1.setDevicesId("8a9adcd4");
        devices1.setDevicesSn("354243070636996");
        list.add(devices1);
        mAdapter.setList(list);
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
