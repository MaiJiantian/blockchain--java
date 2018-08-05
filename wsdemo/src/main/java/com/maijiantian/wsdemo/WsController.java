package com.maijiantian.wsdemo;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;

@RestController
public class WsController {
    private MyServer server;

    @PostConstruct
    public void init() {
        server = new MyServer(Integer.parseInt(WsdemoApplication.port) + 1);
        server.startServer();
    }

    // 节点注册
    private HashSet<String> set = new HashSet<>();

    @RequestMapping("/regist")
    public String regist(String node) {
        set.add(node);
        return "添加成功";
    }

    // 连接
    @RequestMapping("/conn")
    public String conn() {
        try {
            for (String s : set) {
                URI uri = new URI("ws://localhost:" + s);
                MyClient client = new MyClient(uri, s);
                client.connect();

            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return "连接成功";
    }
    // 广播

    @RequestMapping("/broadcast")
    public String broadcast(String msg) {
        server.broadcast(msg);
        return "广播成功";
    }

}
