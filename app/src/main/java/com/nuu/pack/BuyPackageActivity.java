package com.nuu.pack;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nuu.buy.PayPalActivity;
import com.nuu.config.AppConfig;
import com.nuu.entity.BuyPackageRsp;
import com.nuu.http.IPostListener;
import com.nuu.http.OkHttpConnector;
import com.nuu.nuuinfo.BaseActivity;
import com.nuu.nuuinfo.R;
import com.nuu.util.GsonUtil;
import com.paypal.android.sdk.payments.PayPalAuthorization;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalFuturePaymentActivity;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalProfileSharingActivity;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class BuyPackageActivity extends BaseActivity implements View.OnClickListener {

    public static final String TAG = BuyPackageActivity.class.getSimpleName();

    public static final String PACKAGE_ID = "package_id";

    private Spinner spinner;
    private EditText et_num;
    private Button btn_data;
    private Button btn_commit;

    private int valid_type;
    private LinearLayout linear_type;

    /**
     * - Set to PayPalConfiguration.ENVIRONMENT_PRODUCTION to move real money.
     * <p>
     * - Set to PayPalConfiguration.ENVIRONMENT_SANDBOX to use your test credentials
     * from https://developer.paypal.com
     * <p>
     * - Set to PayPalConfiguration.ENVIRONMENT_NO_NETWORK to kick the tires
     * without communicating to PayPal's servers.
     */

    //配置何种支付环境，一般沙盒，正式
    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;

    //你所注册的APP Id
    // note that these credentials will differ between live & sandbox environments.
    private static final String CONFIG_CLIENT_ID = "ASskKGQjRAf-6jAdwn771epAcx7C_dDNBGH-SMtjbo9xAlbV-D7Ah695YLTdllnRCPklUZdjjH1mlTcW";

    private static final int REQUEST_CODE_PAYMENT = 1;
    private static final int REQUEST_CODE_FUTURE_PAYMENT = 2;
    private static final int REQUEST_CODE_PROFILE_SHARING = 3;

    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(CONFIG_ENVIRONMENT)
            .clientId(CONFIG_CLIENT_ID);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.package_buy_activity);
        initView();

        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_back) {
            onBackPressed();
        } else if (id == R.id.btn_data) {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog.OnDateSetListener callBack = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    Calendar c = Calendar.getInstance();
                    c.set(year, monthOfYear, dayOfMonth);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
                    String data = sdf.format(c.getTime());
                    btn_data.setText(data);
                }
            };
            DatePickerDialog dialog = new DatePickerDialog(mContext, callBack,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
            DatePicker datePicker = dialog.getDatePicker();
            datePicker.setMinDate(calendar.getTimeInMillis());

            calendar.add(Calendar.YEAR, 1);
            datePicker.setMaxDate(calendar.getTimeInMillis());//设置日期的上限日期
            dialog.show();
        } else if (id == R.id.btn_commit) {
            int packageId = getIntent().getIntExtra(PACKAGE_ID, 0);
            String beginData = btn_data.getText().toString();
            String total = et_num.getText().toString();
            int num;
            try {
                num = Integer.parseInt(total);
            } catch (NumberFormatException e) {
                num = 0;
                e.printStackTrace();
            }

            if (packageId != 0) {
                onBuyPressed();
                //reqBuyPackage(packageId, beginData, num, valid_type);
            } else {
                Toast.makeText(mContext, "package id error", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void initView() {
        TextView tv_back = (TextView) findViewById(R.id.tv_back);
        tv_back.setOnClickListener(this);

        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("package info");

        ImageView img_right = (ImageView) findViewById(R.id.img_right);
        img_right.setVisibility(View.INVISIBLE);

        et_num = (EditText) findViewById(R.id.et_num);
        et_num.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String total = s.toString();
                int num;
                try {
                    num = Integer.parseInt(total);
                } catch (NumberFormatException e) {
                    num = 0;
                    e.printStackTrace();
                }

                if (num == 0) {
                    linear_type.setVisibility(View.INVISIBLE);
                    btn_commit.setEnabled(false);
                } else if (num == 1) {
                    linear_type.setVisibility(View.INVISIBLE);
                    btn_commit.setEnabled(true);
                } else if (num > 1) {
                    linear_type.setVisibility(View.VISIBLE);
                    btn_commit.setEnabled(true);
                }

            }
        });
        btn_data = (Button) findViewById(R.id.btn_data);
        btn_data.setOnClickListener(this);

        btn_commit = (Button) findViewById(R.id.btn_commit);
        btn_commit.setOnClickListener(this);


        linear_type = (LinearLayout) findViewById(R.id.linear_type);
        spinner = (Spinner) findViewById(R.id.spinner);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                valid_type = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void onBuyPressed() {
        /*
         * PAYMENT_INTENT_SALE will cause the payment to complete immediately.
         * Change PAYMENT_INTENT_SALE to
         *   - PAYMENT_INTENT_AUTHORIZE to only authorize payment and capture funds later.
         *   - PAYMENT_INTENT_ORDER to create a payment for authorization and capture
         *     later via calls from your server.
         *
         * Also, to include additional payment details and an item list, see getStuffToBuy() below.
         */
        PayPalPayment thingToBuy = getThingToBuy(PayPalPayment.PAYMENT_INTENT_SALE);

        /*
         * See getStuffToBuy(..) for examples of some available payment options.
         */

        Intent intent = new Intent(this, PaymentActivity.class);

        // send the same configuration for restart resiliency
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);

        startActivityForResult(intent, REQUEST_CODE_PAYMENT);
    }

    private PayPalPayment getThingToBuy(String paymentIntent) {
        return new PayPalPayment(new BigDecimal("0.01"), "USD", "sample item",
                paymentIntent);
    }

    private void reqBuyPackage(int packageId, String beginData, int total_num, int valid_type) {
        String url = AppConfig.getHost();

        ContentValues params = new ContentValues();
        params.put("itf_name", "buy_package");  //API name
        params.put("trans_serial", "1234cde");  //API name
        params.put("login", "tuser");
        params.put("auth_code", "abcd456");
        params.put("device_sn", "354243074362656");
        params.put("package_id", packageId);
        params.put("begin_date", beginData);
        params.put("total_num", total_num);
        params.put("valid_type", valid_type);
        showProgressDialog();

        OkHttpConnector.httpPost(url, params, new IPostListener() {
            @Override
            public void httpReqResult(String response) {
                BuyPackageRsp rsp = GsonUtil.parse(response, BuyPackageRsp.class);
                if (rsp != null && rsp.getErr_code() == 0) {
                    Toast.makeText(mContext, "buy package success", Toast.LENGTH_SHORT).show();
                } else if (rsp != null && !TextUtils.isEmpty(rsp.getErr_desc())) {
                    Toast.makeText(mContext, rsp.getErr_desc(), Toast.LENGTH_SHORT).show();
                }
                dismissProgressDialog();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm =
                        data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        String paymentId = confirm.getProofOfPayment().getPaymentId();
                        PayPalPayment payPalPayment = confirm.getPayment();

                        Log.i(TAG, confirm.toJSONObject().toString(4));
                        Log.i(TAG, confirm.getPayment().toJSONObject().toString(4));
                        /**
                         *  TODO: send 'confirm' (and possibly confirm.getPayment() to your server for verification
                         * or consent completion.
                         * See https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/
                         * for more details.
                         *
                         * For sample mobile backend interactions, see
                         * https://github.com/paypal/rest-api-sdk-python/tree/master/samples/mobile_backend
                         */
                        Toast.makeText(this,
                                "PaymentConfirmation info received from PayPal", Toast.LENGTH_LONG)
                                .show();


                    } catch (JSONException e) {
                        Log.e(TAG, "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i(TAG, "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i(
                        TAG,
                        "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        } else if (requestCode == REQUEST_CODE_FUTURE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PayPalAuthorization auth =
                        data.getParcelableExtra(PayPalFuturePaymentActivity.EXTRA_RESULT_AUTHORIZATION);
                if (auth != null) {
                    try {
                        Log.i("FuturePaymentExample", auth.toJSONObject().toString(4));

                        String authorization_code = auth.getAuthorizationCode();
                        Log.i("FuturePaymentExample", authorization_code);

                        sendAuthorizationToServer(auth);
                        Toast.makeText(this,
                                "PaymentConfirmation info received from PayPal", Toast.LENGTH_LONG)
                                .show();

                    } catch (JSONException e) {
                        Log.e("FuturePaymentExample", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("FuturePaymentExample", "The user canceled.");
            } else if (resultCode == PayPalFuturePaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i(
                        "FuturePaymentExample",
                        "Probably the attempt to previously start the PayPalService had an invalid PayPalConfiguration. Please see the docs.");
            }
        } else if (requestCode == REQUEST_CODE_PROFILE_SHARING) {
            if (resultCode == Activity.RESULT_OK) {
                PayPalAuthorization auth =
                        data.getParcelableExtra(PayPalProfileSharingActivity.EXTRA_RESULT_AUTHORIZATION);
                if (auth != null) {
                    try {
                        Log.i("ProfileSharingExample", auth.toJSONObject().toString(4));

                        String authorization_code = auth.getAuthorizationCode();
                        Log.i("ProfileSharingExample", authorization_code);

                        sendAuthorizationToServer(auth);
                        Toast.makeText(this,
                                "Profile Sharing code received from PayPal", Toast.LENGTH_LONG)
                                .show();

                    } catch (JSONException e) {
                        Log.e("ProfileSharingExample", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("ProfileSharingExample", "The user canceled.");
            } else if (resultCode == PayPalFuturePaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i(
                        "ProfileSharingExample",
                        "Probably the attempt to previously start the PayPalService had an invalid PayPalConfiguration. Please see the docs.");
            }
        }
    }


    private void sendAuthorizationToServer(PayPalAuthorization authorization) {

        /**
         * TODO: Send the authorization response to your server, where it can
         * exchange the authorization code for OAuth access and refresh tokens.
         *
         * Your server must then store these tokens, so that your server code
         * can execute payments for this user in the future.
         *
         * A more complete example that includes the required app-server to
         * PayPal-server integration is available from
         * https://github.com/paypal/rest-api-sdk-python/tree/master/samples/mobile_backend
         */

    }

    @Override
    public void onDestroy() {
        // Stop service when done
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }
}
