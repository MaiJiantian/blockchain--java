package com.maijiantian.handbyhandbc.utils;

import com.sun.org.apache.xml.internal.security.utils.Base64;
import org.apache.commons.io.FileUtils;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RSAUtils {

    /**
     * 生成公私钥,并保存在文件中,Java版本
     *
     * @param algorithm      : 算法
     * @param privateKeyPath : 保存私钥的文件路径
     * @param publicKeyPath  : 保存公钥的文件路径
     */
    public static void generateKeys(String algorithm, String privateKeyPath, String publicKeyPath) {
        try {
            // 密钥对生成器
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm);
            // 获取密钥对
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            // 获取公私钥
            PrivateKey privateKey = keyPair.getPrivate();
            PublicKey publicKey = keyPair.getPublic();
            // 获取公私钥字节数据
            byte[] privateKeyEncoded = privateKey.getEncoded();
            byte[] publicKeyEncoded = publicKey.getEncoded();
            // 对数据进行Base64编码
            String encodePrivateKey = Base64.encode(privateKeyEncoded);
            String encodePublicKey = Base64.encode(publicKeyEncoded);
            // 保存到文件
            FileUtils.writeStringToFile(new File(privateKeyPath), encodePrivateKey, "UTF-8");
            FileUtils.writeStringToFile(new File(publicKeyPath), encodePublicKey, "UTF-8");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成公私钥,并保存在文件中,JS版本
     *
     * @param algorithm      : 算法
     * @param privateKeyPath : 保存私钥的文件路径
     * @param publicKeyPath  : 保存公钥的文件路径
     */
    public static void generateKeysJS(String algorithm, String privateKeyPath, String publicKeyPath) {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm);

            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            PrivateKey privateKey = keyPair.getPrivate();
            PublicKey publicKey = keyPair.getPublic();

            byte[] privateKeyEncoded = privateKey.getEncoded();
            byte[] publicKeyEncoded = publicKey.getEncoded();

            String encodePrivateKey = Base64.encode(privateKeyEncoded);
            String encodePublicKey = Base64.encode(publicKeyEncoded);

            FileUtils.writeStringToFile(new File(privateKeyPath), "-----BEGIN PRIVATE KEY-----\n" + encodePrivateKey + "\n-----END PRIVATE KEY-----", "UTF-8");
            FileUtils.writeStringToFile(new File(publicKeyPath), encodePublicKey, "UTF-8");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 从文件中加载公钥
     *
     * @param algorithm     : 算法
     * @param publicKeyPath : 文件路径
     * @return : 公钥
     */
    public static PublicKey getPublicKeyFromFile(String algorithm, String publicKeyPath) {

        try {
            // 读取文件内容
            String encodePublicKey = FileUtils.readFileToString(new File(publicKeyPath), "UTF-8");
            // 转为公钥
            return getPublicKeyFromString(algorithm, encodePublicKey);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 从字符串中加载公钥
     *
     * @param algorithm       : 算法
     * @param publicKeyString : 字符串
     * @return : 公钥
     */
    public static PublicKey getPublicKeyFromString(String algorithm, String publicKeyString) {
        try {
            // 进行Base64解码
            byte[] publicKeyEncoded = Base64.decode(publicKeyString);
            // 获取密钥工厂
            KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
            // 获取公钥
            return keyFactory.generatePublic(new X509EncodedKeySpec(publicKeyEncoded));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 从文件中加载私钥
     *
     * @param algorithm      :  算法
     * @param privateKeyPath :  文件路径
     * @return : 私钥
     */
    public static PrivateKey getPrivateKeyFile(String algorithm, String privateKeyPath) {

        try {
            // 读取文件内容
            String encodePrivateKey = FileUtils.readFileToString(new File(privateKeyPath), "UTF-8");
            // 获取私钥
            return getPrivateKeyFromString(algorithm, encodePrivateKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 从字符串中加载私钥
     *
     * @param algorithm        :  算法
     * @param privateKeyString :  字符串
     * @return : 私钥
     */
    public static PrivateKey getPrivateKeyFromString(String algorithm, String privateKeyString) {
        try {
            // 进行Base64解码
            byte[] privateKeyEncoded = Base64.decode(privateKeyString);
            // 获取密钥工厂
            KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
            // 获取私钥
            return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(privateKeyEncoded));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 使用密钥进行非对称加密
     *
     * @param algorithm      : 算法
     * @param key            : 密钥
     * @param data           : 原数据
     * @param encryptMaxSize : 一次能够加密的最大数据量
     * @return : 密文
     */
    public static String encryptByKey(String algorithm, Key key, String data, int encryptMaxSize) {
        try {
            // 获取Cipher对象
            Cipher cipher = Cipher.getInstance(algorithm);
            // 指定加密模式和密钥
            cipher.init(Cipher.ENCRYPT_MODE, key);
            // 将原数据转为字节数组
            byte[] dataArray = data.getBytes();
            // 获取原数据的字节大小
            int total = dataArray.length;
            // 分段处理数据
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            decodeByte(encryptMaxSize, cipher, dataArray, total, baos);
            // 对密文进行Base64编码
            return Base64.encode(baos.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 分段处理数据
     *
     * @param maxSize   : 最大数据处理量
     * @param cipher    : Cipher对象
     * @param dataArray : 要处理的数据
     * @param total     : 总数据大小
     * @param baos      : 缓存数据对象
     */
    private static void decodeByte(int maxSize, Cipher cipher, byte[] dataArray, int total, ByteArrayOutputStream baos) throws Exception {
        byte[] buffer;
        int offset = 0;
        while (total - offset > 0) {
            // 剩余数据大于等于maxSize,就处理maxSize个
            if (total - offset >= maxSize) {
                buffer = cipher.doFinal(dataArray, offset, maxSize);
                offset += maxSize;
            } else {
                // 剩余数据小于maxSize,就处理剩余的
                buffer = cipher.doFinal(dataArray, offset, total - offset);
                offset = total;
            }
            baos.write(buffer);
        }
    }

    /**
     * 使用密钥进行非对称解密
     *
     * @param algorithm      : 算法
     * @param key            : 密钥
     * @param encryptedData  : 密文
     * @param decryptMaxSize : 一次能够解密的最大数据量
     * @return : 原文
     */
    public static String decryptByKey(String algorithm, Key key, String encryptedData, int decryptMaxSize) {

        try {
            // 对密文进行Base64解码
            byte[] decode = Base64.decode(encryptedData);
            // 获取Cipher
            Cipher cipher = Cipher.getInstance(algorithm);
            // 指定解密模式和密钥
            cipher.init(Cipher.DECRYPT_MODE, key);
            // 总数据大小
            int total = decode.length;
            // 处理数据
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            decodeByte(decryptMaxSize, cipher, decode, total, baos);
            return baos.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 生成签名,Java版本
     *
     * @param algorithm  : 算法
     * @param privateKey : 私钥
     * @param data       : 原文
     * @return : 签名
     */
    public static String getSignature(String algorithm, PrivateKey privateKey, String data) {
        try {
            // 获取签名对象
            Signature signature = Signature.getInstance(algorithm);
            // 传入私钥
            signature.initSign(privateKey);
            // 传入原文
            signature.update(data.getBytes());
            // 签名
            byte[] sign = signature.sign();
            // 对签名数据进行Base64编码
            return Base64.encode(sign);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 校验签名,Java版本
     *
     * @param algorithm     : 算法
     * @param publicKey     : 公钥
     * @param data          : 原文
     * @param signatureData : 签名
     * @return : 签名是否正确
     */
    public static boolean verify(String algorithm, PublicKey publicKey, String data, String signatureData) {
        try {
            // 获取签名对象
            Signature signature = Signature.getInstance(algorithm);
            // 传入公钥
            signature.initVerify(publicKey);
            //传入原文
            signature.update(data.getBytes());
            // 校验签名
            return signature.verify(Base64.decode(signatureData));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 校验签名,JS版本
     *
     * @param algorithm      : 加密算法(SHA256withRSA)
     * @param publicKey      : 公钥
     * @param originalData   : 原文
     * @param signaturedData : 签名
     * @return : 签名是否正确
     */
    public static boolean verifyDataJS(String algorithm, PublicKey publicKey, String originalData, String signaturedData) {
        try {
            // 获取签名对象
            Signature signature = Signature.getInstance(algorithm);
            // 传入公钥
            signature.initVerify(publicKey);
            // 传入原文
            signature.update(originalData.getBytes());
            // 校验数据
            return signature.verify(toBytes(signaturedData));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //转换方法
    public static byte[] toBytes(String str) {
        if (str == null || trim(str) == "") {
            return new byte[0];
        }
        byte[] bytes = new byte[str.length() / 2];
        for (int i = 0; i < str.length() / 2; i++) {
            String subStr = str.substring(i * 2, i * 2 + 2);
            bytes[i] = (byte) Integer.parseInt(subStr, 16);
        }
        return bytes;
    }

    public static String trim(String str) {
        int startIndex = 0;
        int endIndex = str.length() - 1;
        boolean startFound = false;

        while (startIndex <= endIndex) {
            int index;
            if (!startFound) {
                index = startIndex;
            } else {
                index = endIndex;
            }

            char it = str.charAt(index);
            boolean match = it <= ' ';

            if (!startFound) {
                if (!match)
                    startFound = true;
                else
                    startIndex += 1;
            } else {
                if (!match)
                    break;
                else
                    endIndex -= 1;
            }
        }
        return str.substring(startIndex, endIndex + 1);
    }

}
