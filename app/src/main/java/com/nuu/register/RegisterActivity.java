package com.nuu.register;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.nuu.country.picker.Country;
import com.nuu.country.picker.CountryPicker;
import com.nuu.country.picker.OnPick;
import com.nuu.country.picker.PickActivity;
import com.nuu.nuuinfo.BaseActivity;
import com.nuu.nuuinfo.R;

public class RegisterActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }


    private void initView() {
        Button btn_test = findViewById(R.id.btn_test);
        btn_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*CountryPicker.newInstance(null, new OnPick() {
                    @Override
                    public void onPick(Country country) {
                        Toast.makeText(mContext, country.toString(), Toast.LENGTH_LONG).show();
                    }
                }).show(getSupportFragmentManager(), "country");*/

                startActivityForResult(new Intent(getApplicationContext(), PickActivity.class), 111);
            }
        });
    }
}
