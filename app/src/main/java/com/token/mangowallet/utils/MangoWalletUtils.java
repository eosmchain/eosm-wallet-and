package com.token.mangowallet.utils;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.token.mangowallet.R;
import com.token.mangowallet.bean.AccountNames;
import com.token.mangowallet.callback.CommonCallback;
import com.token.mangowallet.db.MangoWallet;
import com.token.mangowallet.entity.Token;
import com.token.mangowallet.entity.TokenInfo;
import com.token.mangowallet.net.common.RxSubscriber;
import com.token.mangowallet.net.eosmgp.EOSNetWorkManager;
import com.token.mangowallet.net.eosmgp.EOSParams;

import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.ObjectMapperFactory;
import org.web3j.utils.Numeric;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import party.loveit.bip44forandroidlibrary.utils.Bip44Utils;
import party.loveit.eosforandroidlibrary.Ecc;

import static com.token.mangowallet.utils.Constants.ADD_WALLET_FAIL;
import static com.token.mangowallet.utils.Constants.ADD_WALLET_SUCCEED;
import static com.token.mangowallet.utils.Constants.WalletType.ALL;
import static com.token.mangowallet.utils.Constants.WalletType.BTC;
import static com.token.mangowallet.utils.Constants.WalletType.EOS;
import static com.token.mangowallet.utils.Constants.WalletType.ETH;
import static com.token.mangowallet.utils.Constants.WalletType.MGP;

public class MangoWalletUtils {
    private static ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
    /**
     * 随机
     */
    private static final SecureRandom secureRandom = SecureRandomUtils.secureRandom();
    private Credentials credentials;
    /**
     * 通用的以太坊基于bip44协议的助记词路径 （imtoken jaxx Metamask myetherwallet）path
     * 例如：btc一般是 m/44'/0'/0'/0, eth一般是 m/44'/60'/0'/0
     */

    public static String ETH_LEDGER_PATH = "m/44'/60'/0'/0";
    public static String ETH_CUSTOM_PATH = "m/44'/60'/1'/0/0";
    public static String ETH_JAXX_PATH = "m/44'/60'/0'/0/0";
    public static String EOS_PATH = "m/44'/194'/0'/0/0";
    public static String BTC_PATH = "m/44'/0'/0'/0";

    /**
     * 新建钱包
     *
     * @param context
     * @param walletType 钱包类型
     * @param pwd        钱包密码
     * @param hint       密码提示
     * @return 钱包对象
     */
    public static MangoWallet newWallet(Context context, Constants.WalletType walletType, String pwd, String hint) {
        BigInteger prieth;
        try {
            List<String> mnemonicCode = Bip44Utils.generateMnemonicWords(context);
            LogUtils.dTag("newWallet", "mnemonicCode = " + mnemonicCode.toString());
            MangoWallet mangoWallet;
            if (walletType == ALL) {
                mangoWallet = newAllWallet(mnemonicCode, pwd, hint);
            } else {
                prieth = Bip44Utils.getPathPrivateKey(mnemonicCode, getCoinType(walletType));
                mangoWallet = generateWalletData(walletType, pwd, hint, prieth);
                if (mangoWallet != null) {
                    mangoWallet.setMnemonicCode(mnemonicCode);
                }
            }
            return mangoWallet;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static MangoWallet newAllWallet(List<String> mnemonicCode, String pwd, String hint) {
        BigInteger EOSPrieth = Bip44Utils.getPathPrivateKey(mnemonicCode, getCoinType(EOS));
        MangoWallet EOSWallet = generateWalletData(EOS, pwd, hint, EOSPrieth);
        if (EOSWallet != null) {
            EOSWallet.setMnemonicCode(mnemonicCode);
            WalletDaoUtils.insertNewWallet(EOSWallet);
        }

        BigInteger MGPPrieth = Bip44Utils.getPathPrivateKey(mnemonicCode, getCoinType(MGP));
        MangoWallet MGPWallet = generateWalletData(MGP, pwd, hint, MGPPrieth);
        if (MGPWallet != null) {
            MGPWallet.setMnemonicCode(mnemonicCode);
//            WalletDaoUtils.insertNewWallet(MGPWallet);
        }
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

//        BigInteger ETHPrieth = Bip44Utils.getPathPrivateKey(mnemonicCode, getCoinType(ETH));
//        MangoWallet ETHWallet = generateWalletData(ETH, pwd, hint, ETHPrieth);
//        if (ETHWallet != null) {
//            ETHWallet.setMnemonicCode(mnemonicCode);
//            WalletDaoUtils.insertNewWallet(ETHWallet);
//        }


//        BigInteger BTCPrieth = Bip44Utils.getPathPrivateKey(mnemonicCode, getCoinType(BTC));
//        MangoWallet BTCWallet = generateWalletData(BTC, pwd, hint, BTCPrieth);
//        if (BTCWallet != null) {
//            BTCWallet.setMnemonicCode(mnemonicCode);
//            WalletDaoUtils.insertNewWallet(BTCWallet);
//        }
        if (MGPWallet != null) {
            return MGPWallet;
        } else if (EOSWallet != null) {
            return EOSWallet;
        }
//        else if (ETHWallet != null) {
//            return ETHWallet;
//        } else if (BTCWallet != null) {
//            return BTCWallet;
//        }
        else {
            return null;
        }
    }

    /**
     * 通过导入助记词，导入钱包
     *
     * @param walletType   路径
     * @param mnemonicCode 助记词
     * @param pwd          密码
     * @param hint
     * @return
     */
    public static MangoWallet importMnemonicWallet(Constants.WalletType walletType, List<String> mnemonicCode, String pwd, String hint) {
        try {
            BigInteger prieth = Bip44Utils.getPathPrivateKey(mnemonicCode, getCoinType(walletType));
            MangoWallet mangoWallet = generateWalletData(walletType, pwd, hint, prieth);
            if (mangoWallet != null) {
                mangoWallet.setMnemonicCode(mnemonicCode);
            }
            return mangoWallet;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过keystore.json文件导入钱包
     *
     * @param walletType 钱包类型
     * @param keystore   原json文件
     * @param pwd        钱包密码
     * @param hint       密码提示
     * @return
     */
    public static MangoWallet importKeystoreWallet(Constants.WalletType walletType, String keystore, String pwd, String hint) {
        Credentials credentials = null;
        WalletFile walletFile = null;
        try {
            walletFile = objectMapper.readValue(keystore, WalletFile.class);
            credentials = Credentials.create(Wallet.decrypt(pwd, walletFile));
            if (credentials != null) {
                ECKeyPair ecKeyPair = credentials.getEcKeyPair();
                return generateWalletData(walletType, pwd, hint, ecKeyPair);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e("ETHWalletUtils", e.toString());
        }
        return null;
    }

    /**
     * 通过明文私钥导入钱包
     *
     * @param privateKey
     * @param pwd
     * @return
     */
    public static MangoWallet importPrivateKeyWallet(Constants.WalletType walletType, String privateKey, String pwd, String hint) {
        ECKeyPair ecKeyPair = null;
        if (walletType == ETH || walletType == BTC) {
            ecKeyPair = ECKeyPair.create(Numeric.toBigInt(privateKey));
            return generateWalletData(walletType, pwd, hint, ecKeyPair);
        } else if (walletType == EOS || walletType == MGP) {
            return generateWalletData(walletType, pwd, hint, privateKey);
        }
        return null;
    }

    public static void getEOSWalletAccountByPrivateKey(Activity context, final String privatekey, CommonCallback listener) {
        String publicKey = Ecc.privateToPublic(privatekey);
        getKeyAccounts(context, publicKey, listener);
    }

    private static void getKeyAccounts(Activity context, String publicKey, CommonCallback listener) {
        Map<String, Object> map = EOSParams.getKeyAccountsPamars(publicKey);
        Observable.fromCallable(() -> EOSNetWorkManager.getRpcProvider().getKeyAccounts(EOSParams.getRequestBody(map)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new RxSubscriber<String>(context, true) {

                    @Override
                    public void onFail(String failMsg) {
                        listener.onFailure(StringUtils.getString(R.string.str_import_wallet_fail), ADD_WALLET_FAIL);
                    }

                    @Override
                    public void onSuccess(String s) {
                        if (ObjectUtils.isEmpty(s)) {
                            listener.onFailure(StringUtils.getString(R.string.str_import_wallet_fail), ADD_WALLET_FAIL);
                            return;
                        }
                        AccountNames accountNames = GsonUtils.fromJson(s, AccountNames.class);
                        if (ObjectUtils.isEmpty(accountNames)) {
                            listener.onFailure(StringUtils.getString(R.string.str_import_wallet_fail), ADD_WALLET_FAIL);
                            return;
                        }
                        List<String> accountList = accountNames.getAccount_names();
                        if (accountList != null) {
                            if (accountList.size() <= 0) {
                                listener.onFailure(StringUtils.getString(R.string.str_import_wallet_fail), ADD_WALLET_FAIL);
                            } else {
//                                String currentAccountName = accountList.get(0);
//                                MgpWallet mgpWallet = addWallet(context, accountList, currentAccountName, password, privatekey, publicKey, mnemonicList, hint);
                                listener.onSuccess(accountNames, StringUtils.getString(R.string.str_add_wallet_success), ADD_WALLET_SUCCEED);
                            }
                        }
                    }
                });
    }

    /**
     * 生成钱包数据类型
     *
     * @param walletType 钱包类型
     * @param pwd        钱包密码
     * @param hint       密码提示
     * @param prieth     EOS、MGP币生成钱包数据的对象
     * @return 钱包对象
     */
    private static MangoWallet generateWalletData(Constants.WalletType walletType, String pwd, String hint, BigInteger prieth) {
        String publicKey;
        String privateKey;
        String address;
        String keystorePath;
        if (walletType != ALL) {
            try {
                if (walletType == ETH || walletType == BTC) {
                    ECKeyPair ecKeyPair = ECKeyPair.create(prieth);
                    WalletFile keyStoreFile;
                    keyStoreFile = Wallet.create(pwd, ecKeyPair, 1024, 1);
                    File destination = new File(AppFilePath.Wallet_DIR, "keystore_" + AppFilePath.creatFileName() + ".json");
                    //目录不存在则创建目录，创建不了则报错
                    if (!createParentDir(destination)) {
                        return null;
                    }
                    objectMapper.writeValue(destination, keyStoreFile);
                    privateKey = Numeric.toHexStringWithPrefixZeroPadded(ecKeyPair.getPrivateKey(), Keys.PRIVATE_KEY_LENGTH_IN_HEX);
                    publicKey = Numeric.toHexStringWithPrefix(ecKeyPair.getPublicKey());
                    address = Keys.toChecksumAddress(keyStoreFile.getAddress());//"0x" + Keys.getAddress(ecKeyPair)
                    keystorePath = destination.getAbsolutePath();
                } else if (walletType == EOS || walletType == MGP) {
                    privateKey = Ecc.seedPrivate(prieth);
                    publicKey = Ecc.privateToPublic(privateKey);
                    keystorePath = "";
                    address = "";
                } else {
                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return addWallet(walletType, pwd, hint, address, privateKey, publicKey, keystorePath);
        }
        return null;
    }

    /**
     * 生成钱包数据类型
     *
     * @param walletType 钱包类型
     * @param pwd        钱包密码
     * @param hint       密码提示
     * @return 钱包对象
     */
    private static MangoWallet generateWalletData(Constants.WalletType walletType, String pwd, String hint, String privateKey) {
        String publicKey;
        String address;
        String keystorePath;
        try {
            if (walletType == EOS || walletType == MGP) {
                publicKey = Ecc.privateToPublic(privateKey);
                keystorePath = "";
                address = "";
                return addWallet(walletType,  pwd, hint, address, privateKey, publicKey, keystorePath);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    /**
     * 生成钱包数据类型
     *
     * @param walletType 钱包类型
     * @param pwd        钱包密码
     * @param hint       密码提示
     * @return 钱包对象
     */
    private static MangoWallet generateWalletData(Constants.WalletType walletType, String pwd, String hint, ECKeyPair ecKeyPair) {
        String publicKey;
        String privateKey;
        String address;
        String keystorePath;
        try {
            if (walletType == ETH || walletType == BTC) {
                WalletFile keyStoreFile;
                keyStoreFile = Wallet.create(pwd, ecKeyPair, 1024, 1);
                File destination = new File(AppFilePath.Wallet_DIR, "keystore_" + AppFilePath.creatFileName() + ".json");
                //目录不存在则创建目录，创建不了则报错
                if (!createParentDir(destination)) {
                    return null;
                }
                objectMapper.writeValue(destination, keyStoreFile);
                privateKey = Numeric.toHexStringWithPrefixZeroPadded(ecKeyPair.getPrivateKey(), Keys.PRIVATE_KEY_LENGTH_IN_HEX);
                publicKey = Numeric.toHexStringWithPrefix(ecKeyPair.getPublicKey());
                address = Keys.toChecksumAddress(keyStoreFile.getAddress());//"0x" + Keys.getAddress(ecKeyPair)
                keystorePath = destination.getAbsolutePath();
                LogUtils.i("ETHWalletUtils", "wallet_dir = " + keystorePath);
                return addWallet(walletType, pwd, hint, address, privateKey, publicKey, keystorePath);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    /**
     * 创建钱包对象
     *
     * @param walletType   钱包类型
     * @param pwd          钱包密码
     * @param hint         密码提示
     * @param address      钱包地址
     * @param privateKey   钱包私钥
     * @param publicKey    钱包公钥
     * @param keystorePath keystore文件绝对路径
     * @return MangoWallet 钱包对象
     */
    @Nullable
    public static MangoWallet addWallet(Constants.WalletType walletType, String pwd, String hint
            , String address, String privateKey, String publicKey, String keystorePath) {
        LogUtils.i("ETHWalletUtils", "walletType = " + walletType + " privateKey = " + privateKey + " publicKey = " + publicKey + " address = " + address + " keystorePath = " + keystorePath);
        Token token = addToken("", walletType + "", walletType + "", walletType);
        List<String> tokens = new ArrayList<>();
        tokens.add(GsonUtils.toJson(token));

        MangoWallet mangoWallet = new MangoWallet();
        mangoWallet.setWalletType(walletType.getType());
        mangoWallet.setWalletPassword(Md5Utils.md5(pwd));
        mangoWallet.setHint(hint);
        mangoWallet.setWalletAddress(address);
        mangoWallet.setPrivateKey(privateKey);
        mangoWallet.setPublicKey(publicKey);
        mangoWallet.setKeystore(keystorePath);
        mangoWallet.setTokens(tokens);
        return mangoWallet;
    }

    private static Token addToken(String address, String name, String symbol, Constants.WalletType walletType) {
        TokenInfo tokenInfo = new TokenInfo(address, name, symbol, getDecimals(walletType), walletType);
        return new Token(tokenInfo, "");
    }

    public static MangoWallet walletAddToken(MangoWallet mangoWallet, String address, String name, String symbol, Constants.WalletType walletType) {
        List<String> tokens = mangoWallet.getTokens();
        Token token = addToken(address, name, symbol, walletType);
        tokens.add(GsonUtils.toJson(token));

        mangoWallet.setTokens(tokens);
        WalletDaoUtils.mangoWalletDao.update(mangoWallet);
        return mangoWallet;
    }

    private static int getDecimals(Constants.WalletType walletType) {
        int decimals = 0;
        switch (walletType) {
            case BTC:
                decimals = 18;
                break;
            case ETH:
                decimals = 18;
                break;
            case EOS:
            case MGP:
                decimals = 4;
                break;
            default:
                decimals = 18;
        }
        return decimals;
    }

    /**
     * 修改钱包密码
     *
     * @param walletId
     * @param oldPassword
     * @param newPassword
     * @return
     */
    public static MangoWallet modifyPassword(long walletId, String oldPassword, String newPassword) {
        MangoWallet mangoWallet = WalletDaoUtils.mangoWalletDao.load(walletId);

        String filePath = mangoWallet.getKeystore();
        try {
            if (Constants.WalletType.getPagerFromPositon(mangoWallet.getWalletType()) == ETH || Constants.WalletType.getPagerFromPositon(mangoWallet.getWalletType()) == MGP) {
                Credentials credentials = WalletUtils.loadCredentials(oldPassword, mangoWallet.getKeystore());
                ECKeyPair keypair = credentials.getEcKeyPair();
                File destinationDirectory = new File(filePath);
                WalletUtils.generateWalletFile(newPassword, keypair, destinationDirectory, true);
            }
            mangoWallet.setWalletPassword(newPassword);
            WalletDaoUtils.mangoWalletDao.insert(mangoWallet);
        } catch (CipherException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mangoWallet;
    }

    /**
     * 公钥获取地址
     *
     * @param publicKey
     * @return address
     */
    public static String publicKeToAddress(BigInteger publicKey) {
        return Keys.toChecksumAddress(Keys.getAddress(publicKey));
    }

    /**
     * 导出keystore文件
     *
     * @param walletId
     * @param pwd
     * @return
     */
    public static String deriveKeystore(long walletId, String pwd) {
        MangoWallet mangoWallet = WalletDaoUtils.mangoWalletDao.load(walletId);
        String keystore = null;
        WalletFile walletFile;
        try {
            walletFile = objectMapper.readValue(new File(mangoWallet.getKeystore()), WalletFile.class);
            keystore = objectMapper.writeValueAsString(walletFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return keystore;
    }

    /**
     * 导出keystore文件
     *
     * @param mangoWallet
     * @return
     */
    public static String deriveKeystore(MangoWallet mangoWallet) {
        String keystore = null;
        WalletFile walletFile;
        try {
            walletFile = objectMapper.readValue(new File(mangoWallet.getKeystore()), WalletFile.class);
            keystore = objectMapper.writeValueAsString(walletFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return keystore;
    }


    private static String getCoinType(Constants.WalletType walletType) {
        String coin_type = "";
        switch (walletType) {
            case EOS:
            case MGP:
                coin_type = EOS_PATH;
                break;
            case ETH:
                coin_type = ETH_JAXX_PATH;
                break;
            case BTC:
                coin_type = BTC_PATH;
                break;
        }
        return coin_type;
    }

    public static Credentials loadCredentials(String password, File source)
            throws IOException, CipherException {
        WalletFile walletFile = objectMapper.readValue(source, WalletFile.class);
        return Credentials.create(Wallet.decrypt(password, walletFile));
    }

    private static boolean createParentDir(File file) {
        //判断目标文件所在的目录是否存在
        if (!file.getParentFile().exists()) {
            //如果目标文件所在的目录不存在，则创建父目录
            System.out.println("目标文件所在目录不存在，准备创建");
            if (!file.getParentFile().mkdirs()) {
                System.out.println("创建目标文件所在目录失败！");
                return false;
            }
        }
        return true;
    }
}
