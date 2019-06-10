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

import com.nuu.config.AppConfig;
import com.nuu.drawable.OneDrawable;
import com.nuu.entity.ReportData;
import com.nuu.http.IGetListener;
import com.nuu.http.OkHttpConnector;
import com.nuu.nuuinfo.BasePermissionFragment;
import com.nuu.nuuinfo.R;
import com.nuu.util.GsonUtil;
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
        reqData();
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


    private void reqData() {
        String url = AppConfig.DEVICE_INFO;
        OkHttpConnector.httpGet(url, new IGetListener() {
            @Override
            public void httpReqResult(String response) {
                ReportData data = GsonUtil.parse(response, ReportData.class);
                if (data != null) {
                    int point = data.getHotPoint();
                    String connect = String.format(getString(R.string.net_connect), point);
                    tv_connect.setText(connect);

                    int power = data.getPow();
                    wv_power.setProgressValue(power);
                    wv_power.setCenterTitle(power + "%");

                    if (data.getSim1() != null) {
                        int signal = onSignalStrength(data.getSim1().getSignal());
                        if (signal == 4) {
                            img_signal.setImageResource(R.mipmap.signal_4);
                        } else if (signal == 3) {
                            img_signal.setImageResource(R.mipmap.signal_3);
                        } else if (signal == 2) {
                            img_signal.setImageResource(R.mipmap.signal_2);
                        } else if (signal == 1) {
                            img_signal.setImageResource(R.mipmap.signal_1);
                        } else if (signal == 0) {
                            img_signal.setImageResource(R.mipmap.signal_0);
                        }

                    }
                }
            }
        });

    }


    public int onSignalStrength(int dbm) {
        int res;
        if (dbm >= -75) {
            res = 4;
        } else if (dbm >= -85) {
            res = 3;
        } else if (dbm >= -95) {
            res = 2;
        } else if (dbm >= -100) {
            res = 1;
        } else {
            res = 0;
        }
        return res;
    }


}
