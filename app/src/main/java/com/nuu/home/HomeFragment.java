package com.nuu.home;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nuu.drawable.OneDrawable;
import com.nuu.nuuinfo.BasePermissionFragment;
import com.nuu.nuuinfo.R;
import com.nuu.view.WaveLoadingView;


public class HomeFragment extends BasePermissionFragment implements View.OnClickListener {

    public final static String TAG = HomeFragment.class.getSimpleName();

    private WaveLoadingView wv_power;
    private WaveLoadingView wv_used;
    private ImageView img_signal;
    private ImageView img_refresh;
    private TextView tv_connect;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null && bundle.size() > 0) {
        }
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        initView(view);
    }


    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private void initView(View view) {
        wv_power = (WaveLoadingView) view.findViewById(R.id.wv_power);
        wv_used = (WaveLoadingView) view.findViewById(R.id.wv_used);
        img_signal = (ImageView) view.findViewById(R.id.img_signal);
        img_refresh = (ImageView) view.findViewById(R.id.img_refresh);
        tv_connect = (TextView) view.findViewById(R.id.tv_connect);

        Drawable drawable = OneDrawable.createBgDrawableWithDarkMode(mContext, R.mipmap.btn_refresh, 0.4f);
        img_refresh.setImageDrawable(drawable);
    }


    /**
     * 用户登录
     *
     * @param mobile   手机号
     * @param password 密码
     * @param imei     设备编码
     */
    private void login(final String mobile,
                       final String password, final String imei, final String phoneMac) {

    }


    private void choiceReBindDialog(final String mobile) {

    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tv_connect:
            break;
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //thirdLogin.onActivityResult(requestCode, resultCode, data); //第三方登录
    }


}
