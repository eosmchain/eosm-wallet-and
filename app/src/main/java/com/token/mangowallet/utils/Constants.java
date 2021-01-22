package com.token.mangowallet.utils;

import android.os.Environment;

import java.io.File;
import java.math.BigDecimal;

public interface Constants {
    boolean isTest = false;
    /**
     * 正式域名
     */
    String PRODUCT_URL = "https://api.mgpchain.com/";//https://api.mgpchain.com/api/appVersion/getVersion
    String TEST_URL = "https://api.coom.pub/";//http://47.56.124.233:18888/";
    String GUOYU_TEST_URL = "http://192.168.0.101:8888/";
    String AYING_TEST_URL = "https://api.coom.pub/";//"http://192.168.31.50:28888"; //"http://47.56.127.227:8888";
    //MGP
    String MIAN_MGP_URL = "http://sh-expnode.vm.mgps.me:8888";//"http://m1.vm.mgps.me:8010";//"http://23.252.162.93:8000";//http://m1.vm.mgps.me:8010";//"http://explorer.mgpchain.io:8000";
    String TEST_MGP_URL = "https://jungle2.cryptolions.io";
    String AYING_TEST_MGP_URL = "http://47.56.127.227:8888";
    String CHEN_TEST_MGP_URL = "http://sh-test.vm.mgps.me:8888";
    //EOS
    String MAIN_EOS_URL = "http://eosnetworkmonitor.io";
    String TEST_EOS_URL = "http://eosnetworkmonitor.io";
    //获取代币Token
    String ETHPLORER_API_URL = "https://api.ethplorer.io";
    String VOTE_TEST_API_URL = "http://m.test.mgps.me/";//"http://vote.mgpchain.io";// "http://192.168.31.49:9000";
    String VOTE_API_URL = "http://vote.mgpchain.io";
    String OTC_API_URL = "https://m.mgps.me/";
    String OTC_TEST_API_URL = "http://m.test.mgps.me/";
    String MMGPS_API_URL = "http://m.mgps.me";
    /**
     * 查询币价地址
     */
    String TICKER_API_URL = "https://api.bkex.co";

    String MGP_TRANSACTION_DETAILS_URL = "https://explorer.mgpchain.io/transaction/";//MGP 交易详情

    ///公司地址
    String CORPORATION_URL = "CORPORATION_URL";
    String TESTURL = "TEST_URL";
    String GUOYU_URL = "GUOYU_URL";
    String AYING_URL = "AYING_URL";
    String FAJIAN_URL = "FAJIAN_URL";
    String TOKENURL = "ETHPLORER_URL";
    String VOTEURL = "VOTE_URL";
    String OTCURL = "OTC_URL";
    String MMGPSME = "m.mgps.me";
    ///查询数字货币价格 price
    String DIGICCY_PRICE_URL = "DIGICCY_PRICE_URL";

    String EOS_URL = "EOS_URL";

    String OFFICIAL_URL_EOS = "OFFICIAL_URL_EOS";
    String TESTURL_EOS = "TESTURL_EOS";
    String AYING_URL_EOS = "AYING_URL_EOS";

    String OFFICIAL_URL_MGP = "OFFICIAL_URL_MGP";
    String TESTURL_MGP = "TESTURL_MGP";
    String AYING_URL_MGP = "AYING_URL_MGP";
    //log key
    String LOG_TAG = "LOG_TAG_MangoWallet";
    int DEFAULT_TIMEOUT = 40000;

    BigDecimal percent = new BigDecimal(100);
    BigDecimal thousand = new BigDecimal(10000);
    BigDecimal bdCurrencyAmount = new BigDecimal("500000000");

    enum WalletType {
        ALL(0), MGP(1), ETH(2), BTC(3), EOS(4);

        private int type;//自定义属性

        public int getType() {
            return type;
        }

        WalletType(int type) {
            this.type = type;
        }

        public static WalletType getPagerFromPositon(int position) {
            switch (position) {
                case 0:
                    return ALL;
                case 1:
                    return MGP;
                case 2:
                    return ETH;
                case 3:
                    return BTC;
                case 4:
                    return EOS;
                default:
                    return ALL;
            }
        }
    }

    boolean ISTEST = true;

    String MGP_SYMBOL = "MGP";
    String ETH_SYMBOL = "ETH";
    String BTC_SYMBOL = "BTC";
    String EOS_SYMBOL = "EOS";
    String POA_SYMBOL = "POA";
    String ETC_SYMBOL = "ETC";
    String HMIO_SYMBOL = "HMIO";
    String PRICE_MGP = "MGP_USDT";
    String USDT_SYMBOL = "USDT";
    String KEY_LANGUAGE_SETUP = "key_language_setup";
    int lang_system_language = 0;
    int lang_english = 1;
    int lang_japanese = 2;
    int lang_korean = 3;
    int lang_simplified_chinese = 4;
    int lang_traditional_chinese = 5;
    ////////////////// EOS or MGP /////////////////////////
    String EOSIO_SYSTEM_CONTRACT_CODE = "eosio";//合约托管账号名称，字符串 "buyram" : "sellram"; "delegatebw" : "undelegatebw"
    String EOSIO_TOKEN_CONTRACT_CODE = "eosio.token";//合约托管账号名称，字符串 transfer
    String BUYRAM_ACTION = "buyram";
    String SELLRAM_ACTION = "sellram";
    String DELEGATEBW_ACTION = "delegatebw";
    String UNDELEGATEBW_ACTION = "undelegatebw";
    String TRANSFER_ACTION = "transfer";
    String BIND_ACTION = "bindaddress";
    String contractAddress = "addressbookt";//合约地址：主要用于抵押
    String SHOP_ACCOUNT = "mgpchainshop";//Merchant account
    String MARGIN_ACCOUNT = "mgpchainbond";//保证金
    String VOTE_ACCOUNT = "mgpchainvote";
    String HMIO_TOKEN = "0x155697df0d39e18f719fa58e25a53a65ccb4864e";
    String ETH_TOKEN = "0xDA79e4a8839CB8EAF9BeCe5DB71feAA4565347B2";
    String MGP_PLEDGE_ACCOUNT = "mgpchainhalf";//Merchant account pledge
    String USDT_TOKEN = "0xdAC17F958D2ee523a2206206994597C13D831ec7";
    String VOTE_CONTRACT = "mgp.bpvoting";
    String TEST_VOTE_CONTRACT = "bpvotingo3o5";
    String WITHDRAW_VOTE = "unvote";//撤回投票
    String CANCEL_NODE = "delist";//取消节点  cancel
    String DEAL_CONTRACT = "mgp.otcstore";//"qmgpotcstore"mgp.otcstore";//买卖合约地址
    String OPEN_ORDER = "openorder";
    String OLOSE_ORDER = "closeorder";
    ///////////////////////////////////////////
    String BaseFilePath = Environment.getExternalStorageDirectory() + File.separator + "MangoWallet";
    String CrashFilePath = BaseFilePath + File.separator + "Crash";
    String HttpRequestLog = BaseFilePath + File.separator + "HttpRequestLog";
    String WalletFilePath = BaseFilePath + File.separator + "Wallet";
    String CropimageFilePath = BaseFilePath + File.separator + "cropimage";
    String REGEX_ACCOUNT_NAME = "^[a-z1-5]{12}$";
    //////////////////Activity Fragment KEY/////////////////////////
    String INTENT_EXTRA_KEY_QR_SCAN = "qr_scan_result";
    String INTENT_EXTRA_KEY_WALLET_TYPE = "wallet_type";
    String EXTRA_WALLET = "extra_wallet";
    String EXTRA_CREATE_ACCOUNT_DATA = "extra_create_account_data";
    String EXTRA_INTERSECTION = "extra_intersection";
    String EXTRA_ADD_WALLET_TYPE = "add_wallet_type";
    String EXTRA_SELLRAM = "extra_sellram";
    String EXTRA_BUYRAM = "extra_buyram";
    String EXTRA_DELEGATEBW = "extra_delegatebw";
    String EXTRA_UNDELEGATEBW = "extra_undelegatebw";
    String EXTRA_ACCOUNT_INFO = "extra_account_info";
    String EXTRA_TRANSACTION = "extra_transaction";
    String EXTRA_TRANSACTION_ID = "extra_transaction_id";
    String EXTRA_TO_URL = "extra_to_url";
    String EXTRA_TITLE = "extra_title";
    String EXTRA_GOODS_INFO = "extra_goods_info";
    String EXTRA_RECEIVER_ADDRESS = "extra_receiver_address";
    String EXTRA_ISADD_ADDRESS = "extra_isadd_address";
    String EXTRA_ORDER_TYPE = "extra_order_type";
    String EXTRA_IS_BUYER = "extra_is_buyer";
    String EXTRA_GOODS = "extra_goods";
    String EXTRA_STORE = "extra_store";
    String EXTRA_IS_EDIT = "extra_is_edit";
    String EXTRA_LIFE_DATA = "extra_life_data";
    String EXTRA_CATE_DATA = "extra_cate_data";
    String EXTRA_IS_FIRST = "extra_is_first";
    String EXTRA_VOTE_DATA = "extra_vote_data";
    int ADD_WALLET = 0;//创建添加 AddWalletFragment
    int CREATE_WALLET = 1;//创建添加 CreateAccountFragment
    int IMPORT_WALLET = 2;//导入添加
    ///////////////////////////////////////////
    int TO_TRANSFER_WALLET = 1;//转账transfer 二维码json数据定义
    int TO_IMPORT_WALLET_PRIVATEKEY = 2;//导出私钥 二维码json数据定义
    int TO_CREATE_WALLET = 3;//创建钱包 二维码json数据定义
    int TO_IMPORT_WALLET_MNEMONIC = 4;//创建钱包 二维码json数据定义

    int REQUEST_SCAN_CODE = 1000;
    int REQUEST_CUT_WALLET = 1001;
    ///////////////////sp////////////////////////
    String SP_WALLET = "sp_wallet";
    String KEY_WALLET = "key_wallet";
    String SP_MangoWallet_info = "sp_mangowallet_info";
    String KEY_EOS_SERVER = "key_eos_server";
    String KEY_SERVER = "key_server";
    String KEY_CURRENT_ACCOUNT_NAME = "current_account_name";//当前钱包账户名
    String KEY_CURRENT_WALLET_PRIVATEKEY = "current_wallet_privateKey";//当前钱包账户名
    String KEY_WALLET_ID = "current_wallet_id";//当前钱包账户名
    String KEY_DIGICCY_SERVER = "key_digiccy_server";
    String KEY_CURRENCY_DATA = "key_currency_data";
    String KEY_CURRENCY_SYMBOL = "key_currency_symbolName";
    String KEY_COIN_SYMBOL = "key_coin_symbol";
    /////////////////BusUtils////////////////////////
    String BUS_TO_WALLET = "bus_to_wallet";
    String BUS_DELETE_WALLET = "bus_delete_wallet";
    int BUS_CUT_WALLET = 0;//
    int BUS_ADD_WALLET = 1;//
    ///////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////
    // 获取实时价格（行情 Ticker ） URL
//    String TICKER_API_URL = "http://47.93.151.65:8000";


    int IMPORT_REQUEST_CODE = 1001;
    int EXPORT_REQUEST_CODE = 1002;
    int SHARE_REQUEST_CODE = 1003;

    String ETHEREUM_MAIN_NETWORK_NAME = "Mainnet";
    String CLASSIC_NETWORK_NAME = "Ethereum Classic";
    String POA_NETWORK_NAME = "POA Network";
    String KOVAN_NETWORK_NAME = "Kovan";
    String ROPSTEN_NETWORK_NAME = "Ropsten";

    String LOCAL_DEV_NETWORK_NAME = "local_dev";

    String ETHEREUM_TIKER = "ethereum";
    String POA_TIKER = "poa";

    String GWEI_UNIT = "Gwei";

    String EXTRA_ADDRESS = "ADDRESS";
    String EXTRA_CONTRACT_ADDRESS = "CONTRACT_ADDRESS";
    String EXTRA_DECIMALS = "DECIMALS";
    String EXTRA_SYMBOL = "SYMBOL";
    String EXTRA_BALANCE = "balance";

    String EXTRA_SENDING_TOKENS = "SENDING_TOKENS";
    String EXTRA_TO_ADDRESS = "TO_ADDRESS";
    String EXTRA_AMOUNT = "AMOUNT";
    String EXTRA_GAS_PRICE = "GAS_PRICE";
    String EXTRA_GAS_LIMIT = "GAS_LIMIT";

    String COINBASE_WIDGET_CODE = "88d6141a-ff60-536c-841c-8f830adaacfd";
    String SHAPESHIFT_KEY = "c4097b033e02163da6114fbbc1bf15155e759ddfd8352c88c55e7fef162e901a800e7eaecf836062a0c075b2b881054e0b9aa2324be7bc3694578493faf59af4";
    String CHANGELLY_REF_ID = "968d4f0f0bf9";
    String DONATION_ADDRESS = "0x9f8284ce2cf0c8ce10685f537b1fff418104a317";

    String DEFAULT_GAS_PRICE = "21000000000";

    String DEFAULT_GAS_LIMIT_FOR_ETH = "21000";
    String DEFAULT_GAS_LIMIT = "90000";
    String DEFAULT_GAS_LIMIT_FOR_TOKENS = "144000";
    String DEFAULT_GAS_LIMIT_FOR_NONFUNGIBLE_TOKENS = "432000";

    long GAS_LIMIT_MIN = 21000L;
    long GAS_PER_BYTE = 300;
    long GAS_LIMIT_MAX = 300000L;
    long GAS_PRICE_MIN = 1000000000L;
    long NETWORK_FEE_MAX = 20000000000000000L;
    int ETHER_DECIMALS = 18;

    public interface ErrorCode {

        int UNKNOWN = 1;
        int CANT_GET_STORE_PASSWORD = 2;
    }

    public interface Key {
        String WALLET = "wallet";
        String TRANSACTION = "transaction";
        String SHOULD_SHOW_SECURITY_WARNING = "should_show_security_warning";
    }

    ///////////////请求code///////////////
    int ADD_WALLET_FAIL = 0;//添加钱包失败
    int ADD_WALLET_SUCCEED = 1;//添加钱包成功

    //0 ： 卖家， 1 : 买家 ， 2 ：客服
    int OTC_BUYER_ORDERS = 1;//买家 buyer  1
    int OTC_SELLER_ORDERS = 0;//卖家 seller  0
    int OTC_ARBITER_ORDERS = 2;//客服 arbiter  2
}
