package com.token.mangowallet.net.common;

import com.blankj.utilcode.util.MapUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.SPUtils;
import com.token.mangowallet.utils.Constants;

import java.util.HashMap;
import java.util.LinkedHashMap;

import static com.token.mangowallet.utils.Constants.AYING_TEST_MGP_URL;
import static com.token.mangowallet.utils.Constants.AYING_URL;
import static com.token.mangowallet.utils.Constants.CHEN_TEST_MGP_URL;
import static com.token.mangowallet.utils.Constants.CORPORATION_URL;
import static com.token.mangowallet.utils.Constants.DEAL_CONTRACT;
import static com.token.mangowallet.utils.Constants.FAJIAN_URL;
import static com.token.mangowallet.utils.Constants.GUOYU_TEST_URL;
import static com.token.mangowallet.utils.Constants.GUOYU_URL;
import static com.token.mangowallet.utils.Constants.KEY_DIGICCY_SERVER;
import static com.token.mangowallet.utils.Constants.MIAN_MGP_URL;
import static com.token.mangowallet.utils.Constants.OTC_API_URL;
import static com.token.mangowallet.utils.Constants.OTC_TEST_API_URL;
import static com.token.mangowallet.utils.Constants.PRODUCT_URL;
import static com.token.mangowallet.utils.Constants.TESTURL;
import static com.token.mangowallet.utils.Constants.TEST_DEAL_CONTRACT;
import static com.token.mangowallet.utils.Constants.TEST_URL;
import static com.token.mangowallet.utils.Constants.TEST_VOTE_CONTRACT;
import static com.token.mangowallet.utils.Constants.VOTE_API_URL;
import static com.token.mangowallet.utils.Constants.VOTE_CONTRACT;
import static com.token.mangowallet.utils.Constants.VOTE_TEST_API_URL;

public class BaseUrlUtils {
    private static BaseUrlUtils mInstance;
    public LinkedHashMap<String, ServerInfo> serverInfoMap;
    private ServerInfo serverInfo;


    public static BaseUrlUtils getInstance() {
        if (mInstance == null) {
            synchronized (BaseUrlUtils.class) {
                if (mInstance == null) {
                    mInstance = new BaseUrlUtils();
                }
            }
        }
        return mInstance;
    }


    public BaseUrlUtils() {
        getServerInfo();
    }

    public String getDIGICCYBaseUrl() {
        String nodeServerName = SPUtils.getInstance(Constants.SP_MangoWallet_info)
                .getString(KEY_DIGICCY_SERVER, String.valueOf(Constants.WalletType.MGP));
        String serverName = SPUtils.getInstance(Constants.SP_MangoWallet_info).getString(Constants.KEY_SERVER, Constants.isTest ? TESTURL : CORPORATION_URL);
        ServerInfo serverInfo = serverInfoMap.get(serverName);
        if (ObjectUtils.equals(String.valueOf(Constants.WalletType.EOS), nodeServerName)) {
            return serverInfo.getNodeEOS();
        } else if (ObjectUtils.equals(String.valueOf(Constants.WalletType.MGP), nodeServerName)) {
            return serverInfo.getNodeMGP();
        } else {
            return MIAN_MGP_URL;
        }
    }

    public String getVoteUrl() {
        String serverName = SPUtils.getInstance(Constants.SP_MangoWallet_info).getString(Constants.KEY_SERVER, Constants.isTest ? TESTURL : CORPORATION_URL);
        ServerInfo serverInfo = serverInfoMap.get(serverName);
        if (serverInfo.isTest()) {
            return VOTE_TEST_API_URL;
        } else {
            return VOTE_API_URL;
        }
    }

    public String getOTCUrl() {
        String serverName = SPUtils.getInstance(Constants.SP_MangoWallet_info).getString(Constants.KEY_SERVER, Constants.isTest ? TESTURL : CORPORATION_URL);
        ServerInfo serverInfo = serverInfoMap.get(serverName);
        if (serverInfo.isTest()) {
            return OTC_TEST_API_URL;
        } else {
            return OTC_API_URL;
        }
    }

    public String getOTCContract() {
        String serverName = SPUtils.getInstance(Constants.SP_MangoWallet_info).getString(Constants.KEY_SERVER, Constants.isTest ? TESTURL : CORPORATION_URL);
        ServerInfo serverInfo = serverInfoMap.get(serverName);
        if (serverInfo.isTest()) {
            return TEST_DEAL_CONTRACT;
        } else {
            return DEAL_CONTRACT;
        }
    }

    public String getVoteContract() {
        String serverName = SPUtils.getInstance(Constants.SP_MangoWallet_info).getString(Constants.KEY_SERVER, Constants.isTest ? TESTURL : CORPORATION_URL);
        ServerInfo serverInfo = serverInfoMap.get(serverName);
        if (serverInfo.isTest()) {
            return TEST_VOTE_CONTRACT;
        } else {
            return VOTE_CONTRACT;
        }
    }

    public String getCompanyBaseUrl() {
        String serverName = SPUtils.getInstance(Constants.SP_MangoWallet_info).getString(Constants.KEY_SERVER, Constants.isTest ? TESTURL : CORPORATION_URL);
        ServerInfo serverInfo = serverInfoMap.get(serverName);
        return serverInfo.getKserverApi();
    }

    public boolean isTest() {
        String serverName = SPUtils.getInstance(Constants.SP_MangoWallet_info).getString(Constants.KEY_SERVER, Constants.isTest ? TESTURL : CORPORATION_URL);
        ServerInfo serverInfo = serverInfoMap.get(serverName);
        return serverInfo.isTest();
    }

    public HashMap<String, ServerInfo> getServerInfo() {
        serverInfoMap = MapUtils.newLinkedHashMap();
        ServerInfo info1 = new ServerInfo();
        info1.setTest(false);
        info1.setKserverName("正式环境");
        info1.setKserverApi(PRODUCT_URL);
        info1.setNodeBTC("https://bitstoreapp.com");
        info1.setNodeETH("https://api-cn.etherscan.com/api");
        info1.setNodeEOS("https://eos.greymass.com");
        info1.setNodeMGP(MIAN_MGP_URL);
        info1.setKserverKey(CORPORATION_URL);

        ServerInfo info2 = new ServerInfo();
        info2.setTest(true);
        info2.setKserverName("测试环境");
        info2.setKserverApi(TEST_URL);
        info2.setNodeBTC("http://test.bitstoreapp.com");
        info2.setNodeETH("https://ropsten.infura.io/v3/dc6e037d7e2141e38e2b8042d29438aa");
        info2.setNodeEOS("https://jungle2.cryptolions.io");
        info2.setNodeMGP(CHEN_TEST_MGP_URL);
        info2.setKserverKey(TESTURL);

        ServerInfo info3 = new ServerInfo();
        info3.setTest(true);
        info3.setKserverName("gy本地");
        info3.setKserverApi(GUOYU_TEST_URL);
        info3.setNodeBTC("http://test.bitstoreapp.com");
        info3.setNodeETH("https://ropsten.infura.io/v3/dc6e037d7e2141e38e2b8042d29438aa");
        info3.setNodeEOS("https://jungle2.cryptolions.io");
        info3.setNodeMGP(AYING_TEST_MGP_URL);
        info3.setKserverKey(GUOYU_URL);

        ServerInfo info4 = new ServerInfo();
        info4.setTest(true);
        info4.setKserverName("ay本地");
        info4.setKserverApi(TEST_URL);
        info4.setNodeBTC("http://test.bitstoreapp.com");
        info4.setNodeETH("https://ropsten.infura.io/v3/dc6e037d7e2141e38e2b8042d29438aa");
        info4.setNodeEOS("https://jungle2.cryptolions.io");
        info4.setNodeMGP(AYING_TEST_MGP_URL);
        info4.setKserverKey(AYING_URL);

        ServerInfo info5 = new ServerInfo();
        info5.setTest(true);
        info5.setKserverName("fj本地");
        info5.setKserverApi("http://v.babih.com");
        info5.setNodeBTC("http://test.bitstoreapp.com");
        info5.setNodeETH("https://ropsten.infura.io/v3/dc6e037d7e2141e38e2b8042d29438aa");
        info5.setNodeEOS("https://jungle2.cryptolions.io");
        info5.setNodeMGP(AYING_TEST_MGP_URL);
        info5.setKserverKey(FAJIAN_URL);

//        ServerInfo info6 = new ServerInfo();
//        info6.setTest(true);
//        info6.setKserverName("法建本地");
//        info6.setKserverApi("http://v.babih.com");
//        info6.setNodeBTC("http://test.bitstoreapp.com");
//        info6.setNodeETH("https://ropsten.infura.io/v3/dc6e037d7e2141e38e2b8042d29438aa");
//        info6.setNodeEOS("https://jungle2.cryptolions.io");
//        info6.setNodeMGP(AYING_TEST_MGP_URL);
//        info6.setKserverKey(FAJIAN_URL);

        serverInfoMap.put(CORPORATION_URL, info1);
        serverInfoMap.put(TESTURL, info2);
        serverInfoMap.put(GUOYU_URL, info3);
        serverInfoMap.put(AYING_URL, info4);
        serverInfoMap.put(FAJIAN_URL, info5);
        return serverInfoMap;
    }
}
