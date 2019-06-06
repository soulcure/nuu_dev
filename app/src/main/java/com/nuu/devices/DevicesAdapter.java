package com.nuu.devices;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nuu.entity.Devices;
import com.nuu.nuuinfo.R;

import java.util.List;


public class DevicesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Devices> mList;

    public DevicesAdapter(Context context) {
        mContext = context;
    }


    public void setList(List<Devices> list) {
        mList = list;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }


    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_devices, parent, false);
        return new TextViewHolder(view);
    }

    // 数据绑定
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        final Devices item = mList.get(position);

        ImageView img_status = ((TextViewHolder) viewHolder).img_status;
        ImageView img_devices = ((TextViewHolder) viewHolder).img_devices;
        TextView tv_devices_sn = ((TextViewHolder) viewHolder).tv_devices_sn;
        TextView tv_devices_id = ((TextViewHolder) viewHolder).tv_devices_id;

        if (item.isStatus()) {
            img_status.setImageResource(R.mipmap.ic_green_status);
        } else {
            img_status.setImageResource(R.mipmap.ic_red_status);
        }

        tv_devices_sn.setText(item.getDevicesSn());
        tv_devices_id.setText(item.getDevicesId());

    }


    private class TextViewHolder extends RecyclerView.ViewHolder {
        ImageView img_status;
        ImageView img_devices;
        TextView tv_devices_sn;
        TextView tv_devices_id;


        private TextViewHolder(View itemView) {
            super(itemView);
            img_status = (ImageView) itemView.findViewById(R.id.img_status);
            img_devices = (ImageView) itemView.findViewById(R.id.img_devices);
            tv_devices_sn = (TextView) itemView.findViewById(R.id.tv_devices_sn);
            tv_devices_id = (TextView) itemView.findViewById(R.id.tv_devices_id);
        }

    }


}

