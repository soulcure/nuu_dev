package com.nuu.my;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nuu.entity.Devices;
import com.nuu.nuuinfo.BasePermissionFragment;
import com.nuu.nuuinfo.R;

import java.util.ArrayList;
import java.util.List;


public class MyPackageFragment extends BasePermissionFragment {

    public final static String TAG = MyPackageFragment.class.getSimpleName();


    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private TabFragmentPagerAdapter mAdapter;
    private FragmentManager fragmentManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null && bundle.size() > 0) {
        }
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_package, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        initView(view);
    }


    private void initView(View view) {

        mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
        mTabLayout = (TabLayout) view.findViewById(R.id.sliding_tabs);
        fragmentManager = getChildFragmentManager();
        mAdapter = new TabFragmentPagerAdapter(fragmentManager);
        mViewPager.setAdapter(mAdapter);

        mTabLayout.setupWithViewPager(mViewPager);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private class TabFragmentPagerAdapter extends FragmentStatePagerAdapter {

        private String[] tabTitle;

        private TabFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
            tabTitle = getResources().getStringArray(R.array.my_package_tab_title);
        }


        @Override
        public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            super.setPrimaryItem(container, position, object);
        }

        @Override
        public Fragment getItem(int arg0) {
            Fragment ft = null;

            switch (arg0) {
                case 0:
                    ft = new HistoryFragment();
                    break;
                case 1:
                    ft = new HistoryFragment();
                    break;
                default:
                    break;
            }

            return ft;
        }


        @Override
        public int getCount() {
            return tabTitle.length;
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitle[position];
        }

    }

}
