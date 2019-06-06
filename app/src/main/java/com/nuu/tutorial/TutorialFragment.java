package com.nuu.tutorial;

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

import com.nuu.entity.Video;
import com.nuu.nuuinfo.BasePermissionFragment;
import com.nuu.nuuinfo.R;

import java.util.ArrayList;
import java.util.List;


public class TutorialFragment extends BasePermissionFragment {

    public final static String TAG = TutorialFragment.class.getSimpleName();


    private LinearLayout mEmptyView;
    private EmptyRecyclerViewDataObserver mEmpty = new EmptyRecyclerViewDataObserver();

    private TutorialAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null && bundle.size() > 0) {
        }
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tutorial, container, false);
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
        mAdapter = new TutorialAdapter(mContext);
        mAdapter.registerAdapterDataObserver(mEmpty);

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);
    }


    private void reqData() {
        List<Video> list = new ArrayList<>();
        Video video1 = new Video();
        video1.setTitle("NUU Konnect basic operation");
        video1.setResId(R.raw.on_off_reset);

        Video video2 = new Video();
        video2.setTitle("how to connect NUU Konnect i1");
        video2.setResId(R.raw.connect_wifi);

        Video video3 = new Video();
        video3.setTitle("how to buy package");
        video3.setResId(R.raw.buy_package);

        list.add(video1);
        list.add(video2);
        list.add(video3);
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
