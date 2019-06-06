package com.nuu.news;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nuu.entity.News;
import com.nuu.nuuinfo.R;

import java.util.List;


public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<News> mList;

    public NewsAdapter(Context context) {
        mContext = context;
    }


    public void setList(List<News> list) {
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
        View view = inflater.inflate(R.layout.item_news, parent, false);
        return new TextViewHolder(view);
    }

    // 数据绑定
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        final News item = mList.get(position);

        TextView tv_title = ((TextViewHolder) viewHolder).tv_title;
        TextView tv_time = ((TextViewHolder) viewHolder).tv_time;
        TextView tv_content = ((TextViewHolder) viewHolder).tv_content;

        tv_title.setText(item.getTitle());
        tv_time.setText(item.getTime());
        tv_content.setText(item.getContent());
    }


    private class TextViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title;
        TextView tv_time;
        TextView tv_content;


        private TextViewHolder(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_content = (TextView) itemView.findViewById(R.id.tv_content);
        }

    }


}

