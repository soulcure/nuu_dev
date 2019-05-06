package cn.gosomo.proxy;

import cn.gosomo.proxy.IProxyCallback;

interface IProxyCall {
    // master
    boolean downloadData(in byte[] data, int len);
    boolean eraseFactory(String command);
    boolean updateFactory(int board, String filepath);

    // exec cmd [fg: running foregroud]
    boolean execCommand(String command, int fg);

    // check self
    boolean checkSelf(int board);

    // system time
    boolean setTime(int board, long time);
    long getTime(int board);

    // version/status
    String getVersion(int board, int hwSw);
    int getStatus(int board, int hwSw);

    // reboot(true: reboot; false: shutdown)
    boolean reboot(int board, boolean reboot);
    // enable(true: suspend; false: resume)
    void suspend(int board, boolean enable);

    void setProp(String name, String value);
    String getProp(String name, String defValue);

    // nv
    void nvRead(int itemId, int oft, out byte[] bytes);
    void nvWrite(int itemId, in byte[] bytes);

    void nvReadExt(int itemId, int slot, int oft, out byte[] bytes);

    // callback
    void registerCallback(String pkgName,
                    IProxyCallback callback);

    void unregisterCallback(String pkgName,
                    IProxyCallback callback);

    boolean accoutRecharged();
}
