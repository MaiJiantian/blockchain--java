package com.maijiantian.handbyhandbc.websocket;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maijiantian.handbyhandbc.bean.Block;
import com.maijiantian.handbyhandbc.bean.MessageBean;
import com.maijiantian.handbyhandbc.bean.Notebook;
import com.maijiantian.handbyhandbc.bean.Transaction;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;

public class MyClient extends WebSocketClient {
    private String name;

    public MyClient(URI serverUri, String name) {
        super(serverUri);
        this.name = name;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("客户端__" + name + "__打开了连接");
    }

    @Override
    public void onMessage(String message) {
        System.out.println("客户端__" + name + "__收到了消息:" + message);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            MessageBean messageBean = objectMapper.readValue(message, MessageBean.class);
            Notebook notebook = Notebook.getInstance();
            // 判断消息类型
            if (messageBean.type == 1) {
                // 收到的是区块链数据
                JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, Block.class);
                ArrayList<Block> newList = objectMapper.readValue(messageBean.msg, javaType);
                // 和本地的区块链进行比较,如果对方的数据比较新,就用对方的数据替换本地的数据

                notebook.compareData(newList);

            } else if (messageBean.type == 2) {

                Transaction transaction = objectMapper.readValue(messageBean.msg, Transaction.class);
                if (transaction.verify()) {
                    notebook.addNote(messageBean.msg);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("客户端__" + name + "__关闭了连接");
    }

    @Override
    public void onError(Exception ex) {
        System.out.println("客户端__" + name + "__发生错误");
    }
}
