package com.nuu.http;


public interface DownloadListener {

    void onProgress(int cur, int total);

    void onFail(String err);

    void onSuccess(String path);

}