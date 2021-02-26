package com.token.mangowallet.utils;

import com.token.mangowallet.net.common.BaseUrlUtils;

/**
 * 社群投票 链表参数
 * <p>
 * 正式表  测试表
 * 全局配置表:configs       testconfig
 * 方案表:schemes  testscheme
 * 投票详情表:votedetails testvoted
 * 投票记录表:voterecords testvoter
 * 奖励记录表:awards  testaward
 * 结算记录表:settles  testsettles
 */
public class AssociationVoteTable {


    /**
     * 获取全局配置表
     */
    public static String getConfigs() {
        return BaseUrlUtils.getInstance().isTest() ? "testconfig" : "configs";
    }

    /**
     * 获取方案表
     */
    public static String getSchemes() {
        return BaseUrlUtils.getInstance().isTest() ? "testscheme" : "schemes";
    }

    /**
     * 获取投票详情表
     */
    public static String getVotedetails() {
        return BaseUrlUtils.getInstance().isTest() ? "testvoted" : "votedetails";
    }

    /**
     * 获取投票记录表
     */
    public static String getVoterecords() {
        return BaseUrlUtils.getInstance().isTest() ? "testvoter" : "voterecords";
    }

    /**
     * 获取奖励记录表
     */
    public static String getAwards() {
        return BaseUrlUtils.getInstance().isTest() ? "testaward" : "awards";
    }

    /**
     * 获取结算记录表
     */
    public static String getSettles() {
        return BaseUrlUtils.getInstance().isTest() ? "testsettles" : "settles";
    }
}
