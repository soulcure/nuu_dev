package com.nuu.register;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.nuu.country.picker.CountryPicker;
import com.nuu.country.picker.OnPick;
import com.nuu.country.picker.PickActivity;
import com.nuu.nuuinfo.BaseActivity;
import com.nuu.nuuinfo.R;

import net.rimoto.intlphoneinput.Country;
import net.rimoto.intlphoneinput.IntlPhoneInput;

public class RegisterActivity extends BaseActivity {
    IntlPhoneInput phoneInputView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }


    private void initView() {
        Button btn_register = findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String myInternationalNumber;
                if (phoneInputView.isValid()) {
                    myInternationalNumber = phoneInputView.getNumber();
                    Country country = phoneInputView.getSelectedCountry();

                    Log.d("test", myInternationalNumber + "@" + country.getIso() + "@" + country.getName() + "@" + country.getDialCode());
                }
            }
        });


        phoneInputView = (IntlPhoneInput) findViewById(R.id.phone_input);

        phoneInputView.setOnValidityChange(new IntlPhoneInput.IntlPhoneInputListener() {
            @Override
            public void done(View view, boolean isValid) {

            }
        });
    }
}
