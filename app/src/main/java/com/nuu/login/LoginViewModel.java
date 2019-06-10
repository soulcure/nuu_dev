package com.nuu.login;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.text.TextUtils;
import android.util.Patterns;

import com.nuu.entity.DetailRsp;
import com.nuu.http.IPostListener;
import com.nuu.login.model.LoginRepository;
import com.nuu.nuuinfo.R;
import com.nuu.util.GsonUtil;


public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;

    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String username, String password) {
        // can be launched in a separate asynchronous job
        IPostListener listener = new IPostListener() {
            @Override
            public void httpReqResult(String response) {
                DetailRsp rsp = GsonUtil.parse(response, DetailRsp.class);
                if (rsp != null && rsp.getErr_code() == 0) {
                    loginResult.setValue(new LoginResult(new LoggedInUserView(rsp.getErr_desc())));
                } else if (rsp != null && !TextUtils.isEmpty(rsp.getErr_desc())) {
                    loginResult.setValue(new LoginResult(R.string.login_failed));
                }
            }
        };
        loginRepository.login(username, password, listener);
    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}
