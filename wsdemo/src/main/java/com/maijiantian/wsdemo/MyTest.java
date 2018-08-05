package com.maijiantian.wsdemo;

import java.net.URI;

public class MyTest {
    public static void main(String[] args) throws Exception {
        MyServer server = new MyServer(7000);
        server.startServer();

        URI uri = new URI("ws://localhost:7000");
        MyClient client1 = new MyClient(uri, "1111");
        MyClient client2 = new MyClient(uri, "2222");

        client1.connect();
        client2.connect();

        Thread.sleep(1000);
        //server.broadcast("这是来自服务器的消息");

        client1.send("这是客户端1发出来的消息");
    }
}
