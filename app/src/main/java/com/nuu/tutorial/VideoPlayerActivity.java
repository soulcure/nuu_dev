package com.nuu.tutorial;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import com.nuu.nuuinfo.BaseActivity;
import com.nuu.nuuinfo.R;


public class VideoPlayerActivity extends BaseActivity {

    private VideoView videoView;
    private MediaController mediaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_video);

        String path = getIntent().getStringExtra("path");

        videoView = (VideoView) findViewById(R.id.video);

        mediaController = new MediaController(mContext);

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                videoView.start();
                mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                        /*
                         * add media controller
                         */
                        videoView.setMediaController(mediaController);

                        /*
                         * and set its position on screen
                         */
                        mediaController.setAnchorView(videoView);

                    }
                });

            }
        });


        videoView.setVideoURI(Uri.parse(path));

    }


    @Override
    protected void onStop() {
        super.onStop();
        videoView.pause();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
