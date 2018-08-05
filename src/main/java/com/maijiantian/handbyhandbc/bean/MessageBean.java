package com.maijiantian.handbyhandbc.bean;

public class MessageBean {
    // 1 区块链数据
    // 2 交易数据
    public int type;// 消息的类型
    public String msg;// 消息的具体内容

    public MessageBean() {
    }

    public MessageBean(int type, String msg) {
        this.type = type;
        this.msg = msg;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
