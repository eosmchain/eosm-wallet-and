package com.token.mangowallet.ui.activity.login;

import android.os.Bundle;

import com.qmuiteam.qmui.arch.annotation.DefaultFirstFragment;
import com.qmuiteam.qmui.arch.annotation.FirstFragments;
import com.qmuiteam.qmui.arch.annotation.LatestVisitRecord;
import com.token.mangowallet.base.BaseFragmentActivity;

@FirstFragments(
        value = {
                LoginFragment.class,
                ForgetPasswordFragment.class,
//                NotificationsFragment.class,
        })
@DefaultFirstFragment(LoginFragment.class)
@LatestVisitRecord
public class BaseLoginActivity extends BaseFragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
