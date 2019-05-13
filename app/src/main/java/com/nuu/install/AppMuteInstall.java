package com.nuu.install;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.content.Context;
import android.content.pm.IPackageInstallObserver;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

public class AppMuteInstall {

    private static final String TAG = "TcpClient";

    private static final int INSTALL_REPLACE_EXISTING = 2;


    private PackageInstallObserver observer;
    private PackageManager pm;
    private Method method;

    private OnInstalledPackaged onInstalledPackaged;

    class PackageInstallObserver extends IPackageInstallObserver.Stub {

        @Override
        public void packageInstalled(String packageName, int resultCode) throws RemoteException {
            Log.d("TcpClient", "packageInstalled = " + packageName + "; resultCode = " + resultCode);
            if (onInstalledPackaged != null) {
                onInstalledPackaged.packageInstalled(packageName, resultCode);
            }
        }
    }


    public AppMuteInstall(Context context) throws SecurityException, NoSuchMethodException {
        observer = new PackageInstallObserver();
        pm = context.getPackageManager();
        Class<?>[] types = new Class[]{Uri.class, IPackageInstallObserver.class, int.class, String.class};
        method = pm.getClass().getMethod("installPackage", types);
    }

    public void setOnInstalledPackaged(OnInstalledPackaged onInstalledPackaged) {
        this.onInstalledPackaged = onInstalledPackaged;
    }

    public void installPackage(String apkFile) {
        installPackage(new File(apkFile));
    }

    public void installPackage(File apkFile) {
        if (!apkFile.exists()) {
            return;
        }
        Uri packageURI = Uri.fromFile(apkFile);
        installPackage(packageURI);
    }

    public void installPackage(Uri apkFile) {
        try {
            method.invoke(pm, new Object[]{apkFile, observer, INSTALL_REPLACE_EXISTING, null});
        } catch (IllegalArgumentException e) {
            Log.e(TAG, e.getMessage());
        } catch (IllegalAccessException e) {
            Log.e(TAG, e.getMessage());
        } catch (InvocationTargetException e) {
            Log.e(TAG, e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }


    public static void installPackage(Context context, String filePath) {
        try {
            PackageManager packageManager = context.getPackageManager();
            Class<?>[] types = new Class[]{Uri.class, IPackageInstallObserver.class, int.class, String.class};
            Method method = packageManager.getClass().getMethod("installPackage", types);

            File apkFile = new File(filePath);
            Uri apkUri = Uri.fromFile(apkFile);

            method.invoke(packageManager,
                    new Object[]{apkUri,
                            new IPackageInstallObserver.Stub() {
                                @Override
                                public void packageInstalled(String pkgName, int resultCode) throws RemoteException {
                                    Log.d(TAG, "packageInstalled = " + pkgName + "; resultCode = " + resultCode);
                                }
                            },
                            INSTALL_REPLACE_EXISTING, null});
        } catch (SecurityException e) {
            Log.e(TAG, e.getMessage());
        } catch (NoSuchMethodException e) {
            Log.e(TAG, e.getMessage());
        } catch (IllegalArgumentException e) {
            Log.e(TAG, e.getMessage());
        } catch (IllegalAccessException e) {
            Log.e(TAG, e.getMessage());
        } catch (InvocationTargetException e) {
            Log.e(TAG, e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }


    public static boolean installByPm(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return false;
        }
        boolean result = false;
        Process process = null;
        OutputStream outputStream = null;
        BufferedReader errorStream = null;
        try {
            process = Runtime.getRuntime().exec("su");
            outputStream = process.getOutputStream();

            String command = "pm install -r " + filePath + "\n";
            outputStream.write(command.getBytes());
            outputStream.flush();
            outputStream.write("exit\n".getBytes());
            outputStream.flush();
            process.waitFor();
            errorStream = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            StringBuilder msg = new StringBuilder();
            String line;
            while ((line = errorStream.readLine()) != null) {
                msg.append(line);
            }
            Log.d(TAG, "install msg is " + msg);
            if (!msg.toString().contains("Failure")) {
                result = true;
                Log.d(TAG, "pm install success");
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            result = false;
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
                if (errorStream != null) {
                    errorStream.close();
                }
            } catch (IOException e) {
                outputStream = null;
                errorStream = null;
                process.destroy();
            }
        }
        return result;
    }


}
