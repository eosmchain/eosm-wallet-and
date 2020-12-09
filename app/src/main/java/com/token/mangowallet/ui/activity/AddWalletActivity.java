package com.token.mangowallet.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import com.qmuiteam.qmui.arch.annotation.DefaultFirstFragment;
import com.qmuiteam.qmui.arch.annotation.FirstFragments;
import com.qmuiteam.qmui.arch.annotation.LatestVisitRecord;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.base.BaseFragmentActivity;
import com.token.mangowallet.ui.fragment.BackupsMnemonicFragment;
import com.token.mangowallet.ui.fragment.ConfirmMnemonicFragment;
import com.token.mangowallet.ui.fragment.CreationWalletFragment;
import com.token.mangowallet.ui.fragment.ImportWalletFragment;
import com.token.mangowallet.utils.Constants;
import com.token.mangowallet.utils.MangoFragmentManager;

import static com.token.mangowallet.utils.Constants.CREATE_WALLET;
import static com.token.mangowallet.utils.Constants.EXTRA_ADD_WALLET_TYPE;
import static com.token.mangowallet.utils.Constants.IMPORT_WALLET;
import static com.token.mangowallet.utils.Constants.INTENT_EXTRA_KEY_WALLET_TYPE;

@FirstFragments(
        value = {
                CreationWalletFragment.class,
                BackupsMnemonicFragment.class,
                ConfirmMnemonicFragment.class,
                ImportWalletFragment.class,
        })
@LatestVisitRecord
public class AddWalletActivity extends BaseFragmentActivity {
    private Constants.WalletType mWalletType;
    public static AddWalletActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        Bundle bundle = getIntent().getExtras();
        int type;
        int toUI;
        if (bundle != null) {
            type = bundle.getInt(INTENT_EXTRA_KEY_WALLET_TYPE, 0);
            toUI = bundle.getInt(EXTRA_ADD_WALLET_TYPE);
        } else {
            type = 0;
            toUI = CREATE_WALLET;
        }
        mWalletType = Constants.WalletType.values()[type];
        String fragmentName;
        if (CREATE_WALLET == toUI) {
            fragmentName = "CreationWalletFragment";
        } else if (IMPORT_WALLET == toUI) {
            fragmentName = "ImportWalletFragment";
        } else {
            fragmentName = "CreationWalletFragment";
        }
        BaseFragment fragment = null;
        try {
            fragment = MangoFragmentManager.getInstance().getFragment(fragmentName).newInstance();
            fragment.setArguments(bundle);
            startFragment(fragment);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
