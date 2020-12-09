package com.token.mangowallet.net.eosmgp;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.MapUtils;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;

public class EOSParams {

    /**
     * @return 获取余额参数
     */
    public static Map<String, Object> getBalancePamars(String account, String code, String symbol) {
        Map<String, Object> map = new HashMap<>();
        map.put("account", account);
        map.put("code", code);
        map.put("symbol", symbol);
        return map;
    }

    /**
     * @return 获取用户名组
     */
    public static Map<String, Object> getKeyAccountsPamars(String publickey) {
        Map<String, Object> map = new HashMap<>();
        map.put("public_key", publickey);
        return map;
    }

    /**
     * @return 获取账号基本信息
     */
    public static Map<String, Object> getAccountPamars(String accountName) {
        Map<String, Object> map = new HashMap<>();
        map.put("account_name", accountName);
        return map;
    }

    /**
     * @return 获取交易记录
     */
    public static Map<String, Object> getTransactionPamars(String id) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        return map;
    }

    /**
     * @param pos          位置，整数
     * @param offset       偏移量，整数
     * @param account_name 合约托管账号，字符串
     * @return 获取交易记录
     */
    public static Map<String, Object> getActionsPamars(int pos, int offset, String account_name) {
        Map<String, Object> map = new HashMap<>();
        map.put("pos", pos);
        map.put("offset", offset);
        map.put("account_name", account_name);
        return map;
    }

    /**
     * @param payer    付款人
     * @param receiver 收受者
     * @param quant    数额
     * @return 内存买入
     */
    public static Map<String, Object> getBuyRAMPamars(String payer, String receiver, String quant) {
        Map<String, Object> map = new HashMap<>();
        map.put("payer", payer);
        map.put("receiver", receiver);
        map.put("quant", quant);
        return map;
    }

    /**
     * @param account 账户
     * @param bytes   数量内存（单位： byte）
     * @return 内存买入
     */
    public static Map<String, Object> getSellRAMPamars(String account, String bytes) {
        Map<String, Object> map = new HashMap<>();
        map.put("account", account);
        map.put("bytes", bytes);
        return map;
    }

    /**
     * @param from               账户
     * @param receiver           账户
     * @param stake_net_quantity 小数点必须要四位，带单位
     * @param stake_cpu_quantity 小数点必须要四位，带单位
     * @param transfer           transfer
     * @return 抵押
     */
    public static Map<String, Object> getDelegatebw(String from, String receiver, String stake_net_quantity,
                                                    String stake_cpu_quantity, int transfer) {
        Map map = MapUtils.newHashMap();
        map.put("from", from);
        map.put("receiver", receiver);
        map.put("stake_net_quantity", stake_net_quantity);
        map.put("stake_cpu_quantity", stake_cpu_quantity);
        map.put("transfer", transfer);
        return map;
    }

    /**
     * @param from               账户
     * @param receiver           账户
     * @param stake_net_quantity 小数点必须要四位，带单位
     * @param stake_cpu_quantity 小数点必须要四位，带单位
     * @return 抵押
     */
    public static Map<String, Object> getUnDelegatebw(String from, String receiver, String stake_net_quantity,
                                                      String stake_cpu_quantity) {
        Map map = MapUtils.newHashMap();
        map.put("from", from);
        map.put("receiver", receiver);
        map.put("unstake_net_quantity", stake_net_quantity);
        map.put("unstake_cpu_quantity", stake_cpu_quantity);
        return map;
    }

    /**
     * @return 获取余额参数
     */
    public static Map<String, Object> getCurrencyStatsPamars(String code, String symbol) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", code);
        map.put("symbol", symbol);
        return map;
    }

    public static RequestBody getRequestBody(Map<String, Object> map) {
        String json = GsonUtils.toJson(map);
//        LogUtils.dTag(Constants.TAG_EOS, "RequestBody json  " + json);
        return RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json);
    }

}
