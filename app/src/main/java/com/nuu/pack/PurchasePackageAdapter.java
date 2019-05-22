package com.nuu.pack;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nuu.entity.PackageRsp;
import com.nuu.nuuinfo.R;
import com.nuu.util.AppUtils;

import java.util.List;


public class PurchasePackageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<PackageRsp.PackageBean> mList;

    public PurchasePackageAdapter(Context context, List<PackageRsp.PackageBean> list) {
        mContext = context;
        mList = list;
    }


    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }


    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_package, parent, false);
        return new TextViewHolder(view);
    }

    // 数据绑定
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        final PackageRsp.PackageBean item = mList.get(position);

        TextView tv_name = ((TextViewHolder) viewHolder).tv_name;
        TextView tv_buy_time = ((TextViewHolder) viewHolder).tv_buy_time;
        TextView tv_start = ((TextViewHolder) viewHolder).tv_start;
        TextView tv_end = ((TextViewHolder) viewHolder).tv_end;
        ImageView img_status = ((TextViewHolder) viewHolder).img_status;
        TextView tv_used = ((TextViewHolder) viewHolder).tv_used;
        TextView tv_total = ((TextViewHolder) viewHolder).tv_total;

        tv_name.setText(item.getPackage_name());
        tv_buy_time.setText(item.getOrder_time());
        tv_start.setText(item.getBegin_date());
        tv_end.setText(item.getEnd_date());
        tv_used.setText(AppUtils.bytes2mb(item.getData_used()));
        tv_total.setText(AppUtils.bytes2mb(item.getData()));

        if (item.getStatus() == 1) {
            img_status.setEnabled(true);
        } else {
            img_status.setEnabled(false);
        }

    }


    private class TextViewHolder extends RecyclerView.ViewHolder {

        TextView tv_name;
        TextView tv_buy_time;
        TextView tv_start;
        TextView tv_end;
        ImageView img_status;
        TextView tv_used;
        TextView tv_total;


        private TextViewHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_buy_time = (TextView) itemView.findViewById(R.id.tv_buy_time);
            tv_start = (TextView) itemView.findViewById(R.id.tv_start);
            tv_end = (TextView) itemView.findViewById(R.id.tv_end);
            img_status = (ImageView) itemView.findViewById(R.id.img_status);
            tv_used = (TextView) itemView.findViewById(R.id.tv_used);
            tv_total = (TextView) itemView.findViewById(R.id.tv_total);
        }

    }


}

