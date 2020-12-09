package com.token.mangowallet.utils;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.SPUtils;
import com.token.mangowallet.bean.CurrencyData;

import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

import static com.token.mangowallet.utils.Constants.KEY_CURRENCY_DATA;
import static com.token.mangowallet.utils.Constants.SP_MangoWallet_info;

public class BalanceUtils {
    private static String weiInEth = "1000000000000000000";

    public static BigDecimal weiToEth(BigInteger wei) {
        return Convert.fromWei(new BigDecimal(wei), Convert.Unit.ETHER);
    }

    public static String weiToEth(BigInteger wei, int sigFig) throws Exception {
        BigDecimal eth = weiToEth(wei);
        int scale = sigFig - eth.precision() + eth.scale();
        BigDecimal eth_scaled = eth.setScale(scale, RoundingMode.HALF_UP);
        return eth_scaled.toString();
    }

    public static String ethToUsd(String priceUsd, String ethBalance) {
        if (ObjectUtils.isEmpty(ethBalance)) {
            ethBalance = "0";
        }
        BigDecimal usd = new BigDecimal(ethBalance).multiply(new BigDecimal(priceUsd));
        usd = usd.setScale(2, RoundingMode.CEILING);
        return usd.toString();
    }

    public static String EthToWei(String eth) throws Exception {
        BigDecimal wei = new BigDecimal(eth).multiply(new BigDecimal(weiInEth));
        return wei.toBigInteger().toString();
    }

    public static BigDecimal weiToGweiBI(BigInteger wei) {
        return Convert.fromWei(new BigDecimal(wei), Convert.Unit.GWEI);
    }

    public static String weiToGwei(BigInteger wei) {
        return Convert.fromWei(new BigDecimal(wei), Convert.Unit.GWEI).toPlainString();
    }

    public static BigInteger gweiToWei(BigDecimal gwei) {
        return Convert.toWei(gwei, Convert.Unit.GWEI).toBigInteger();
    }

    public static BigDecimal tokenToWei(BigDecimal number, int decimals) {
        BigDecimal weiFactor = BigDecimal.TEN.pow(decimals);
        return number.multiply(weiFactor);
    }

    /**
     * Base - taken to mean default unit for a currency e.g. ETH, DOLLARS
     * Subunit - taken to mean subdivision of base e.g. WEI, CENTS
     *
     * @param baseAmountStr - decimal amonut in base unit of a given currency
     * @param decimals      - decimal places used to convert to subunits
     * @return amount in subunits
     */
    public static BigInteger baseToSubunit(String baseAmountStr, int decimals) {
        assert (decimals >= 0);
        BigDecimal baseAmount = new BigDecimal(baseAmountStr);
        BigDecimal subunitAmount = baseAmount.multiply(BigDecimal.valueOf(10).pow(decimals));
        try {
            return subunitAmount.toBigIntegerExact();
        } catch (ArithmeticException ex) {
            assert (false);
            return subunitAmount.toBigInteger();
        }
    }

    /**
     * @param subunitAmount - amouunt in subunits
     * @param decimals      - decimal places used to convert subunits to base
     * @return amount in base units
     */
    public static BigDecimal subunitToBase(BigInteger subunitAmount, int decimals) {
        assert (decimals >= 0);
        return new BigDecimal(subunitAmount).divide(BigDecimal.valueOf(10).pow(decimals));
    }

    /**
     * 标的物美元，转其他币种货币
     *
     * @param scale        保留小数点位数
     * @param roundingMode 计算用户拥有的金额，即余额就：RoundingMode.FLOOR（往下舍）；计算支付金额，即付款就：RoundingMode.UP（往上舍）
     * @param amount       - 总金额（单位：美元）
     * @param currencyData - 所要转换的货币数据
     * @return amount in base units
     */
    public static String currencyToBase(String amount, int scale, RoundingMode roundingMode) {
        CurrencyData data = getCurrencyData();
        if (ObjectUtils.isNotEmpty(data) && ObjectUtils.isNotEmpty(amount)) {
            if (ObjectUtils.isNotEmpty(data.getPrice())) {
                BigDecimal decimal = new BigDecimal(amount).multiply(new BigDecimal(data.getPrice())).setScale(scale, roundingMode);
                return data.getSymbol() + decimal.toPlainString();
            }
        }
        return "$" + APPUtils.dataFormat(amount);
    }

    public static CurrencyData getCurrencyData() {
        String currencyData = SPUtils.getInstance(SP_MangoWallet_info).getString(KEY_CURRENCY_DATA, "");
        if (ObjectUtils.isNotEmpty(currencyData)) {
            return GsonUtils.fromJson(currencyData, CurrencyData.class);
        }
        return null;
    }
}
