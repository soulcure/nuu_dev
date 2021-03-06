package com.nuu.nuuinfo;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nuu.config.AppConfig;
import com.nuu.entity.CardItem;
import com.nuu.entity.DevicesStatusRsp;
import com.nuu.entity.PackageRsp;
import com.nuu.entity.SettingSsidPwRsp;
import com.nuu.http.IPostListener;
import com.nuu.http.OkHttpConnector;
import com.nuu.pack.PackageDetailByCountryActivity;
import com.nuu.my.PurchasePackageActivity;
import com.nuu.setting.NetSettingActivity;
import com.nuu.user.UserInfoSettingActivity;
import com.nuu.util.AppUtils;
import com.nuu.util.DeviceInfo;
import com.nuu.util.GsonUtil;
import com.nuu.util.ShellUtils;
import com.nuu.util.TimeUtils;
import com.nuu.view.WaveLoadingView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int PACKAGE_USED = 1;  //流量使用情况
    private static final int DEVICES_STATUS = 2; //设备当前状态
    private static final int SETTING_WIFI = 3; //设置wifi账号密码
    private static final int SETTING_USER = 4; //设置设备用户信息
    private static final int REBOOT_DEVICES = 5; //重启设备
    private static final int SHUTDOWN_DEVICES = 6; //关闭设备
    private static final int NET_SETTING = 7; //网络设置
    private static final int WEB_APP = 8; //web 设置

    private static final String STR_PACKAGE_USED = "query_using_package_resp";  //流量使用情况
    private static final String STR_DEVICES_STATUS = "query_device_status_resp"; //设备当前状态查询
    private static final String STR_SETTING_WIFI = "set_device_change_resp"; //设置wifi账号密码
    private static final String STR_SETTING_USER = "device_customer_register"; //设置设备用户信息
    private static final String STR_REBOOT_DEVICES = "reboot_device_resp"; //重启设备
    private static final String STR_SHUTDOWN_DEVICES = "shutdown_device_resp"; //关闭设备
    private static final String STR_NET_SETTING = "net_setting"; //网络设置
    private static final String STR_WEB_APP = "web_app"; //web 设置


    private Context mContext;
    private List<CardItem> mList;

    public CardAdapter(Context context) {
        mContext = context;
        mList = new ArrayList<>();

        CardItem used = new CardItem(STR_PACKAGE_USED);
        CardItem packageType = new CardItem(STR_DEVICES_STATUS);
        CardItem settingWifi = new CardItem(STR_SETTING_WIFI);
        CardItem settingUser = new CardItem(STR_SETTING_USER);
        CardItem reboot = new CardItem(STR_REBOOT_DEVICES);
        CardItem shutdown = new CardItem(STR_SHUTDOWN_DEVICES);
        CardItem netSetting = new CardItem(STR_NET_SETTING);
        CardItem webSetting = new CardItem(STR_WEB_APP);

        mList.add(used);
        mList.add(packageType);
        mList.add(settingWifi);
        mList.add(settingUser);
        mList.add(reboot);
        mList.add(shutdown);
        mList.add(netSetting);
        mList.add(webSetting);
    }

    public void setPackageUsed(PackageRsp rsp) {
        for (CardItem item : mList) {
            if (item.getItfName().equals(STR_PACKAGE_USED)) {
                item.setPackageRsp(rsp);
                break;
            }
        }
        notifyDataSetChanged();
    }


    public void setDevicesStatus(DevicesStatusRsp rsp) {
        for (CardItem item : mList) {
            if (item.getItfName().equals(STR_DEVICES_STATUS)) {
                item.setDevicesStatusRsp(rsp);
                break;
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        // 返回数据总数
        return mList == null ? 0 : mList.size();
    }


    @Override
    public int getItemViewType(int position) {
        CardItem item = mList.get(position);
        int type;
        switch (item.getItfName()) {
            case STR_PACKAGE_USED:
                type = PACKAGE_USED;
                break;
            case STR_DEVICES_STATUS:
                type = DEVICES_STATUS;
                break;
            case STR_SETTING_WIFI:
                type = SETTING_WIFI;
                break;
            case STR_SETTING_USER:
                type = SETTING_USER;
                break;
            case STR_REBOOT_DEVICES:
                type = REBOOT_DEVICES;
                break;
            case STR_SHUTDOWN_DEVICES:
                type = SHUTDOWN_DEVICES;
                break;
            case STR_NET_SETTING:
                type = NET_SETTING;
                break;
            case STR_WEB_APP:
                type = WEB_APP;
                break;
            default:
                type = -1;
                break;
        }
        return type;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view;
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case PACKAGE_USED:
                view = inflater.inflate(R.layout.package_used, parent, false);
                holder = new PackageUsedViewHolder(view);
                break;
            case DEVICES_STATUS:
                view = inflater.inflate(R.layout.devices_stauts, parent, false);
                holder = new DevicesStatusViewHolder(view);
                break;
            case SETTING_WIFI:
                view = inflater.inflate(R.layout.setting_wifi, parent, false);
                holder = new SettingWifiViewHolder(view);
                break;
            case SETTING_USER:
                view = inflater.inflate(R.layout.setting_user, parent, false);
                holder = new SettingUserViewHolder(view);
                break;
            case REBOOT_DEVICES:
                view = inflater.inflate(R.layout.reboot_devices, parent, false);
                holder = new RebootViewHolder(view);
                break;
            case SHUTDOWN_DEVICES:
                view = inflater.inflate(R.layout.shutdown_devices, parent, false);
                holder = new ShutdownViewHolder(view);
                break;
            case NET_SETTING:
                view = inflater.inflate(R.layout.setting_net, parent, false);
                holder = new NetSettingViewHolder(view);
                break;
            case WEB_APP:
                view = inflater.inflate(R.layout.web_app, parent, false);
                holder = new WebAppViewHolder(view);
                break;
            default:
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // 给ViewHolder设置元素
        if (holder instanceof PackageUsedViewHolder) { //流量使用情况
            onPackageUsed((PackageUsedViewHolder) holder, position);
        } else if (holder instanceof DevicesStatusViewHolder) {  //套餐包类型
            onDevicesStatus((DevicesStatusViewHolder) holder, position);
        } else if (holder instanceof SettingWifiViewHolder) {  //设备WIFI账号密码
            onSettingWifi((SettingWifiViewHolder) holder, position);
        } else if (holder instanceof SettingUserViewHolder) {  //设备用户信息
            onSettingUser((SettingUserViewHolder) holder, position);
        } else if (holder instanceof RebootViewHolder) { //重启设备
            onReboot((RebootViewHolder) holder, position);
        } else if (holder instanceof ShutdownViewHolder) { //重启设备
            shutDown((ShutdownViewHolder) holder, position);
        } else if (holder instanceof NetSettingViewHolder) { //重启设备
            netSetting((NetSettingViewHolder) holder, position);
        } else if (holder instanceof WebAppViewHolder) { //重启设备
            webApp((WebAppViewHolder) holder, position);
        }


    }


    private void onPackageUsed(final PackageUsedViewHolder holder, final int position) {
        CardItem item = mList.get(position);
        View view = holder.itemView;
        WaveLoadingView waveLoadingView = holder.waveLoadingView;
        TextView tv_used = holder.tv_used;
        TextView tv_total = holder.tv_total;

        final PackageRsp rsp = item.getPackageRsp();
        if (rsp != null) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, PurchasePackageActivity.class);
                    intent.putParcelableArrayListExtra(PurchasePackageActivity.PACK_LIST, rsp.getPackageX());
                    mContext.startActivity(intent);
                }
            });


            waveLoadingView.setCenterTitle(rsp.percentStr());
            waveLoadingView.setProgressValue(rsp.percent());

            String used = AppUtils.bytes2mb(rsp.dataUsed());
            String data = AppUtils.bytes2mb(rsp.data());

            tv_used.setText(used);
            tv_total.setText(data);
        }

    }

    private void onDevicesStatus(final DevicesStatusViewHolder holder, final int position) {
        CardItem item = mList.get(position);
        DevicesStatusRsp statusRsp = item.getDevicesStatusRsp();

        View view = holder.itemView;
        ImageView img_status = holder.img_status;
        TextView tv_last_time = holder.tv_last_time;
        TextView tv_location = holder.tv_location;

        if (statusRsp != null) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, PackageDetailByCountryActivity.class);
                    mContext.startActivity(intent);
                }
            });


            String status = statusRsp.getStatus();
            if (!TextUtils.isEmpty(status) && status.equals("enable")) {
                img_status.setImageResource(R.mipmap.status_enable);
            } else {
                img_status.setImageResource(R.mipmap.status_disable);
            }

            String lastTime = statusRsp.getLast_heartbeat();
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
                Calendar calendar = TimeUtils.parseDate(lastTime, sdf);
                String newTime = TimeUtils.DEFAULT_DATE_FORMAT.format(calendar.getTime());
                tv_last_time.setText(newTime);
            } catch (ParseException e) {
                tv_last_time.setText(lastTime);
            }


            String location = statusRsp.getLocation();
            tv_location.setText(location);
        }


    }

    private void onSettingWifi(final SettingWifiViewHolder holder, final int position) {
        CardItem item = mList.get(position);

        LinearLayout linear_name = holder.linear_name;
        final TextView tv_wifi_name = holder.tv_wifi_name;

        LinearLayout linear_password = holder.linear_password;
        final TextView tv_wifi_password = holder.tv_wifi_password;
        tv_wifi_password.setTransformationMethod(PasswordTransformationMethod.getInstance());

        getWifiAp(mContext, tv_wifi_name, tv_wifi_password);

        linear_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText et = new EditText(mContext);
                et.setInputType(InputType.TYPE_CLASS_TEXT);

                et.setText(tv_wifi_name.getText().toString());

                et.setSelection(et.getText().toString().length());//将光标移至文字末尾
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("热点名称");
                int space = mContext.getResources().getDimensionPixelOffset(R.dimen.edit_view_space);
                builder.setView(et, space, 0, space, 0);

                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = et.getText().toString();
                        tv_wifi_name.setText(name);

                        reqSetSsidAndPassword(name, tv_wifi_password.getText().toString());
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.create().show();
            }
        });


        linear_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText et = new EditText(mContext);
                et.setInputType(InputType.TYPE_CLASS_TEXT);

                et.setText(tv_wifi_password.getText().toString());

                et.setSelection(et.getText().toString().length());//将光标移至文字末尾
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("热点密码");
                int space = mContext.getResources().getDimensionPixelOffset(R.dimen.edit_view_space);
                builder.setView(et, space, 0, space, 0);

                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String pw = et.getText().toString();
                        tv_wifi_password.setText(pw);

                        reqSetSsidAndPassword(tv_wifi_name.getText().toString(), pw);
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.create().show();
            }
        });


    }


    private void onSettingUser(final SettingUserViewHolder holder, final int position) {
        CardItem item = mList.get(position);

        View view = holder.itemView;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, UserInfoSettingActivity.class);
                mContext.startActivity(intent);
            }
        });

        LinearLayout linear_user = holder.linear_user;
        final TextView tv_user = holder.tv_user;

        LinearLayout linear_owner = holder.linear_owner;
        final TextView tv_owner = holder.tv_owner;

        linear_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText et = new EditText(mContext);
                et.setInputType(InputType.TYPE_CLASS_TEXT);

                et.setText(tv_user.getText().toString());

                et.setSelection(et.getText().toString().length());//将光标移至文字末尾
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Device User");
                int space = mContext.getResources().getDimensionPixelOffset(R.dimen.edit_view_space);
                builder.setView(et, space, 0, space, 0);

                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String user = et.getText().toString();
                        tv_user.setText(user);
                        reqDevicesUser(user);
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.create().show();
            }
        });


        linear_owner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText et = new EditText(mContext);
                et.setInputType(InputType.TYPE_CLASS_TEXT);

                et.setText(tv_owner.getText().toString());

                et.setSelection(et.getText().toString().length());//将光标移至文字末尾
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Device Owner");
                int space = mContext.getResources().getDimensionPixelOffset(R.dimen.edit_view_space);
                builder.setView(et, space, 0, space, 0);

                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String owner = et.getText().toString();
                        tv_owner.setText(owner);
                        reqDevicesOwner(owner);

                    }
                });
                builder.setNegativeButton("取消", null);
                builder.create().show();
            }
        });

    }


    private void onReboot(final RebootViewHolder holder, final int position) {
        Button btn_reboot = holder.btn_reboot;
        btn_reboot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("重启设备？");

                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ShellUtils.execCmd("reboot", false);
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.create().show();
            }
        });
    }

    private void shutDown(final ShutdownViewHolder holder, final int position) {
        Button btn_shutdown = holder.btn_shutdown;
        btn_shutdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("关闭设备？");

                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ShellUtils.execCmd("reboot -p", false);
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.create().show();
            }
        });
    }


    private void netSetting(final NetSettingViewHolder holder, final int position) {
        Button btn_net_setting = holder.btn_net_setting;
        btn_net_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, NetSettingActivity.class);
                mContext.startActivity(intent);
            }
        });
    }


    private void webApp(final WebAppViewHolder holder, final int position) {
        Button btn_web_app = holder.btn_web_app;
        btn_web_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, WebViewActivity.class);
                intent.putExtra(WebViewActivity.INTENT_TITLE, "title");
                intent.putExtra(WebViewActivity.INTENT_URL, "http://localhost:8088");
                mContext.startActivity(intent);
            }
        });
    }


    // 重写的自定义ViewHolder
    static class PackageUsedViewHolder extends RecyclerView.ViewHolder {
        WaveLoadingView waveLoadingView;
        TextView tv_used;
        TextView tv_total;

        PackageUsedViewHolder(View v) {
            super(v);
            waveLoadingView = (WaveLoadingView) v.findViewById(R.id.waveLoadingView);
            tv_used = (TextView) v.findViewById(R.id.tv_used);
            tv_total = (TextView) v.findViewById(R.id.tv_total);
        }
    }

    static class DevicesStatusViewHolder extends RecyclerView.ViewHolder {
        ImageView img_status;
        TextView tv_last_time;
        TextView tv_location;

        DevicesStatusViewHolder(View v) {
            super(v);
            img_status = (ImageView) v.findViewById(R.id.img_status);
            tv_last_time = (TextView) v.findViewById(R.id.tv_last_time);
            tv_location = (TextView) v.findViewById(R.id.tv_location);
        }
    }

    static class SettingWifiViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linear_name;
        TextView tv_wifi_name;
        LinearLayout linear_password;
        TextView tv_wifi_password;

        SettingWifiViewHolder(View v) {
            super(v);
            linear_name = (LinearLayout) v.findViewById(R.id.linear_name);
            tv_wifi_name = (TextView) v.findViewById(R.id.tv_wifi_name);
            linear_password = (LinearLayout) v.findViewById(R.id.linear_password);
            tv_wifi_password = (TextView) v.findViewById(R.id.tv_wifi_password);
        }
    }


    static class SettingUserViewHolder extends RecyclerView.ViewHolder {

        LinearLayout linear_user;
        TextView tv_user;
        LinearLayout linear_owner;
        TextView tv_owner;

        SettingUserViewHolder(View v) {
            super(v);
            linear_user = (LinearLayout) v.findViewById(R.id.linear_user);
            tv_user = (TextView) v.findViewById(R.id.tv_user);
            linear_owner = (LinearLayout) v.findViewById(R.id.linear_owner);
            tv_owner = (TextView) v.findViewById(R.id.tv_owner);
        }
    }

    static class RebootViewHolder extends RecyclerView.ViewHolder {
        Button btn_reboot;

        RebootViewHolder(View v) {
            super(v);
            btn_reboot = (Button) v.findViewById(R.id.btn_reboot);
        }
    }

    static class ShutdownViewHolder extends RecyclerView.ViewHolder {
        Button btn_shutdown;

        ShutdownViewHolder(View v) {
            super(v);
            btn_shutdown = (Button) v.findViewById(R.id.btn_shutdown);
        }
    }


    static class NetSettingViewHolder extends RecyclerView.ViewHolder {
        Button btn_net_setting;

        NetSettingViewHolder(View v) {
            super(v);
            btn_net_setting = (Button) v.findViewById(R.id.btn_net_setting);
        }
    }


    static class WebAppViewHolder extends RecyclerView.ViewHolder {
        Button btn_web_app;

        WebAppViewHolder(View v) {
            super(v);
            btn_web_app = (Button) v.findViewById(R.id.btn_web_app);
        }
    }


    private void reqSetSsidAndPassword(final String ssid, final String password) {
        String url = AppConfig.getHost();

        ContentValues params = new ContentValues();
        params.put("itf_name", "device_wifi_setup");  //API name
        params.put("trans_serial", "1234cde");  //API name
        params.put("login", "tuser");
        params.put("auth_code", "abcd456");
        params.put("device_sn", "354243074362656");

        params.put("ssid", ssid);
        params.put("wifi_password", password);

        OkHttpConnector.httpPost(url, params, new IPostListener() {
            @Override
            public void httpReqResult(String response) {
                SettingSsidPwRsp rsp = GsonUtil.parse(response, SettingSsidPwRsp.class);
                if (rsp != null && rsp.getErr_code() == 0) {
                    DeviceInfo.setWifiAp(mContext, ssid, password);
                } else if (rsp != null && !TextUtils.isEmpty(rsp.getErr_desc())) {
                    Toast.makeText(mContext, rsp.getErr_desc(), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


    private static void getWifiAp(Context context, TextView tvSsid, TextView tvPw) {
        try {
            Context app = context.getApplicationContext();
            WifiManager wifiManager = (WifiManager) app.getSystemService(Context.WIFI_SERVICE);
            java.lang.reflect.Method getConfigMethod = wifiManager.getClass().getMethod("getWifiApConfiguration");
            WifiConfiguration wifiConfig = (WifiConfiguration) getConfigMethod.invoke(wifiManager);

            String ssid = wifiConfig.SSID;
            String realPwd = wifiConfig.preSharedKey;

            if (tvSsid != null) {
                tvSsid.setText(ssid);
            }
            if (tvPw != null) {
                tvPw.setText(realPwd);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void reqDevicesUser(String user) {
        String url = AppConfig.getHost();

        ContentValues params = new ContentValues();
        params.put("itf_name", "set_device_user");  //API name
        params.put("trans_serial", "1234cde");  //API name
        params.put("login", "tuser");
        params.put("auth_code", "abcd456");
        params.put("device_sn", "354243074362656");
        params.put("user_name", user);

        OkHttpConnector.httpPost(url, params, new IPostListener() {
            @Override
            public void httpReqResult(String response) {
                DevicesStatusRsp rsp = GsonUtil.parse(response, DevicesStatusRsp.class);
            }
        });

    }


    private void reqDevicesOwner(String owner) {
        String url = AppConfig.getHost();

        ContentValues params = new ContentValues();
        params.put("itf_name", "set_device_user");  //API name
        params.put("trans_serial", "1234cde");  //API name
        params.put("login", "tuser");
        params.put("auth_code", "abcd456");
        params.put("device_sn", "354243074362656");
        params.put("owner_name", owner);

        OkHttpConnector.httpPost(url, params, new IPostListener() {
            @Override
            public void httpReqResult(String response) {
                DevicesStatusRsp rsp = GsonUtil.parse(response, DevicesStatusRsp.class);
            }
        });

    }
}