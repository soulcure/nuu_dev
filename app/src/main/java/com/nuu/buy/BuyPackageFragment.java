package com.nuu.buy;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blikoon.qrcodescanner.QrCodeActivity;
import com.nuu.drawable.OneDrawable;
import com.nuu.nuuinfo.BasePermissionFragment;
import com.nuu.nuuinfo.MainActivity;
import com.nuu.nuuinfo.R;
import com.nuu.view.WaveLoadingView;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;


public class BuyPackageFragment extends BasePermissionFragment implements View.OnClickListener {

    public final static String TAG = BuyPackageFragment.class.getSimpleName();

    private static final int REQUEST_CODE_QR_SCAN = 101;


    private static final int RC_CAMERA_PERMISSIONS = 1;

    private EditText et_sn;
    private ImageView img_search;
    private ImageView img_scan;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null && bundle.size() > 0) {
        }
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_package_buy, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        initView(view);
    }


    private void initView(View view) {
        et_sn = view.findViewById(R.id.et_sn);
        img_search = view.findViewById(R.id.img_search);

        Drawable search = OneDrawable.createBgDrawableWithDarkMode(mContext, R.mipmap.ic_search_sn, 0.4f);
        img_search.setImageDrawable(search);
        img_search.setOnClickListener(this);

        img_scan = view.findViewById(R.id.img_scan);
        Drawable scan = OneDrawable.createBgDrawableWithDarkMode(mContext, R.mipmap.ic_scan_sn, 0.4f);
        img_scan.setImageDrawable(scan);
        img_scan.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.img_search:
                break;
            case R.id.img_scan:
                checkPermission();
                break;
        }

    }


    @AfterPermissionGranted(RC_CAMERA_PERMISSIONS)
    private void checkPermission() {
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.VIBRATE};
        if (EasyPermissions.hasPermissions(mContext, perms)) {
            // Already have permission, do the thing
            Intent i = new Intent(mContext, QrCodeActivity.class);
            startActivityForResult(i, REQUEST_CODE_QR_SCAN);
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, getString(R.string.need_camera_permissions),
                    RC_CAMERA_PERMISSIONS, perms);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            Log.d(TAG, "COULD NOT GET A GOOD RESULT.");
            if (data == null)
                return;
            //Getting the passed result
            String result = data.getStringExtra("com.blikoon.qrcodescanner.error_decoding_image");
            if (result != null) {
                AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
                alertDialog.setTitle("Scan Error");
                alertDialog.setMessage("QR Code could not be scanned");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
            return;

        }
        if (requestCode == REQUEST_CODE_QR_SCAN) {
            if (data == null)
                return;
            //Getting the passed result
            String result = data.getStringExtra("com.blikoon.qrcodescanner.got_qr_scan_relult");
            Log.d(TAG, "scan qr code SN:" + result);
            et_sn.setText(result);
        }
    }


}
