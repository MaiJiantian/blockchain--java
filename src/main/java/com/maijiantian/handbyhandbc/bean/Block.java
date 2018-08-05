package com.maijiantian.handbyhandbc.bean;

public class Block {
    public int id;
    public String content;
    public String hash;
    public int nonce;// 工作量证明
    public String preHash;//上一个区块的hash

    public Block() {
    }

    public Block(int id, String content, String hash, int nonce, String preHash) {
        this.id = id;
        this.content = content;
        this.hash = hash;
        this.nonce = nonce;
        this.preHash = preHash;
    }
}
