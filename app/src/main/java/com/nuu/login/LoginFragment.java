package com.nuu.login;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nuu.home.HomeFragment;
import com.nuu.nuuinfo.BasePermissionFragment;
import com.nuu.nuuinfo.Main2Activity;
import com.nuu.nuuinfo.R;
import com.nuu.register.ForgetActivity;
import com.nuu.register.RegisterActivity;


public class LoginFragment extends BasePermissionFragment implements View.OnClickListener {

    public final static String TAG = LoginFragment.class.getSimpleName();

    private LoginViewModel loginViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null && bundle.size() > 0) {
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initView(view);
    }

    private void initView(View view) {
        view.findViewById(R.id.btn_register).setOnClickListener(this);
        view.findViewById(R.id.tv_forget_pw).setOnClickListener(this);
        view.findViewById(R.id.tv_forget_email).setOnClickListener(this);

        loginViewModel = ViewModelProviders.of(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        final TextInputLayout til_account = view.findViewById(R.id.til_account);
        final TextInputLayout til_password = view.findViewById(R.id.til_password);

        final EditText usernameEditText = view.findViewById(R.id.et_account);
        final EditText passwordEditText = view.findViewById(R.id.et_password);
        final Button loginButton = view.findViewById(R.id.btn_login);

        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    til_account.setErrorEnabled(true);
                    til_account.setError(getString(loginFormState.getUsernameError()));
                } else {
                    til_account.setErrorEnabled(false);
                }
                if (loginFormState.getPasswordError() != null) {
                    til_password.setErrorEnabled(true);
                    til_password.setError(getString(loginFormState.getPasswordError()));
                } else {
                    til_password.setErrorEnabled(false);
                }
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                dismissProgressDialog();
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                }


            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressDialog();
                loginViewModel.login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        });


    }


    private void updateUiWithUser(LoggedInUserView model) {
        Toast.makeText(mContext, R.string.success, Toast.LENGTH_LONG).show();
        FragmentActivity act = getActivity();
        if (act instanceof Main2Activity) {
            ((Main2Activity) act).skipToFragment(HomeFragment.TAG, null);
        }
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(mContext, errorString, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_register) {
            startActivity(new Intent(mContext, RegisterActivity.class));
        } else if (id == R.id.tv_forget_pw) {
            Intent intent = new Intent(mContext, ForgetActivity.class);
            intent.putExtra(ForgetActivity.FORGET_TYPE, ForgetActivity.TYPE_PASSWORD);

            startActivity(intent);
        } else if (id == R.id.tv_forget_email) {
            Intent intent = new Intent(mContext, ForgetActivity.class);
            intent.putExtra(ForgetActivity.FORGET_TYPE, ForgetActivity.TYPE_EMAIL);

            startActivity(intent);
        }


    }
}
