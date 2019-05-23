package com.nuu.data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nuu.entity.DetailRsp;
import com.nuu.nuuinfo.R;
import com.nuu.util.AppUtils;

import java.util.List;


public class UsedDataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<DetailRsp.UsedDtlBean> mList;

    public UsedDataAdapter(Context context) {
        mContext = context;
    }

    public void setList(List<DetailRsp.UsedDtlBean> list) {
        this.mList = list;
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
        View view = inflater.inflate(R.layout.used_data, parent, false);
        return new TextViewHolder(view);
    }

    // 数据绑定
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        final DetailRsp.UsedDtlBean item = mList.get(position);

        TextView tv_country = ((TextViewHolder) viewHolder).tv_country;
        TextView tv_used = ((TextViewHolder) viewHolder).tv_used;

        tv_country.setText(item.getCountry());
        tv_used.setText(AppUtils.bytes2mb(item.getUsed_data()));
    }


    private class TextViewHolder extends RecyclerView.ViewHolder {
        TextView tv_country;
        TextView tv_used;
        TextView tv_total;

        private TextViewHolder(View itemView) {
            super(itemView);
            tv_country = (TextView) itemView.findViewById(R.id.tv_country);
            tv_used = (TextView) itemView.findViewById(R.id.tv_used);
        }

    }


}

