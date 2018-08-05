package com.maijiantian.handbyhandbc.bean;

import com.maijiantian.handbyhandbc.utils.RSAUtils;

import java.security.PublicKey;

public class Transaction {

    // 付款方公钥
    public String senderPublicKey;
    // 收款方公钥
    public String receiverPublicKey;
    // 金额
    public String content;
    // 签名
    public String signaturedData;

    public Transaction() {
    }

    public Transaction(String senderPublicKey, String receiverPublicKey, String content, String signaturedData) {
        this.senderPublicKey = senderPublicKey;
        this.receiverPublicKey = receiverPublicKey;
        this.content = content;
        this.signaturedData = signaturedData;
    }

    public String getSenderPublicKey() {
        return senderPublicKey;
    }

    public void setSenderPublicKey(String senderPublicKey) {
        this.senderPublicKey = senderPublicKey;
    }

    public String getReceiverPublicKey() {
        return receiverPublicKey;
    }

    public void setReceiverPublicKey(String receiverPublicKey) {
        this.receiverPublicKey = receiverPublicKey;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSignaturedData() {
        return signaturedData;
    }

    public void setSignaturedData(String signaturedData) {
        this.signaturedData = signaturedData;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "senderPublicKey='" + senderPublicKey + '\'' +
                ", receiverPublicKey='" + receiverPublicKey + '\'' +
                ", content='" + content + '\'' +
                ", signaturedData='" + signaturedData + '\'' +
                '}';
    }

    // 校验交易的方法
    public boolean verify() {
        PublicKey publicKey = RSAUtils.getPublicKeyFromString("RSA", senderPublicKey);
        return RSAUtils.verifyDataJS("SHA256withRSA", publicKey, content, signaturedData);
    }
}
