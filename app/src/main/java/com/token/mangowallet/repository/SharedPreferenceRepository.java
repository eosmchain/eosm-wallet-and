package com.token.mangowallet.repository;

import android.content.Context;
import android.content.SharedPreferences;

import com.blankj.utilcode.util.SPUtils;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.entity.GasSettings;
import com.token.mangowallet.utils.Constants;
import com.token.mangowallet.utils.WalletDaoUtils;

import java.math.BigInteger;

import static com.token.mangowallet.utils.Constants.DEFAULT_GAS_LIMIT;
import static com.token.mangowallet.utils.Constants.DEFAULT_GAS_LIMIT_FOR_TOKENS;
import static com.token.mangowallet.utils.Constants.DEFAULT_GAS_PRICE;
import static com.token.mangowallet.utils.Constants.WalletType.ETH;

public class SharedPreferenceRepository {

    private static final String SP_ETH_WALLET_INFO = "sp_eth_wallet_info";
    private static final String CURRENT_ACCOUNT_ADDRESS_KEY = "current_account_address";
    private static final String DEFAULT_NETWORK_NAME_KEY = "default_network_name";
    private static final String GAS_PRICE_KEY = "gas_price";
    private static final String GAS_LIMIT_KEY = "gas_limit";
    private static final String GAS_LIMIT_FOR_TOKENS_KEY = "gas_limit_for_tokens";
    private static final String CURRENCY_UNIT = "currencyUnit";

    private static SharedPreferenceRepository sSelf;


    public static SharedPreferenceRepository init(Context context) {
        if (sSelf == null) {
            sSelf = new SharedPreferenceRepository(context);
        }
        return sSelf;
    }

    private SharedPreferenceRepository(Context context) {
//        pref = context.getSharedPreferences(context.getPackageName() + "_preference", Context.MODE_MULTI_PROCESS);
    }

    public static SharedPreferenceRepository instance(Context context) {
        return init(context);
    }

    ;

    public String getCurrentWalletAddress() {
        MangoWallet mangoWallet = WalletDaoUtils.getCurrentWallet();
        if (Constants.WalletType.getPagerFromPositon(mangoWallet.getWalletType()) == ETH) {
            return mangoWallet.getWalletAddress();
        }
        return "";
//		return pref.getString(CURRENT_ACCOUNT_ADDRESS_KEY, null);
    }

    public void setCurrentWalletAddress(String address) {
        SPUtils.getInstance(SP_ETH_WALLET_INFO).put(CURRENT_ACCOUNT_ADDRESS_KEY, address);
    }

    public int getCurrencyUnit() {
        return SPUtils.getInstance(SP_ETH_WALLET_INFO).getInt(CURRENCY_UNIT, 0);
    }

    public void setCurrencyUnit(int currencyUnit) {
        SPUtils.getInstance(SP_ETH_WALLET_INFO).put(CURRENCY_UNIT, currencyUnit);
    }

    public String getDefaultNetwork() {
        return SPUtils.getInstance(SP_ETH_WALLET_INFO).getString(DEFAULT_NETWORK_NAME_KEY, null);
    }

    public void setDefaultNetwork(String netName) {
        SPUtils.getInstance(SP_ETH_WALLET_INFO).put(DEFAULT_NETWORK_NAME_KEY, netName);
    }

    public GasSettings getGasSettings(boolean forTokenTransfer) {
        BigInteger gasPrice = new BigInteger(SPUtils.getInstance(SP_ETH_WALLET_INFO).getString(GAS_PRICE_KEY, DEFAULT_GAS_PRICE));
        BigInteger gasLimit = new BigInteger(SPUtils.getInstance(SP_ETH_WALLET_INFO).getString(GAS_LIMIT_KEY, DEFAULT_GAS_LIMIT));
        if (forTokenTransfer) {
            gasLimit = new BigInteger(SPUtils.getInstance(SP_ETH_WALLET_INFO).getString(GAS_LIMIT_FOR_TOKENS_KEY, DEFAULT_GAS_LIMIT_FOR_TOKENS));
        }

        return new GasSettings(gasPrice, gasLimit);
    }

    public void setGasSettings(GasSettings gasSettings) {
        SPUtils.getInstance(SP_ETH_WALLET_INFO).put(GAS_PRICE_KEY, gasSettings.gasPrice.toString());
        SPUtils.getInstance(SP_ETH_WALLET_INFO).put(GAS_PRICE_KEY, gasSettings.gasLimit.toString());
    }
}
