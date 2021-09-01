package com.token.mangowallet.net.common;

import com.google.gson.JsonObject;
import com.token.mangowallet.bean.AppHomeBean;
import com.token.mangowallet.bean.CurrencyPrice;
import com.token.mangowallet.bean.EthplorerResponse;
import com.token.mangowallet.bean.FindBean;
import com.token.mangowallet.bean.OrderIndexBean;
import com.token.mangowallet.bean.StoreHomeBean;
import com.token.mangowallet.utils.Constants;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import one.block.eosiojava.models.rpcProvider.request.GetBlockRequest;
import one.block.eosiojava.models.rpcProvider.request.GetRawAbiRequest;
import one.block.eosiojava.models.rpcProvider.request.GetRequiredKeysRequest;
import one.block.eosiojava.models.rpcProvider.request.PushTransactionRequest;
import one.block.eosiojava.models.rpcProvider.response.GetBlockResponse;
import one.block.eosiojava.models.rpcProvider.response.GetInfoResponse;
import one.block.eosiojava.models.rpcProvider.response.GetRawAbiResponse;
import one.block.eosiojava.models.rpcProvider.response.GetRequiredKeysResponse;
import one.block.eosiojava.models.rpcProvider.response.PushTransactionResponse;
import one.block.eosiojavarpcprovider.implementations.EosioJavaRpcProviderImpl;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RequestApi {
    //region Model supported APIs

    /**
     * Retrofit POST call to "chain/get_info" to an EOSIO blockchain.
     * This method gets called from {@link EosioJavaRpcProviderImpl#getInfo()} to get latest information about the pointing chain.
     *
     * @return Executable {@link Call} to return {@link GetInfoResponse} has latest information about a chain.
     */
    @Headers("urlname:" + Constants.EOS_URL)
    @POST("v1/chain/get_info")
    Call<GetInfoResponse> getInfo();

    /**
     * Retrofit POST call to "chain/get_block" to an EOSIO blockchain.
     * This method gets called from {@link EosioJavaRpcProviderImpl#getBlock(GetBlockRequest)} to get info/status of a specific block in the request.
     *
     * @param getBlockRequest Info of a specific block.
     * @return Executable {@link Call} to return {@link GetBlockResponse} has the info/status of a specific block in the request.
     */
    @Headers("urlname:" + Constants.EOS_URL)
    @POST("v1/chain/get_block")
    Call<GetBlockResponse> getBlock(@Body GetBlockRequest getBlockRequest);

    /**
     * Retrofit POST call to "chain/get_raw_abi" to an EOSIO blockchain.
     * This method gets called from {@link EosioJavaRpcProviderImpl#getRawAbi(GetRawAbiRequest)} to get serialized ABI of a smart contract in the request.
     *
     * @param getRawAbiRequest Info of a specific smart contract.
     * @return Executable {@link Call} to return {@link GetRawAbiResponse} has the serialized ABI of a smart contract in the request.
     */
    @Headers("urlname:" + Constants.EOS_URL)
    @POST("v1/chain/get_raw_abi")
    Call<GetRawAbiResponse> getRawAbi(@Body GetRawAbiRequest getRawAbiRequest);

    /**
     * Retrofit POST call to "chain/get_required_keys" to an EOSIO blockchain.
     * This method gets called from {@link EosioJavaRpcProviderImpl#getRequiredKeys(GetRequiredKeysRequest)} to get required keys to sign a transaction
     *
     * @param getRequiredKeysRequest Info to get required keys
     * @return Executable {@link Call} to return {@link GetRequiredKeysResponse} has the required keys to sign a transaction
     */
    @Headers("urlname:" + Constants.EOS_URL)
    @POST("v1/chain/get_required_keys")
    Call<GetRequiredKeysResponse> getRequiredKeys(@Body GetRequiredKeysRequest getRequiredKeysRequest);

    /**
     * Retrofit POST call to "chain/push_transaction" to an EOSIO blockchain.
     * This method gets called from {@link EosioJavaRpcProviderImpl#pushTransaction(PushTransactionRequest)} to Push transaction RPC call to broadcast a transaction to backend
     *
     * @param pushTransactionRequest the transaction to push with signatures.
     * @return Executable {@link Call} to return {@link PushTransactionResponse} has the push transaction response
     */
    @Headers("urlname:" + Constants.EOS_URL)
    @POST("v1/chain/push_transaction")
    Call<PushTransactionResponse> pushTransaction(@Body PushTransactionRequest pushTransactionRequest);
    //endregion


    //region Extra APIs
    // Chain APIs

    /**
     * Retrofit POST call to "chain/get_account" to an EOSIO blockchain.
     * This method gets called from {@link EosioJavaRpcProviderImpl#getAccount(RequestBody)}
     *
     * @param requestBody the request body to call 'get_account' API
     * @return Executable {@link Call} to return {@link ResponseBody} of 'get_account' API
     */
    @Headers("urlname:" + Constants.EOS_URL)
    @POST("v1/chain/get_account")
    Call<ResponseBody> getAccount(@Body RequestBody requestBody);

    /**
     * Retrofit POST call to "chain/push_transactions" to an EOSIO blockchain.
     * This method gets called from {@link EosioJavaRpcProviderImpl#pushTransactions(RequestBody)}
     *
     * @param requestBody the request body to call 'push_transactions' API
     * @return Executable {@link Call} to return {@link ResponseBody} of 'push_transactions' API
     */
    @Headers("urlname:" + Constants.EOS_URL)
    @POST("v1/chain/push_transactions")
    Call<ResponseBody> pushTransactions(@Body RequestBody requestBody);

    /**
     * Retrofit POST call to "chain/get_block_header_state" to an EOSIO blockchain.
     * This method get called from {@link EosioJavaRpcProviderImpl#getBlockHeaderState(RequestBody)}
     *
     * @param requestBody the request body to call 'get_block_header_state' API
     * @return Executable {@link Call} to return {@link ResponseBody} of 'get_block_header_state' API
     */
    @Headers("urlname:" + Constants.EOS_URL)
    @POST("v1/chain/get_block_header_state")
    Call<ResponseBody> getBlockHeaderState(@Body RequestBody requestBody);

    /**
     * Retrofit POST call to "chain/get_abi" to an EOSIO blockchain.
     * This method gets called from {@link EosioJavaRpcProviderImpl#getAbi(RequestBody)}
     *
     * @param requestBody the request body to call 'get_abi' API
     * @return Executable {@link Call} to return {@link ResponseBody} of 'get_abi' API
     */
    @Headers("urlname:" + Constants.EOS_URL)
    @POST("v1/chain/get_abi")
    Call<ResponseBody> getAbi(@Body RequestBody requestBody);

    /**
     * Retrofit POST call to "chain/get_currency_balance" to an EOSIO blockchain.
     * This method gets called from {@link EosioJavaRpcProviderImpl#getCurrencyBalance(RequestBody)}
     *
     * @param requestBody the request body to call 'get_currency_balance' API
     * @return Executable {@link Call} to return {@link ResponseBody} of 'get_currency_balance' API
     */
    @Headers("urlname:" + Constants.EOS_URL)
    @POST("v1/chain/get_currency_balance")
    Call<ResponseBody> getCurrencyBalance(@Body RequestBody requestBody);

    /**
     * Retrofit POST call to "chain/get_currency_stats" to an EOSIO blockchain.
     * This method gets called from {@link EosioJavaRpcProviderImpl#getCurrencyStats(RequestBody)}
     *
     * @param requestBody the request body to call 'get_currency_stats' API
     * @return Executable {@link Call} to return {@link ResponseBody} of 'get_currency_stats' API
     */
    @Headers("urlname:" + Constants.EOS_URL)
    @POST("v1/chain/get_currency_stats")
    Call<ResponseBody> getCurrencyStats(@Body RequestBody requestBody);

    /**
     * Retrofit POST call to "chain/get_producers" to an EOSIO blockchain.
     * This method gets called from {@link EosioJavaRpcProviderImpl#getProducers(RequestBody)}
     *
     * @param requestBody the request body to call 'get_producers' API
     * @return Executable {@link Call} to return {@link ResponseBody} of 'get_producers' API
     */
    @Headers("urlname:" + Constants.EOS_URL)
    @POST("v1/chain/get_producers")
    Call<ResponseBody> getProducers(@Body RequestBody requestBody);

    /**
     * Retrofit POST call to "chain/get_raw_code_and_abi" to an EOSIO blockchain.
     * This method gets called from {@link EosioJavaRpcProviderImpl#getRawCodeAndAbi(RequestBody)}
     *
     * @param requestBody the request body to call 'get_raw_code_and_abi' API
     * @return Executable {@link Call} to return {@link ResponseBody} of 'get_raw_code_and_abi' API
     */
    @Headers("urlname:" + Constants.EOS_URL)
    @POST("v1/chain/get_raw_code_and_abi")
    Call<ResponseBody> getRawCodeAndAbi(@Body RequestBody requestBody);

    /**
     * Retrofit POST call to "chain/get_table_by_scope" to an EOSIO blockchain.
     * This method get called from {@link EosioJavaRpcProviderImpl#getTableByScope(RequestBody)}
     *
     * @param requestBody the request body to call 'get_table_by_scope' API
     * @return Executable {@link Call} to return {@link ResponseBody} of 'get_table_by_scope' API
     */
    @Headers("urlname:" + Constants.EOS_URL)
    @POST("v1/chain/get_table_by_scope")
    Call<ResponseBody> getTableByScope(@Body RequestBody requestBody);

    /**
     * Retrofit POST call to "chain/get_table_rows" to an EOSIO blockchain.
     * This method gets called from {@link EosioJavaRpcProviderImpl#getTableRows(RequestBody)}
     *
     * @param requestBody the request body to call 'get_table_rows' API
     * @return Executable {@link Call} to return {@link ResponseBody} of 'get_table_rows' API
     */
    @Headers("urlname:" + Constants.EOS_URL)
    @POST("v1/chain/get_table_rows")
    Call<ResponseBody> getTableRows(@Body RequestBody requestBody);

    /**
     * Retrofit POST call to "chain/get_code" to an EOSIO blockchain.
     * This method gets called from {@link EosioJavaRpcProviderImpl#getCode(RequestBody)}
     *
     * @param requestBody the request body to call 'get_code' API
     * @return Executable {@link Call} to return {@link ResponseBody} of 'get_code' API
     */
    @Headers("urlname:" + Constants.EOS_URL)
    @POST("v1/chain/get_code")
    Call<ResponseBody> getCode(@Body RequestBody requestBody);

    //History APIs

    /**
     * Retrofit POST call to "chain/get_actions" to an EOSIO blockchain.
     * This method gets called from {@link EosioJavaRpcProviderImpl#getActions(RequestBody)}
     *
     * @param requestBody the request body to call 'get_actions' API
     * @return Executable {@link Call} to return {@link ResponseBody} of 'get_actions' API
     */
    @Headers("urlname:" + Constants.EOS_URL)
    @POST("v1/history/get_actions")
    Call<ResponseBody> getActions(@Body RequestBody requestBody);

    /**
     * Retrofit POST call to "chain/get_transaction" to an EOSIO blockchain.
     * This method gets called from {@link EosioJavaRpcProviderImpl#getTransaction(RequestBody)}
     *
     * @param requestBody the request body to call 'get_transaction' API
     * @return Executable {@link Call} to return {@link ResponseBody} of 'get_transaction' API
     */
    @Headers("urlname:" + Constants.EOS_URL)
    @POST("v1/history/get_transaction")
    Call<ResponseBody> getTransaction(@Body RequestBody requestBody);

    /**
     * Retrofit POST call to "chain/get_key_accounts" to an EOSIO blockchain.
     * This method gets called from {@link EosioJavaRpcProviderImpl#getKeyAccounts(RequestBody)}
     *
     * @param requestBody the request body to call 'get_key_accounts' API
     * @return Executable {@link Call} to return {@link ResponseBody} of 'get_key_accounts' API
     */
    @Headers("urlname:" + Constants.EOS_URL)
    @POST("v1/history/get_key_accounts")
    Call<ResponseBody> getKeyAccounts(@Body RequestBody requestBody);

    /**
     * Retrofit POST call to "chain/get_controlled_accounts" to an EOSIO blockchain.
     * This method gets called from {@link EosioJavaRpcProviderImpl#getControlledAccounts(RequestBody)}
     *
     * @param requestBody the request body to call 'get_controlled_accounts' API
     * @return Executable {@link Call} to return {@link ResponseBody} of 'get_controlled_accounts' API
     */
    @Headers("urlname:" + Constants.EOS_URL)
    @POST("v1/history/get_controlled_accounts")
    Call<ResponseBody> getControlledAccounts(@Body RequestBody requestBody);
    //endregion

    /**
     * 调用停用当前出块器。
     *
     * @return
     */
    @Headers("urlname:" + Constants.EOS_URL)
    @POST("v1/producer/pause")
    Observable<String> getPause();

    /**
     * 调用返回当前出块器是否暂停。
     *
     * @return
     */
    @Headers("urlname:" + Constants.EOS_URL)
    @POST("v1/producer/paused")
    Observable<String> getPaused();

    /**
     * 调用返回当前出块器的运行参数。
     *
     * @return
     */
    @Headers("urlname:" + Constants.EOS_URL)
    @POST("v1/producer/get_runtime_options")
    Observable<String> getRuntimeOptions();

    /**
     * 调用可以修改出块运行参数。
     *
     * @return
     */
    @Headers("urlname:" + Constants.EOS_URL)
    @POST("v1/producer/update_runtime_options")
    Observable<String> getUpdateRuntimeOptions(@Body RequestBody requestBody);

    /**
     * 调用返回出块灰名单。
     *
     * @return
     */
    @Headers("urlname:" + Constants.EOS_URL)
    @POST("v1/producer/get_greylist")
    Observable<String> getGreylist();

    /**
     * 调用向出块灰名单中添加指定的账号。
     *
     * @return
     */
    @Headers("urlname:" + Constants.EOS_URL)
    @POST("v1/producer/add_greylist_accounts")
    Observable<String> getAddGreylistAccounts(@Body RequestBody requestBody);

    /**
     * 调用从出块灰名单中删除指定的账号。
     *
     * @return
     */
    @Headers("urlname:" + Constants.EOS_URL)
    @POST("v1/producer/remove_greylist_accounts")
    Observable<String> getRemoveGreylistAccounts(@Body RequestBody requestBody);

    /**
     * 调用返回当前应用的出块白名单和黑名单。
     *
     * @return
     */
    @Headers("urlname:" + Constants.EOS_URL)
    @POST("v1/producer/get_whitelist_blacklist")
    Observable<String> getWhitelistBlacklist();

    /**
     * 调用设置出块白名单和黑名单。
     *
     * @return
     */
    @Headers("urlname:" + Constants.EOS_URL)
    @POST("v1/producer/set_whitelist_blacklist")
    Observable<String> setWhitelistBlacklist();

//    /**
//     * 去交易所获取数字货币价格
//     *
//     * @param pair 数字货币类型_货币单位 如：EOS_USDT EOS兑换美元的价额
//     * @return
//     */
//    @Headers("urlname:" + Constants.DIGICCY_PRICE_URL)
//    @GET("/v1/q/ticker/price")
//    Observable<CurrencyPrice> getDIGICCYPrice(@Query("pair") String pair);

    /**
     * appType  1.ios  2.android
     * apkName 1.mangoworld 2.mangowallet 3.mangowalletnew
     * version 当前APP版本号
     *
     * @return
     */
    @Headers("urlname:" + Constants.CORPORATION_URL)
    @POST("/api/appVersion/check")
    Observable<JsonObject> getVersion(@Header("appType") String appType,
                                      @Header("apkName") String apkName,
                                      @Header("version") String version);

    /**
     * @param content content
     * @return
     */
    @Headers("urlname:" + Constants.CORPORATION_URL)
    @POST("/user/findMgp")
    Observable<FindBean> getFindMgp(@Header("content") String content);

    /**
     * @param content accountName
     * @return
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.CORPORATION_URL)
    @POST("/user/orderIndex")
    Observable<OrderIndexBean> getOrderIndex(@Header("content") String content);

    /**
     * @return
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.CORPORATION_URL)
    @POST("/user/realTimeIndex")
    Observable<JsonObject> getRealTimeIndex();


    /**
     * @param content accountName
     * @return
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.CORPORATION_URL)
    @POST("/user/mortgage")
    Observable<JsonObject> getMortgageAssociation(@Header("content") String content);

    /**
     * @param content accountName
     * @return
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.CORPORATION_URL)
    @POST("/user/bindMgp")
    Observable<JsonObject> getBindMidMgp(@Header("content") String content);

    /**
     * @param content accountName
     * @return
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.CORPORATION_URL)
    @POST("/user/nodeIndex")
    Observable<JsonObject> getNodeIndex(@Header("content") String content);

    /**
     * "address", address
     * "limit", "100"
     * "page", "1"
     *
     * @param content content
     * @return
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.CORPORATION_URL)
    @POST("/user/myOrderWallet")
    Observable<JsonObject> getMyOrderWallet(@Header("content") String content);

    /**
     * "address", address
     * "moneyType", moneyType
     *
     * @param content content
     * @return
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.CORPORATION_URL)
    @POST("/order/withdraw/withdrawIndex")
    Observable<JsonObject> getWithdrawIndex(@Header("content") String content);

    /**
     * "address", address
     * "moneyType", moneyType
     *
     * @param content content
     * @return
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.CORPORATION_URL)
    @POST("/order/withdraw/addOrder")
    Observable<JsonObject> getWithdrawAddOrder(@Header("content") String content);

    /**
     * "address", address
     * "limit", "100"
     * "page", "1"
     *
     * @param content content
     * @return
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.CORPORATION_URL)
    @POST("/user/indexMarkIndex")
    Observable<JsonObject> getIndexMarkIndex(@Header("content") String content);

    /**
     * "currentAddr", address
     * "type", "2";1:ETH,2:MGP
     *
     * @param content content
     * @return
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.CORPORATION_URL)
    @POST("/userTransactionRecord/newCheckstatus")
    Observable<JsonObject> newCheckstatus(@Header("content") String content);

    /**
     * "bindAddr", address
     * "type", "2";1:ETH,2:MGP
     * "currentAddr", address
     *
     * @param content content
     * @return
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.CORPORATION_URL)
    @POST("/userTransactionRecord/add")
    Observable<JsonObject> bindWalletAddress(@Header("content") String content);

    /**
     * "bindAdress", bindAdress
     * "type", "2";1:ETH,2:MGP
     * "address", address
     *
     * @param content content
     * @return
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.CORPORATION_URL)
    @POST("/userTransactionRecord/newCheckAddr")
    Observable<JsonObject> newCheckAddr(@Header("content") String content);

    /**
     * "address", bindAdress
     * "type", "2";1:ETH,2:MGP
     *
     * @param content content
     * @return
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.CORPORATION_URL)
    @POST("/userTransactionRecord/find")
    Observable<JsonObject> isFindMIDBinding(@Header("content") String content);

    /**
     * 云矿机收益明细
     * "address", string
     * "limit", "100"
     * "page", "1"
     *
     * @param content content
     * @return
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.CORPORATION_URL)
    @POST("order/miningOrderIncome")
    Observable<JsonObject> getMiningOrderIncome(@Header("content") String content);

    /**
     * 商城首页
     *
     * @return
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.CORPORATION_URL)
    @POST("/appStoreCategory/getCategoryProduct")
    Observable<StoreHomeBean> getCategoryProduct(@Header("content") String content);

    /**
     * 购买订单
     * <p>
     * address		支付账户
     * productId		商品id
     * detailedAddress	详情地址
     * userName		收件人
     * productNum		件数
     * city		城市
     * productPrice		商品单价
     * totalPostage		商品邮费
     * phone		收件人电话号码
     * payMgp		订单总支付mgp数量
     * mgpPrice  mgp单价
     *
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.CORPORATION_URL)
    @POST("/appStoreOrder/buyOrder")
    Observable<JsonObject> buyOrder(@Header("content") String content);
//
//    /**
//     * 订单支付
//     * address 支付地址
//     * orderSn 订单号
//     *
//     * @Header("content:") String content
//     */
//    @Headers("urlname:" + Constants.CORPORATION_URL)
//    @POST("/appStorePay/payOrder")
//    Observable<JsonObject> payOrder(@Header("content") String content);

    /**
     * 卖家查询订单详情
     * <p>
     * address 支付地址
     * type    0 全部  1 待付款  2 待发货  3 发货中  4 已收货 5 退款中/已退款 /退款失败
     *
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.CORPORATION_URL)
    @POST("/appStoreOrder/getOutOrder")
    Observable<JsonObject> getOutOrder(@Header("content") String content);

    /**
     * 销售模式默认设置
     *
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.CORPORATION_URL)
    @POST("/appStore/saleModel/defaultModel")
    Observable<JsonObject> defaultModel(@Header("content") String content);

    /**
     * 获取所有商品分类
     *
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.CORPORATION_URL)
    @POST("/appStoreCategory/allCategory")
    Observable<JsonObject> allCategory();

    /**
     * 卖家添加商品
     * address mgp账户名
     * cateId 商品分类ID
     * image		商品图片
     * isNew		是否是新的 true or false
     * isPostage		是否包邮 true or false
     * postage		邮费
     * price		商品价格
     * sliderImage		轮播图
     * stock		库存
     * storeInfo		商品简介
     * storeName		商品名称
     * storeUnit		台 货物单位
     * givePro      卖家实际收取
     * bonusPro      奖金激励
     * buyerPro      购买者激励
     * storeType		商品规格
     *
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.CORPORATION_URL)
    @POST("/appStoreProduct/addPro")
    Observable<JsonObject> addPro(@Header("content") String content);

    /**
     * 是否是商家
     * address 支付地址
     *
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.CORPORATION_URL)
    @POST("/appStore/isMers")
    Observable<JsonObject> isMer(@Header("content") String content);

    /**
     * 上传图片
     * file 图片
     *
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.CORPORATION_URL)
    @Multipart
    @POST("/file/upload")
    Observable<JsonObject> upload(@Part("file") MultipartBody multipartBody);

    /**
     * 条件查询商品
     * productName		 商品名称
     * price	 1价格从高到低  0价格从低到高  默认是0
     * cateId		 分类ID
     * sales		 1 按销量从高到低 0从低到高 默认是0
     * page		 默认为1 第一页
     * limit		 默认为10
     * recommend		 推荐 1 浏览量由高到低  0由低到高
     *
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.CORPORATION_URL)
    @POST("/appStoreProduct/proList")
    Observable<JsonObject> proList(@Header("content") String content);

    /**
     * 添加订单
     * address		 用户地址或账户名
     * productId	 商品ID
     * detailedAddress		 收货详细地址
     * userName		 用户名称
     * productNum		 购买商品数量
     * city		 收货城市
     * productPrice		 商品价格
     * totalPostage		 邮费
     * mark 用户备注
     * phone 电话
     *
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.CORPORATION_URL)
    @POST("/appStoreOrder/addOrder")
    Observable<JsonObject> addOrder(@Header("content") String content);

    /**
     * 更新商品浏览量
     * productId		 商品ID（proID）
     *
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.CORPORATION_URL)
    @POST("/appStoreProduct/browse")
    Observable<JsonObject> browse(@Header("content") String content);

    /**
     * 买家获取订单详情
     * address		 地址
     * type 0 全部  1 待付款  2 待发货  3 发货中  4 已收货 5 退款中/已退款 /退款失败
     *
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.CORPORATION_URL)
    @POST("/appStoreOrder/getOrder")
    Observable<JsonObject> getOrder(@Header("content") String content);

    /**
     * 添加收货地址
     * address		 地址
     * phone 电话
     * city 市
     * detailedAddress 详细地址
     * isDefault 是否为默认 true是 false否
     * userName 名称
     *
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.CORPORATION_URL)
    @POST("/appStoreUserAddr/addUserAddr")
    Observable<JsonObject> addUserAddr(@Header("content") String content);

    /**
     * 查询所有收货地址
     * address		 地址
     *
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.CORPORATION_URL)
    @POST("/appStoreUserAddr/findAddr")
    Observable<JsonObject> findAddr(@Header("content") String content);

    /**
     * 删除收货地址
     * addrID		 地址addrID
     * address
     *
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.CORPORATION_URL)
    @POST("/appStoreUserAddr/delAddr")
    Observable<JsonObject> delAddr(@Header("content") String content);

    /**
     * 更新地址
     * id		 addrID
     * userId
     * userName
     * phone
     * city
     * detailedAddress
     * isDefault       true or false
     *
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.CORPORATION_URL)
    @POST("/appStoreUserAddr/updateAddr")
    Observable<JsonObject> updateAddr(@Header("content") String content);

    /**
     * 用户取消订单
     * orderId		 生成的订单号
     * address
     *
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.CORPORATION_URL)
    @POST("/appStoreOrder/delOrder")
    Observable<JsonObject> delOrder(@Header("content") String content);

    /**
     * 商户查询自己的商品
     * address
     * type      0全部默认 1销售中 2已售完 3 仓库中
     *
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.CORPORATION_URL)
    @POST("/appStoreProduct/merPro")
    Observable<JsonObject> merPro(@Header("content") String content);

    /**
     * 商户上下架商品
     * address
     * type      true上架false下架
     * proID 商品ID
     *
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.CORPORATION_URL)
    @POST("/appStoreProduct/upDown")
    Observable<JsonObject> upDown(@Header("content") String content);

    /**
     * 商户删除商品
     * address
     * proID
     *
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.CORPORATION_URL)
    @POST("/appStoreProduct/delPro")
    Observable<JsonObject> delPro(@Header("content") String content);

    /**
     * 商户编辑商品
     * address
     * image		 商品图文
     * sliderImage 商品轮播图
     * storeUnit		 商品单位
     * storeName		 商品名称
     * storeInfo		 小米手机       商品简介
     * storeType		 金,12+256GB,官方标配      商品类型
     * keyword		 关键字
     * cateId	商品分类
     * price		 商品价格
     * postage		 邮费
     * stock		 库存
     * isShow		 true上架false下架
     * isNew		 是否时新品 true是false不是
     * isPostage		 是否包邮true是false不是
     * givePro		 商家实际收取
     * bonusPro		 奖金激励
     * buyerPro		 购买者激励
     *
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.CORPORATION_URL)
    @POST("/appStoreProduct/upPro")
    Observable<JsonObject> upPro(@Header("content") String content);

    /**
     * 商家确认发货
     * address
     * orderId  订单号
     * num 快递单号
     *
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.CORPORATION_URL)
    @POST("/appStoreOrder/merConfirm")
    Observable<JsonObject> merConfirm(@Header("content") String content);

    /**
     * 买家确认收货
     * address
     * orderId  订单号
     *
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.CORPORATION_URL)
    @POST("/appStoreOrder/buyerConfirm")
    Observable<JsonObject> buyerConfirm(@Header("content") String content);

    /**
     * 用户发起退款
     * orderId
     * money  订单号
     * mark     退款原因
     * msg      退款说明
     * image   退款凭证(图文)
     * address
     *
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.CORPORATION_URL)
    @POST("/appStoreOrderRefund/startRefund")
    Observable<JsonObject> startRefund(@Header("content") String content);

    /**
     * 商城首页收益
     * address
     *
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.CORPORATION_URL)
    @POST("/appStore/getIncome")
    Observable<JsonObject> getIncome(@Header("content") String content);

    /**
     * 首页显示
     * type : 0内部1外部
     *
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.CORPORATION_URL)
    @POST("/application/home")
    Observable<AppHomeBean> getHome(@Header("content") String content);

    /**
     * 获取币种价格
     * pair : MGP_USDT
     * 币种信息
     *
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.CORPORATION_URL)
    @POST("/api/coinPrice")
    Observable<CurrencyPrice> getCoinPrice(@Header("content") String content);


    /**
     * 获取国家
     *
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.CORPORATION_URL)
    @POST("/appCountry/getCountry")
    Observable<JsonObject> getCountry();

    /**
     * 币价设置
     *
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.CORPORATION_URL)
    @POST("/api/coin_symbol")
    Observable<JsonObject> getCoinSymbol();

    /**
     * 本地生活
     * 分类
     *
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.CORPORATION_URL)
    @POST("/api/appStoreLife/category")
    Observable<JsonObject> getCategory(@Header("content") String content);

    /**
     * 本地生活
     * 商铺列表
     *
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.CORPORATION_URL)
    @POST("/api/appStoreLife")
    Observable<JsonObject> getAppStoreLife(@Header("content") String content);

    /**
     * 本地生活
     * 获取买单信息
     * storeId : 店铺id
     * address : 账号
     *
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.CORPORATION_URL)
    @POST("/api/appStoreLife/order/payInit")
    Observable<JsonObject> getPayInit(@Header("content") String content);

    /**
     * 本地生活
     * 买单
     * money : 美金数
     * gainAddress : 收款地址
     * address : 下单地址
     * storeId : 商铺id
     *
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.CORPORATION_URL)
    @POST("/api/appStoreLife/order/addOrder")
    Observable<JsonObject> lifeAddOrder(@Header("content") String content);

    /**
     * 本地生活
     * 支付信息上传
     * orderSn : 订单号
     * payNum : 支付数量
     * payPrice : 支付时的价格
     * hash : 转账hash
     * address : 支付地址
     *
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.CORPORATION_URL)
    @POST("/api/appStoreLife/order/payOrder")
    Observable<JsonObject> lifePayOrder(@Header("content") String content);

    /**
     * 本地生活
     * 添加店铺
     * address : mgp 地址
     * name : 店铺名称
     * homeImg : 首页图
     * detailImg : 轮播图
     * bankTime : 营业时间
     * country : 国家id
     * storeAddress : 详细地址
     * longitude : 经度
     * latitude : 纬度
     * storePro : 收取比例
     * buyerPro : 赠送比例
     * rewardPro : 奖励比例
     * categoryId : 分类id
     *
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.CORPORATION_URL)
    @POST("/api/appStoreLife/addStore")
    Observable<JsonObject> addStore(@Header("content") String content);

    /**
     * 本地生活
     * 添加店铺
     * address : mgp 地址
     * name : 店铺名称
     * homeImg : 首页图
     * detailImg : 轮播图
     * bankTime : 营业时间
     * country : 国家id
     * storeAddress : 详细地址
     * longitude : 经度
     * latitude : 纬度
     * storePro : 收取比例
     * buyerPro : 赠送比例
     * rewardPro : 奖励比例
     * categoryId : 分类id
     *
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.CORPORATION_URL)
    @POST("/api/appStoreLife/editStore")
    Observable<JsonObject> editStore(@Header("content") String content);

    /**
     * 本地生活
     * 添加店铺
     * address : mgp 地址
     * userName : 真实姓名
     * identityCard : 身份证
     * socialCode : 统一社会信用代码
     * businessLicense : 营业执照照片
     * identityCardPhoto : 身份证正反面
     * handCardPhoto : 详细地址
     * phone : 经度
     *
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.CORPORATION_URL)
    @POST("/appStoreUser/addUserInfo")
    Observable<JsonObject> addUserInfo(@Header("content") String content);

    /**
     * 商品查询
     * proId=1 商品ID
     *
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.CORPORATION_URL)
    @POST("appStoreProduct/find")
    Observable<JsonObject> proFind(@Header("content") String content);

    /**
     * 获取支付方式
     * payIds
     *
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.CORPORATION_URL)
    @POST("appStorePayConfig/getPayConfig")
    Observable<JsonObject> getPayConfig();

    /**
     * 商品查询
     * address 商品ID
     * page
     * size
     *
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.CORPORATION_URL)
    @POST("/api/userOrder/blend/list")
    Observable<JsonObject> getMixMortgageList(@Header("content") String content);

    /**
     * 混合抵押hash 首次上传
     * address 商品ID
     * moneyType 币种
     * hash
     * num 数量
     *
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.CORPORATION_URL)
    @POST("/api/userOrder/blend/uploadHash")
    Observable<JsonObject> uploadHash(@Header("content") String content);

    /**
     * 混合抵押hash二次上传
     * address 商品ID
     * hash
     * id 进度id
     * moneyType 币种
     *
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.CORPORATION_URL)
    @POST("/api/userOrder/blend/editHash")
    Observable<JsonObject> editHash(@Header("content") String content);

    /**
     * 绑定usdt充值保证金
     * mgpName
     * usdtAddr
     * hash
     * money
     * type 0首次开通1充值
     *
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.CORPORATION_URL)
    @POST("/userBindUsdt/bindUsdt")
    Observable<JsonObject> bindUsdt(@Header("content") String content);

    /**
     * 添加usdt交易记录
     * mgpName
     * hash
     * orderId
     *
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.CORPORATION_URL)
    @POST("/appStorePayUsdt/addRecord")
    Observable<JsonObject> addRecord(@Header("content") String content);

    /**
     * usdt支付商家手动确认
     * address
     * orderId
     *
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.CORPORATION_URL)
    @POST("/appStoreOrder/confirm")
    Observable<JsonObject> confirm(@Header("content") String content);

    /**
     * 获取保证金记录
     * address
     *
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.CORPORATION_URL)
    @POST("/userCash/getLog")
    Observable<JsonObject> getLog(@Header("content") String content);

    /**
     * 获取保证金提示说明
     * address
     *
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.CORPORATION_URL)
    @POST("userCash/lower")
    Observable<JsonObject> lower(@Header("content") String content);

    /**
     * 注销退回保证金
     * address
     *
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.CORPORATION_URL)
    @POST("userCashRefundOrder/refund")
    Observable<JsonObject> marginRefund(@Header("content") String content);

    /**
     * 是否开启投递方案
     * address
     *
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.CORPORATION_URL)
    @POST("/voteTheme/isScheme")
    Observable<JsonObject> isScheme(@Header("content") String content);

    /**
     * 添加投票方案
     * address
     * voteTitle        主题
     * voteContent      描述
     * schemeContent    方案
     *
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.CORPORATION_URL)
    @POST("/voteTheme/addTheme")
    Observable<JsonObject> addTheme(@Header("content") String content);

    /**
     * 获取全部方案
     * page        页数
     * limit      数量
     *
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.CORPORATION_URL)
    @POST("/voteTheme/themes")
    Observable<JsonObject> themes(@Header("content") String content);

    /**
     * 是否开启投票
     * address
     *
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.CORPORATION_URL)
    @POST("/voteTheme/isVote")
    Observable<JsonObject> isVote(@Header("content") String content);

    /**
     * 检测是不是超级节点
     * mgpName
     *
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.CORPORATION_URL)
    @POST("/voteTheme/checkSuperNode")
    Observable<JsonObject> checkSuperNode(@Header("content") String content);

    /**
     * 超级节点发起投票
     * address 超级节点账户
     * voteId 方案ID
     * money 金额
     *
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.CORPORATION_URL)
    @POST("/voteTheme/addSuperVote")
    Observable<JsonObject> addSuperVote(@Header("content") String content);

    /**
     * 提交hash及抵押金额
     * voteId  添加投票方案返回的内容
     * hash
     * money   押金
     *
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.CORPORATION_URL)
    @POST("/voteTheme/upHash")
    Observable<JsonObject> upHash(@Header("content") String content);

    /**
     * 提交hash及抵押金额
     * page  添加投票方案返回的内容
     * limit
     * address   押金
     *
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.CORPORATION_URL)
    @POST("/voteTheme/myTheme")
    Observable<JsonObject> myTheme(@Header("content") String content);

    /**
     * 投票记录
     * page  添加投票方案返回的内容
     * limit
     * address   押金
     *
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.CORPORATION_URL)
    @POST("/voteTheme/getAward")
    Observable<JsonObject> getAward(@Header("content") String content);

    /**
     * 投票记录
     * voteId  外层voteId
     * address
     * voteTitle   主题
     * voteContent  主题描述
     * schemeContent  方案
     *
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.CORPORATION_URL)
    @POST("/voteTheme/update")
    Observable<JsonObject> votesUpdate(@Header("content") String content);

    /**
     * 投票记录
     * voteId  外层voteId
     * address
     * voteTitle   主题
     * voteContent  主题描述
     * schemeContent  方案
     *
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.CORPORATION_URL)
    @POST("/voteLog/getVotes")
    Observable<JsonObject> participateUpdate(@Header("content") String content);

    /**
     * num 追加的币数
     *
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.CORPORATION_URL)
    @POST("/api/userOrder/blend/blendOrderRatioByDollar")
    Observable<JsonObject> orderSys(@Header("content") String content);

    /**
     * 获取投递方案需要的押金
     * address
     *
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.CORPORATION_URL)
    @POST("/voteTheme/getSchemeMoney")
    Observable<JsonObject> getSchemeMoney(@Header("content") String content);

    /**
     * 获取投递方案需要的押金
     * address
     * limit
     * page
     *
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.CORPORATION_URL)
    @POST("/api/appStoreLife/order/orderList")
    Observable<JsonObject> orderList(@Header("content") String content);

    /**
     * 混合抵押hash二次上传
     * address 商品ID
     * hash
     * id 进度id
     * moneyType 币种
     *
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.TOKENURL)
    @GET("/getAddressInfo/{address}?apiKey=freekey")
    Observable<Response<EthplorerResponse>> fetchTokens(@Path("address") String address);


    /**
     * 竞选节点信息上传
     * mgpAddress      参与竞选的地址
     * nodeContent     社群介绍
     * nodeHeadImg     头像
     * nodeName        名称
     * nodeRewardRule  节点奖励规则
     * nodeShareRatio  分享比例  10% = 10000
     * hash            hash
     * nodeUrl         节点
     *
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.VOTEURL)
    @POST("/api/voteNode/uploadNodeMsg")
    Observable<JsonObject> uploadNodeMsg(@Header("content") String content);

    /**
     * 节点详情
     * address      申请节点的地址
     *
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.VOTEURL)
    @POST("/api/voteNode/nodeDetail")
    Observable<JsonObject> nodeDetail(@Header("content") String content);

    /**
     * 节点规则
     *
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.VOTEURL)
    @POST("/api/voteNode/rule")
    Observable<JsonObject> nodeRule();

    /**
     * 节点列表
     *
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.VOTEURL)
    @POST("/api/voteNode/nodeList")
    Observable<JsonObject> nodeList();

    /**
     * 获取投递方案需要的押金
     * address
     * limit
     * page
     *
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.CORPORATION_URL)
    @POST("/user/userLogin")
    Observable<JsonObject> userLogin();

    /**
     * 获取投递方案需要的押金
     * address
     * limit
     * page
     *
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.VOTEURL)
    @POST("/api/voteNode/scNodeList")
    Observable<JsonObject> scNodeList(@Header("content") String content);

    /**
     * 获取投递方案需要的押金
     * address
     * limit
     * page
     *
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.VOTEURL)
    @POST("/api/voteNode/votes")
    Observable<JsonObject> votes(@Header("content") String content);


    /**
     * 账号自动激活
     * publicKey 公钥
     * account 账号
     *
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.CORPORATION_URL)
    @POST("/user/userRegister")
    Observable<JsonObject> userRegister(@Header("content") String content);

    /**
     * 账号自动激活
     * mgpName mgp账户
     * type :买家=0，卖家=1；
     *
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.OTCURL)
    @POST("/otcapi/api/moUsers/isBind")
    Observable<JsonObject> isBind(@Header("content") String content);

    /**
     * 账号自动激活
     * mgpName mgp账户
     * type :买家=0，卖家=1；
     *
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.OTCURL)
    @POST("/otcapi/api/moUsers/find")
    Observable<JsonObject> getContactInfo(@Header("content") String content);

    /**
     * 保存联系方式
     * mgpName
     * 邮箱 type =0； mail ；code；
     * 手机号码 type =1；phone；
     * 微信号 type =2；weixin；
     *
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.OTCURL)
    @POST("/otcapi/api/moUsers/save")
    Observable<JsonObject> saveContactInfo(@Header("content") String content);

    /**
     * 发送验证码
     * 1、 mail: String,  1. 绑定时的邮箱 ；type=2、3不传mail；
     * 2、 mgpName: String,  type: 1当前账户； 2卖家账户 ； 3 买家账户；
     * 3、 type: Int,  1邮箱绑定；2订单支付通知；3.放行；
     * 4、money: Double?   type： 1：不传money； 2：购买的MGP； 3需要打款的MGP
     *
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.OTCURL)
    @POST("/otcapi/api/email/send")
    Observable<JsonObject> sendVerificationCode(@Header("content") String content);

    /**
     * 获取收款方式
     * mgpName
     *
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.OTCURL)
    @POST("/otcapi/api/moPayInfo/list")
    Observable<JsonObject> getPayInfoList(@Header("content") String content);

    /**
     * 添加及修改支付信息
     * mgpName
     * cardNum 银行卡号 微信账户 支付宝账户
     * id 用户返回的掉save接口时使用
     * name payId,1:银行名称，2.3可以不填
     * payId 支付方式1银行2微信3支付宝
     * qrCode 微信支付宝支付二维码
     * branch 银行支行
     * username 用户名
     * payInfoId 用户信息返回的payInfoId，如果存在就是修改不存在就是新增
     *
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.OTCURL)
    @POST("/otcapi/api/moPayInfo/save")
    Observable<JsonObject> savePayWay(@Header("content") String content);


    /**
     * 获取收款方式
     * mgpName
     * bucket otcstore.mgps.me
     * appName MGP
     *
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.MMGPSME)
    @Multipart
    @POST("/fileapi/file/uploadFile")
    Observable<JsonObject> uploadFile(@Part("file") MultipartBody multipartBody, @Query("bucket") String bucket, @Query("appName") String appName);

    /**
     * 获取收款方式
     * mgpName
     * bucket otcstore.mgps.me
     * appName MGP
     *
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.OTCURL)
    @POST("/otcapi/api/moUsers/payInfo")
    Observable<JsonObject> payInfo(@Header("content") String content);


    /**
     * 添加及修改支付信息
     * mgpName
     * cardNum 银行卡号 微信账户 支付宝账户
     * id 用户返回的掉save接口时使用
     * name payId,1:银行名称，2.3可以不填
     * payId 支付方式1银行2微信3支付宝
     * qrCode 微信支付宝支付二维码
     * branch 银行支行
     * username 用户名
     * payInfoId 用户信息返回的payInfoId，如果存在就是修改不存在就是新增
     *
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.OTCURL)
    @POST("/otcapi/api/moPayInfo/del")
    Observable<JsonObject> delPayWay(@Header("content") String content);

    /**
     * 上传凭证
     * <p>
     * arbitrator 仲裁者
     * buyer 买家
     * createTime	不用传
     * id 不用传
     * img 图片组成json
     * remark 备注
     * seller 卖家
     *
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.OTCURL)
    @POST("/api/arbitration/save")
    Observable<JsonObject> arbitrationSave(@Header("content") String content);

    /**
     * 获取收款方式
     * mgpName
     * bucket otcstore.mgps.me
     * appName MGP
     *
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.CORPORATION_URL)
    @POST("/api/userOrder/blend/blendChannel")
    Observable<JsonObject> blendChannel();

    /**
     * 获取收款方式
     * mgpName
     * bucket otcstore.mgps.me
     * appName MGP
     *
     * @Header("content:") String content
     */
    @Headers("urlname:" + Constants.CORPORATION_URL)
    @POST("/api/lp/order")
    Observable<JsonObject> LPOrder(@Header("content") String content);

}
