package com.token.mangowallet.ui.activity.login;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.blankj.utilcode.util.BarUtils;
import com.qmuiteam.qmui.alpha.QMUIAlphaImageButton;
import com.qmuiteam.qmui.arch.QMUILatestVisit;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.token.mangowallet.MainActivity;
import com.token.mangowallet.R;
import com.token.mangowallet.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class LoginFragment extends BaseFragment {

    @BindView(R.id.goBackIv)
    QMUIAlphaImageButton goBackIv;
    @BindView(R.id.usernameEt)
    AppCompatEditText usernameEt;
    @BindView(R.id.passwordEt)
    AppCompatEditText passwordEt;
    @BindView(R.id.forgetPasswordTv)
    AppCompatTextView forgetPasswordTv;
    @BindView(R.id.loginBtn)
    AppCompatButton loginBtn;
    @BindView(R.id.registerBtn)
    QMUIRoundButton registerBtn;

    private LoginViewModel loginViewModel;
    private Unbinder unbinder = null;
    private boolean isShowBack = false;

    @Override
    protected View onCreateView() {
        BarUtils.setStatusBarColor(getBaseFragmentActivity(), ContextCompat.getColor(getBaseFragmentActivity(), R.color.qmui_config_color_white));
        QMUIStatusBarHelper.setStatusBarLightMode(getBaseFragmentActivity());
        loginViewModel = ViewModelProviders.of(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_login, null);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        Toast.makeText(getBaseFragmentActivity(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getBaseFragmentActivity(), errorString, Toast.LENGTH_SHORT).show();
    }

    @OnClick({R.id.goBackIv, R.id.forgetPasswordTv, R.id.loginBtn, R.id.registerBtn, R.id.explainLayout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.goBackIv:
                getBaseFragmentActivity().finish();
                break;
            case R.id.forgetPasswordTv:
                LoginFragment.this.startFragment(new ForgetPasswordFragment());
                break;
            case R.id.loginBtn:
//                doAfterPermissionsGranted();
                break;
            case R.id.registerBtn:
                LoginFragment.this.startFragment(new RegisterFragment());
                break;
            case R.id.explainLayout:
                break;
        }
    }

    private void doAfterPermissionsGranted() {
        Intent intent = QMUILatestVisit.intentOfLatestVisit(getActivity());
        if (intent == null) {
            intent = new Intent(getActivity(), MainActivity.class);
        }
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    protected void initView() {
        goBackIv.setVisibility(isShowBack ? View.VISIBLE : View.GONE);
        forgetPasswordTv.setText(getString(R.string.str_forget_password) + "?");
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initAction() {
        loginViewModel.getLoginFormState().observe(getViewLifecycleOwner(), new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginBtn.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEt.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEt.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        loginViewModel.getLoginResult().observe(getViewLifecycleOwner(), new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
//                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                }
                doAfterPermissionsGranted();

                //Complete and destroy login activity once successful
                requireActivity().finish();
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
                loginViewModel.loginDataChanged(usernameEt.getText().toString(),
                        passwordEt.getText().toString());
            }
        };
        usernameEt.addTextChangedListener(afterTextChangedListener);
        passwordEt.addTextChangedListener(afterTextChangedListener);
        passwordEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEt.getText().toString(),
                            passwordEt.getText().toString());
                }
                return false;
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                loadingProgressBar.setVisibility(View.VISIBLE);
                loginViewModel.login(usernameEt.getText().toString(),
                        passwordEt.getText().toString());
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}