package com.maijiantian.handbyhandbc.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maijiantian.handbyhandbc.bean.Block;
import com.maijiantian.handbyhandbc.bean.MessageBean;
import com.maijiantian.handbyhandbc.bean.Notebook;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.ArrayList;

public class MyServer extends WebSocketServer {

    private int port;

    public MyServer(int port) {
        super(new InetSocketAddress(port));
        this.port = port;
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        System.out.println("webSocket服务器_" + port + "_打开了连接");
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println("webSocket服务器_" + port + "_关闭了连接");
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        System.out.println("webSocket服务器_" + port + "_收到了消息:" + message);
        try {
            if ("亲,把你的区块链数据给我一份".equals(message)) {
                // 获取本地的区块链数据
                Notebook notebook = Notebook.getInstance();
                ArrayList<Block> list = notebook.showlist();
                // 发送给连接到本服务器的所有客户端
                ObjectMapper objectMapper = new ObjectMapper();
                String blockChainData = objectMapper.writeValueAsString(list);

                MessageBean messageBean = new MessageBean(1, blockChainData);
                String msg = objectMapper.writeValueAsString(messageBean);
                // 广播消息
                broadcast(msg);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        System.out.println("webSocket服务器_" + port + "_发生了错误");
    }

    @Override
    public void onStart() {
        System.out.println("webSocket服务器_" + port + "_启动成功");
    }

    public void startServer() {
        new Thread(this).start();
    }
}
