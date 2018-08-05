package com.maijiantian.wsdemo;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;

public class MyServer extends WebSocketServer {

    private int port;

    public MyServer(int port) {
        super(new InetSocketAddress(port));
        this.port = port;
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        System.out.println("webSocket服务器_"+port+"_打开了连接");
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println("webSocket服务器_"+port+"_关闭了连接");
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        System.out.println("webSocket服务器_"+port+"_收到了消息:" + message);
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        System.out.println("webSocket服务器_"+port+"_发生了错误");
    }

    @Override
    public void onStart() {
        System.out.println("webSocket服务器_"+port+"_启动成功");
    }

    public void startServer() {
        new Thread(this).start();
    }
}
