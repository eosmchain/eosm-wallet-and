package com.token.mangowallet.utils;

import android.os.Build;

import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.EncodeUtils;

import java.io.ByteArrayOutputStream;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

public class RSAUtils {

    public final static int MAX_ENCRYPT_BLOCK = 117;

    public static final String KEY_RSA = "RSA";

    private final static int MAX_DECRYPT_BLOCK = 128;

    public static final String ECB_PKCS1_PADDING = "RSA/ECB/PKCS1Padding";//加密填充方式

    public final static String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC1x7ZYbFgLbz77qVk6UjN1a2dH\n" +
            "ZRTnaIS7l5D8bg5tQb1eBSwWLBh7S1StvyGJkavRG43CdMdOsBCrC4XSs1Ombl9Z\n" +
            "bryAj0+drzf1orrVSjJ+oNgXb9VDK+bCPkZ+5wyKPn6VeGjzGQoPgeUkQFMXSFu+\n" +
            "opFr+9XW3DMjcRv//wIDAQAB";
    public final static String privateKey = "MIICXAIBAAKBgQC1x7ZYbFgLbz77qVk6UjN1a2dHZRTnaIS7l5D8bg5tQb1eBSwW\n" +
            "LBh7S1StvyGJkavRG43CdMdOsBCrC4XSs1Ombl9ZbryAj0+drzf1orrVSjJ+oNgX\n" +
            "b9VDK+bCPkZ+5wyKPn6VeGjzGQoPgeUkQFMXSFu+opFr+9XW3DMjcRv//wIDAQAB\n" +
            "AoGAIngDKJMqwshObhP3mmp53XsHM2+EXjjgal3cHTGVRLxhdxGOAYpM/hwTq3qB\n" +
            "ln9r0BPkdooEPPR/xJ9DuedNJgDmmk3GK59UXZyRgpep7nEGwygSI6f11u1kL4uZ\n" +
            "htB+Y+Oa2AkJ+cB5YnU98olclPKmzjhlLIAr/di2e6puH7ECQQDna0GcY6Bwg3Vh\n" +
            "smjl+lFQ0427vxCcob+qO0IDm2T9jfVPrEeTZw1dtoZHpykfOXDro4OYCUF9hvwX\n" +
            "OO99rHtXAkEAyRatP5886hTgspBMv8pB3lwS44vw16lJt8jbjgQGMnolEsZH9s5M\n" +
            "RVMv7tDoK6+yM9qNqjJeLCTUXmwYHydfmQJBAOIoWJtsZYvvfNR4VbmC8trplFbJ\n" +
            "t4NLM5M6jB4YTA7bH8S4Gc784/wUd5Ao1bA1I5y1VNJNIVp6g3xyYOJRoMsCQH1m\n" +
            "/OfeQDiiiHYS8ynv0h//P3BUBwAW/Hf2dKUd8VdtQM1yhdJuWLYq6IuAECmeSf14\n" +
            "YDwxaPUSirXPp0NWZJECQH9AO07V+ZvmYKYcua4nA5SksSXxNU8WIZuGAkT1QSkD\n" +
            "m98yqC2j5AccauAKZD0cw21KuHFiKvxV4GueSz9RklI=";

    /**
     * 随机生成密钥对
     *
     * @throws NoSuchAlgorithmException
     */
    public static void genKeyPair() throws NoSuchAlgorithmException {
        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_RSA);
        // 初始化密钥对生成器，密钥大小为96-1024位
        keyPairGen.initialize(1024, new SecureRandom());
        // 生成一个密钥对，保存在keyPair中
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();   // 得到私钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();  // 得到公钥
        String publicKeyString = EncodeUtils.base64Encode2String(publicKey.getEncoded());
        // 得到私钥字符串
        String privateKeyString = EncodeUtils.base64Encode2String((privateKey.getEncoded()));
        // 将公钥和私钥保存到Map
        System.out.println("公钥 : " + publicKeyString);
        System.out.println("私钥 : " + privateKeyString);
    }

    /** */
    /**
     * <p>
     * 公钥加密
     * </p>
     *
     * @return
     * @throws Exception
     */
    public static String encrypt(String str) throws Exception {
        java.security.Security.addProvider(
                new org.bouncycastle.jce.provider.BouncyCastleProvider()
        );
        try {
            // 将公钥由字符串转为UTF-8格式的字节数组
            byte[] publicKeyBytes = decryptBase64(publicKey);
            // 获得公钥
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
            // 取得待加密数据
            byte[] data = str.getBytes("UTF-8");
            KeyFactory factory;
            factory = KeyFactory.getInstance(KEY_RSA);
            PublicKey publicKey = factory.generatePublic(keySpec);
            // 对数据加密
            Cipher cipher = Cipher.getInstance(ECB_PKCS1_PADDING);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            // 返回加密后由Base64编码的加密信息
            int inputLen = data.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段解密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                    cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(data, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_ENCRYPT_BLOCK;
            }
            byte[] decryptedData = out.toByteArray();
            out.close();
            return encryptBase64(decryptedData);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String encryptBase64(byte[] key) {
        return EncodeUtils.base64Encode2String(key);
    }

    public static byte[] decryptBase64(String key) {
        return EncodeUtils.base64Decode(key);
    }

    public static String decrypt(String encryptedStr) {
        try {
            // 对私钥解密
            byte[] privateKeyBytes = decryptBase64(privateKey);
            // 获得私钥
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
            // 获得待解密数据
            byte[] data = decryptBase64(encryptedStr);
            KeyFactory factory;
            if (DeviceUtils.getSDKVersionCode() <= Build.VERSION_CODES.M) {
                factory = KeyFactory.getInstance(KEY_RSA, "BC");
            } else {
                factory = KeyFactory.getInstance(KEY_RSA);
            }
            PrivateKey privateKey = factory.generatePrivate(keySpec);
            // 对数据解密
            Cipher cipher = Cipher.getInstance(ECB_PKCS1_PADDING);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            // 返回UTF-8编码的解密信息
            int inputLen = data.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段解密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                    cache = cipher.doFinal(data, offSet, MAX_DECRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(data, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_DECRYPT_BLOCK;
            }
            byte[] decryptedData = out.toByteArray();
            out.close();
            return new String(decryptedData, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


}
