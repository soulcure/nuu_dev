package com.nuu.config;

public class AppConfig {

    public static final int SEND_BUFFER_SIZE = 1024; //1KB

    /**
     * pref文件名定义
     */
    public static final String SHARED_PREFERENCES = "sdk_app";

    /**
     * HuXin 服务器连接配置
     */
    private final static int LAUNCH_MODE = 0; //0 IM本地服务器        1 IM 50服务器

    private final static String SOCKET_HOST[] = new String[]{"119.23.74.49", "119.23.74.49"};

    private final static int SOCKET_PORT[] = new int[]{18990, 18990};

    public static String getSocketHost() {
        return SOCKET_HOST[LAUNCH_MODE];
    }

    public static int getSocketPort() {
        return SOCKET_PORT[LAUNCH_MODE];
    }

}
