package com.maijiantian.handbyhandbc.bean;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maijiantian.handbyhandbc.utils.HashUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Notebook {
    // 保存数据的集合
    private ArrayList<Block> list = new ArrayList<>();

    private Notebook() {
        init();
    }

    public static volatile Notebook instance;

    public static Notebook getInstance() {
        if (instance == null) {
            synchronized (Notebook.class) {
                if (instance == null) {
                    instance = new Notebook();
                }
            }
        }
        return instance;
    }

    private void init() {
        try {
            File file = new File("a.json");
            // 如果文件存在,读取文件的内容并赋值给list
            if (file.exists() && file.length() > 0) {
                ObjectMapper objectMapper = new ObjectMapper();

                JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, Block.class);
                list = objectMapper.readValue(file, javaType);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 添加封面, 创世区块
    // 账本是一个新的账本
    public void addGenesis(String genesis) {
        if (list.size() > 0) {
            throw new RuntimeException("添加封面的时候,必须是新账本");
        }
        String preHash = "0000000000000000000000000000000000000000000000000000000000000000";
        int nonce = mine(genesis + preHash);
        // 添加数据
        list.add(new Block(
                list.size() + 1,//ID
                genesis,//内容
                HashUtils.sha256(nonce + genesis + preHash),// Hash
                nonce,// 工作量
                preHash
        ));
        // 保存到本地
        save2Disk();
    }

    //挖矿的过程就是求取一个符合特定规则的hash值
    private static int mine(String content) {

        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            String hash = HashUtils.sha256(i + content);
            if (hash.startsWith("0000")) {
                return i;
            }
        }

        throw new RuntimeException("挖矿失败");
    }

    // 添加交易记录,普通区块
    // 至少已经有了封面
    public void addNote(String note) {

        if (list.size() < 1) {
            throw new RuntimeException("添加记录的时候,必须是有封面的");
        }

        Block block = list.get(list.size() - 1);
        String preHash = block.hash;
        int nonce = mine(note + preHash);
        // 添加数据
        list.add(new Block(
                list.size() + 1,//ID
                note,//内容
                HashUtils.sha256(nonce + note + preHash),// Hash
                nonce,// 工作量证明
                preHash
        ));
        // 保存到本地
        save2Disk();
    }

    // 展示交易记录
    public ArrayList<Block> showlist() {
        return list;
    }

    // 保存数据
    // xml ,json
    // fastjson jackson gson
    public void save2Disk() {
        try {
            // jackson序列化对象的方法
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(new File("a.json"), list);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String check() {

        StringBuilder sb = new StringBuilder();

        // 校验hash,还要校验prehash

        for (int i = 0; i < list.size(); i++) {

            Block block = list.get(i);
            String hash = block.hash;
            String content = block.content;
            int id = block.id;
            int nonce = block.nonce;
            String preHash = block.preHash;

            if (i == 0) {
                String caculatedHash = HashUtils.sha256(nonce + content + preHash);
                if (!caculatedHash.equals(hash)) {
                    sb.append("编号为" + block.id + "的hash有问题,请注意检查<br>");
                }
            } else {
                // 校验hash,还要校验prehash

                String caculatedHash = HashUtils.sha256(nonce + content + preHash);
                if (!caculatedHash.equals(hash)) {
                    sb.append("编号为" + block.id + "的hash有问题,请注意检查<br>");
                }

                Block preBlock = list.get(i - 1);
                String preBlockHash = preBlock.hash;

                if (!preBlockHash.equals(preHash)) {
                    sb.append("编号为" + block.id + "的prehash有问题,请注意检查<br>");
                }
            }
        }

        return sb.toString();
    }

    public static void main(String[] args) {

        String content = "张三给王武转账100";
        String preHash = "0000929fb3355254e89fc612367d1f48348c1b9446184933362f9e2db2665168";
        int mine = mine(content + preHash);
        String s = HashUtils.sha256(mine + content + preHash);
        System.out.println(s);
    }

    // 和本地的区块链进行比较,如果对方的数据比较新,就用对方的数据替换本地的数据
    public void compareData(ArrayList<Block> newList) {
        // 比较长度, 校验
        if (newList.size() > list.size()) {
            list = newList;
            save2Disk();
        }
    }
}
