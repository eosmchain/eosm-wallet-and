package com.token.mangowallet.utils;

import android.content.Context;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.SPUtils;
import com.token.mangowallet.MyApplication;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.db.greendao.MangoWalletDao;

import java.util.List;
import java.util.regex.Pattern;

public class WalletDaoUtils {
    public static MangoWalletDao mangoWalletDao = MyApplication.getInstance().getDaoSession().getMangoWalletDao();

    /**
     * 插入新创建钱包
     *
     * @param mangoWallet 新创建钱包
     */
    public static MangoWallet insertNewWallet(MangoWallet mangoWallet) {
        long mRowId = mangoWalletDao.insert(mangoWallet);
        mangoWallet = mangoWalletDao.loadByRowId(mRowId);
        updateCurrent(mangoWallet);
        return mangoWallet;
    }

    /**
     * 更新选中钱包
     *
     * @param id 钱包ID
     */
    public static MangoWallet updateCurrent(long id) {
        MangoWallet mangoWallet = getCurrentWallet();
        SPUtils.getInstance(Constants.SP_WALLET).put(Constants.KEY_WALLET_ID, mangoWallet.getId());
        SPUtils.getInstance(Constants.SP_WALLET).put(Constants.KEY_CURRENT_ACCOUNT_NAME, mangoWallet.getWalletAddress());
        SPUtils.getInstance(Constants.SP_WALLET).put(Constants.KEY_CURRENT_WALLET_PRIVATEKEY, mangoWallet.getPrivateKey());
        return mangoWallet;
    }

    /**
     * 更新选中钱包
     *
     * @param mangoWallet 钱包
     */
    public static MangoWallet updateCurrent(MangoWallet mangoWallet) {
        if (mangoWallet == null) {
            return null;
        }
        Constants.WalletType walletType = Constants.WalletType.getPagerFromPositon(mangoWallet.getWalletType());
        SPUtils.getInstance(Constants.SP_MangoWallet_info).put(Constants.KEY_DIGICCY_SERVER, String.valueOf(walletType));
        SPUtils.getInstance(Constants.SP_WALLET).put(Constants.KEY_CURRENT_ACCOUNT_NAME, mangoWallet.getWalletAddress());
        SPUtils.getInstance(Constants.SP_WALLET).put(Constants.KEY_CURRENT_WALLET_PRIVATEKEY, mangoWallet.getPrivateKey());
        if (ObjectUtils.isNotEmpty(mangoWallet.getId())) {
            SPUtils.getInstance(Constants.SP_WALLET).put(Constants.KEY_WALLET_ID, mangoWallet.getId());
        }
        return mangoWallet;
    }

    /**
     * 当前钱包id
     */
    public static Long getWallsetId() {
        return SPUtils.getInstance(Constants.SP_WALLET).getLong(Constants.KEY_WALLET_ID);
    }

    /**
     * 当前钱包地址
     */
    public static String getWalletAddress() {
        return SPUtils.getInstance(Constants.SP_WALLET).getString(Constants.KEY_CURRENT_ACCOUNT_NAME);
    }

    /**
     * 当前钱包私钥
     */
    public static String getWallsetPrivateKey() {
        return SPUtils.getInstance(Constants.SP_WALLET).getString(Constants.KEY_CURRENT_WALLET_PRIVATEKEY);
    }

    /**
     * 查询所有钱包
     */
    public static List<MangoWallet> loadAll() {
        return mangoWalletDao.loadAll();
    }


    /**
     * 获取当前钱包
     *
     * @return 钱包对象
     */
    public static MangoWallet getCurrentWallet(Context context) {
        List<MangoWallet> mgpWallets = mangoWalletDao.queryBuilder()
                .where(MangoWalletDao.Properties.WalletAddress.eq(getWalletAddress()))
                .orderAsc(MangoWalletDao.Properties.WalletAddress).build().list();
        MangoWallet wallet = null;
        if (mgpWallets != null) {
            if (mgpWallets.size() > 0) {
                wallet = mgpWallets.get(0);
            }
        }
        return wallet;
    }

    /**
     * 获取当前钱包
     *
     * @return 钱包对象
     */
    public static MangoWallet getCurrentWallet() {
        return mangoWalletDao.load(getWallsetId());
    }

    public static boolean isValidMnemonic(String mnemonic) {
        return mnemonic.split(" ").length > 12;
    }

    /**
     * 以助记词查询钱包
     *
     * @return 钱包对象
     */
    public static MangoWallet queryMnemonicCodeWallet(List<String> mnemonicCode) {
        List<MangoWallet> mgpWallets = mangoWalletDao.queryBuilder()
                .where(MangoWalletDao.Properties.MnemonicCode.eq(mnemonicCode))
                .build().list();
        MangoWallet wallet = null;
        if (mgpWallets != null) {
            if (mgpWallets.size() > 0) {
                wallet = mgpWallets.get(0);
            }
        }
        return wallet;
    }

    /**
     * 以私钥查询钱包
     *
     * @return 钱包对象
     */
    public static MangoWallet queryPrivateKeyWallet(String privateKey) {
        List<MangoWallet> mgpWallets = mangoWalletDao.queryBuilder()
                .where(MangoWalletDao.Properties.PrivateKey.eq(privateKey))
                .build().list();
        MangoWallet wallet = null;
        if (mgpWallets != null) {
            if (mgpWallets.size() > 0) {
                wallet = mgpWallets.get(0);
            }
        }
        return wallet;
    }

    /**
     * 以Keystore查询钱包
     *
     * @return 钱包对象
     */
    public static MangoWallet queryKeystoreWallet(String keystore) {
        List<MangoWallet> mgpWallets = mangoWalletDao.queryBuilder()
                .where(MangoWalletDao.Properties.Keystore.eq(keystore))
                .build().list();
        MangoWallet wallet = null;
        if (mgpWallets != null) {
            if (mgpWallets.size() > 0) {
                wallet = mgpWallets.get(0);
            }
        }
        return wallet;
    }

    /**
     * 以助记词检查钱包是否存在
     *
     * @param mnemonic 助记词
     * @return true if repeat
     */
    public static boolean checkRepeatByMenmonic(List<String> mnemonic) {
        MangoWallet mangoWallet = queryMnemonicCodeWallet(mnemonic);
        if (mangoWallet == null) {
            return false;
        }
        return true;
    }

    /**
     * 以私钥检查钱包是否存在
     *
     * @param privateKey 私钥
     * @return true if repeat
     */
    public static boolean checkRepeatByPrivateKey(String privateKey) {
        MangoWallet mangoWallet = queryPrivateKeyWallet(privateKey);
        if (mangoWallet == null) {
            return false;
        }
        return true;
    }

    /**
     * 以keystore检查钱包是否存在
     *
     * @param keystore
     * @return true if repeat
     */
    public static boolean checkRepeatByKeystore(String keystore) {
        MangoWallet mangoWallet = queryKeystoreWallet(keystore);
        if (mangoWallet == null) {
            return false;
        }
        return true;
    }

    /**
     * 以助记词检查钱包是否存在
     *
     * @param privateKey
     * @return true if repeat
     */
    public static boolean isPrivateKey(String privateKey) {
        if (privateKey.startsWith("0x"))
            privateKey = privateKey.replace("0x", "");
        Pattern pattern = Pattern.compile("[0-9a-fA-F]{64}");
        return pattern.matcher(privateKey).matches();
    }
}
