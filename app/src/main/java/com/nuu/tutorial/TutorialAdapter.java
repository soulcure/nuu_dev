package com.nuu.tutorial;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.nuu.entity.Video;
import com.nuu.nuuinfo.R;
import com.nuu.util.ScreenUtils;

import java.util.List;


public class TutorialAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Video> mList;

    public TutorialAdapter(Context context) {
        mContext = context;
    }


    public void setList(List<Video> list) {
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
        View view = inflater.inflate(R.layout.item_tutorial, parent, false);
        return new TextViewHolder(view);
    }

    // 数据绑定
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        final Video item = mList.get(position);
        View view = viewHolder.itemView;

        TextView tv_title = ((TextViewHolder) viewHolder).tv_title;
        ImageView img_video = ((TextViewHolder) viewHolder).img_video;
        ImageView img_play = ((TextViewHolder) viewHolder).img_play;

        tv_title.setText(item.getTitle());

        final String path = "android.resource://" + mContext.getPackageName() + "/" + item.getResId();

        int padding_width = mContext.getResources().getDimensionPixelOffset(R.dimen.video_padding_width);
        int width = ScreenUtils.getWidthPixels(mContext) - padding_width * 2;
        int height = width * 640 / 360;
        Glide.with(mContext)
                .load(Uri.parse(path))
                .thumbnail(0.5f)
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .override(width, height))
                .into(img_video);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, VideoPlayerActivity.class);
                intent.putExtra("path", path);
                mContext.startActivity(intent);
            }
        });


    }


    private class TextViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title;
        ImageView img_video;
        ImageView img_play;

        private TextViewHolder(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            img_video = (ImageView) itemView.findViewById(R.id.img_video);
            img_play = (ImageView) itemView.findViewById(R.id.img_play);
        }

    }

}

