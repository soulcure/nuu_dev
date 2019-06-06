package com.nuu.news;

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
import com.nuu.nuuinfo.BasePermissionFragment;
import com.nuu.nuuinfo.R;

import java.util.ArrayList;
import java.util.List;


public class NewsFragment extends BasePermissionFragment {

    public final static String TAG = NewsFragment.class.getSimpleName();


    private LinearLayout mEmptyView;
    private EmptyRecyclerViewDataObserver mEmpty = new EmptyRecyclerViewDataObserver();

    private NewsAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null && bundle.size() > 0) {
        }
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news, container, false);
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
        mAdapter = new NewsAdapter(mContext);
        mAdapter.registerAdapterDataObserver(mEmpty);

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);
    }


    private void reqData() {
        List<News> list = new ArrayList<>();
        News news1 = new News();
        news1.setTitle("Paul Manafort to be transferred to New York City's notorious Rikers Island, source says");
        news1.setTime("2019-04-12 18:35:30");
        news1.setContent("Nader, who reportedly has ties to former aides to President Trump and has served as an adviser to Saudi Arabia and the United Arab Emirates (UAE), is accused of transporting a dozen images of child pornography and bestiality, according to federal prosecutors who announced that the 60-year-old was arrested Monday morning at John F. Kennedy International Airport in New York.");


        News news2 = new News();
        news2.setTitle("Nader pleaded guilty to a federal child-pornography");
        news2.setTime("2019-05-16 19:35:50");
        news2.setContent("George Nader, a Lebanese-American businessman who cooperated with Special Counsel Robert Mueller’s investigation and whose name appeared more than 100 times in Mueller’s Russia report, was arrested Monday on charges of transporting child pornography.");

        News news3 = new News();
        news3.setTitle("Sponsored Stories You May Like");
        news3.setTime("2019-05-17 19:38:50");
        news3.setContent("Nader’s testimony to the grand jury came after he reportedly took part in a December 2016 meeting at New York's Trump Tower with President Trump’s son-in-law Jared Kushner, former chief strategist Steve Bannon and Mohammed bin Zayed, crown prince of Abu Dhabi. Nader purportedly has been an adviser to Zayed.");

        News news4 = new News();
        news4.setTitle("CLICK HERE TO GET THE FOX NEWS APP");
        news4.setTime("2019-05-18 19:55:50");
        news4.setContent("Nader was cited extensively in the sections of the Mueller report which detailed his work arranging a January 2017 meeting in the Seychelles between Trump associate Erik Prince and Kirill Dmitriev., a Russian businessman with close ties to President Vladimir Putin.");

        list.add(news1);
        list.add(news2);
        list.add(news3);
        list.add(news4);
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
