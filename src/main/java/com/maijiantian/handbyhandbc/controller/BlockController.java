package com.maijiantian.handbyhandbc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maijiantian.handbyhandbc.HandbyhandbcApplication;
import com.maijiantian.handbyhandbc.bean.Block;
import com.maijiantian.handbyhandbc.bean.MessageBean;
import com.maijiantian.handbyhandbc.bean.Notebook;
import com.maijiantian.handbyhandbc.bean.Transaction;
import com.maijiantian.handbyhandbc.websocket.MyClient;
import com.maijiantian.handbyhandbc.websocket.MyServer;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;

@RestController
public class BlockController {
    private Notebook notebook = Notebook.getInstance();

    // 添加封面
    @RequestMapping(value = "/addGenesis", method = RequestMethod.POST)
    public String addGenesis(String genesis) {
        try {
            notebook.addGenesis(genesis);
            return "添加封面成功";
        } catch (Exception e) {
            return "添加封面失败:" + e.getMessage();
        }
    }

    // 添加记录
    @RequestMapping(value = "/addNote", method = RequestMethod.POST)
    public String addNote(Transaction transaction) {

        try {
            if (transaction.verify()) {

                ObjectMapper objectMapper = new ObjectMapper();
                String transactionString = objectMapper.writeValueAsString(transaction);
                //广播交易数据
                MessageBean messageBean = new MessageBean(2, transactionString);
                String msg = objectMapper.writeValueAsString(messageBean);

                server.broadcast(msg);
                notebook.addNote(transactionString);
                return "添加记录成功";
            } else {
                throw new RuntimeException("交易数据校验失败");
            }

        } catch (Exception e) {
            return "添加记录失败:" + e.getMessage();
        }
    }

    // 展示记录
    @RequestMapping(value = "/showlist", method = RequestMethod.GET)
    public ArrayList<Block> showlist() {

        return notebook.showlist();
    }

    // 校验数据
    @RequestMapping(value = "/check", method = RequestMethod.GET)
    public String check() {
        String check = notebook.check();

        if (StringUtils.isEmpty(check)) {
            return "数据是安全的";
        }
        return check;
    }

    private MyServer server;

    @PostConstruct
    public void init() {
        server = new MyServer(Integer.parseInt(HandbyhandbcApplication.port) + 1);
        server.startServer();
    }

    // 节点注册
    private HashSet<String> set = new HashSet<>();

    @RequestMapping("/regist")
    public String regist(String node) {
        set.add(node);
        return "添加成功";
    }

    private ArrayList<MyClient> clients = new ArrayList<>();

    // 连接
    @RequestMapping("/conn")
    public String conn() {
        try {
            for (String s : set) {
                URI uri = new URI("ws://localhost:" + s);
                MyClient client = new MyClient(uri, s);
                client.connect();
                clients.add(client);
            }
            return "连接成功";
        } catch (URISyntaxException e) {
            return "连接失败:" + e.getMessage();
        }

    }
    // 广播

    @RequestMapping("/broadcast")
    public String broadcast(String msg) {
        server.broadcast(msg);
        return "广播成功";
    }

    // 请求同步其他节点的区块链数据
    @RequestMapping("/syncData")
    public String syncData() {

        for (MyClient client : clients) {
            client.send("亲,把你的区块链数据给我一份");
        }

        return "广播成功";
    }
}
