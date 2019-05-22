package com.nuu.packinfo;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nuu.entity.PackageDetailByCountryRsp;
import com.nuu.nuuinfo.R;
import com.nuu.util.AppUtils;

import java.util.List;


public class PackageDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<PackageDetailByCountryRsp.PackageBean> mList;

    public PackageDetailAdapter(Context context) {
        mContext = context;
    }

    public void setList(List<PackageDetailByCountryRsp.PackageBean> list) {
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
        View view = inflater.inflate(R.layout.package_detail, parent, false);
        return new TextViewHolder(view);
    }

    // 数据绑定
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        final PackageDetailByCountryRsp.PackageBean item = mList.get(position);
        View view = viewHolder.itemView;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, BuyPackageActivity.class);
                intent.putExtra(BuyPackageActivity.PACKAGE_ID, item.getPackage_id());
                mContext.startActivity(intent);
            }
        });
        TextView tv_name = ((TextViewHolder) viewHolder).tv_name;
        TextView tv_currency = ((TextViewHolder) viewHolder).tv_currency;
        TextView tv_price = ((TextViewHolder) viewHolder).tv_price;
        TextView tv_data = ((TextViewHolder) viewHolder).tv_data;
        TextView tv_cycle_type = ((TextViewHolder) viewHolder).tv_cycle_type;
        TextView tv_cycle = ((TextViewHolder) viewHolder).tv_cycle;
        TextView tv_time_zone = ((TextViewHolder) viewHolder).tv_time_zone;
        TextView tv_country = ((TextViewHolder) viewHolder).tv_country;

        tv_name.setText(item.getPackage_name());
        tv_currency.setText(item.getCurrency());
        tv_price.setText(item.getCost());
        tv_data.setText(AppUtils.bytes2mb(item.getData()));
        tv_cycle_type.setText(item.getCycle_time_type());
        tv_cycle.setText(item.getCycle_time());
        tv_time_zone.setText(item.getTime_zone());

        List<String> list = item.getCountry();
        if (list != null && list.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < list.size(); i++) {
                sb.append(list.get(i));
                if (i < list.size() - 1) {
                    sb.append(",");
                }
            }
            tv_country.setText(sb.toString());
        }

    }


    private class TextViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name;
        TextView tv_currency;
        TextView tv_price;
        TextView tv_data;
        TextView tv_cycle_type;
        TextView tv_cycle;
        TextView tv_time_zone;
        TextView tv_country;


        private TextViewHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_currency = (TextView) itemView.findViewById(R.id.tv_currency);
            tv_price = (TextView) itemView.findViewById(R.id.tv_price);
            tv_data = (TextView) itemView.findViewById(R.id.tv_data);
            tv_cycle_type = (TextView) itemView.findViewById(R.id.tv_cycle_type);
            tv_cycle = (TextView) itemView.findViewById(R.id.tv_cycle);
            tv_time_zone = (TextView) itemView.findViewById(R.id.tv_time_zone);
            tv_country = (TextView) itemView.findViewById(R.id.tv_country);
        }

    }


}

