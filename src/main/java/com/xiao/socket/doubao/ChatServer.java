package com.xiao.socket.doubao;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import com.xiao.domain.ChatMessage;

import java.util.*;

public class ChatServer {
    private static final int PORT = 9092;
    private static final Map<String, List<ChatMessage>> groupMessages = new HashMap<>();
    private static final Map<SocketIOClient, String> clientUserIds = new HashMap<>();

    public static void main(String[] args) {
        Configuration config = new Configuration();
        config.setPort(PORT);

        SocketIOServer server = new SocketIOServer(config);

        // 处理用户连接事件
        server.addConnectListener(client -> {
            System.out.println("Client connected: " + client.getSessionId());
        });

        // 处理用户断开连接事件
        server.addDisconnectListener(client -> {
            System.out.println("Client disconnected: " + client.getSessionId());
            clientUserIds.remove(client);
        });

        // 处理用户发送消息事件
        server.addEventListener("sendMessage", String[].class, new DataListener<String[]>() {
            @Override
            public void onData(SocketIOClient client, String[] data, AckRequest ackSender) {
                String groupId = data[0];
                String message = data[1];
                String userId = clientUserIds.get(client);

                if (userId != null) {
                    ChatMessage chatMessage = new ChatMessage(userId, message);
                    groupMessages.computeIfAbsent(groupId, k -> new ArrayList<>()).add(chatMessage);

                    // 广播消息给群组内的所有用户
                    server.getRoomOperations(groupId).sendEvent("newMessage", chatMessage);
                }
            }
        });

        // 处理用户设置用户 ID 事件
        server.addEventListener("setUserId", String.class, new DataListener<String>() {
            @Override
            public void onData(SocketIOClient client, String userId, AckRequest ackSender) {
                clientUserIds.put(client, userId);
                System.out.println("User " + userId + " connected.");
            }
        });

        // 处理用户拉取群组消息记录事件
        server.addEventListener("getGroupMessages", String.class, new DataListener<String>() {
            @Override
            public void onData(SocketIOClient client, String groupId, AckRequest ackSender) {
                List<ChatMessage> messages = groupMessages.getOrDefault(groupId, new ArrayList<>());
                messages.sort((m1, m2) -> m2.getSendTime().compareTo(m1.getSendTime()));
                ackSender.sendAckData(messages);
            }
        });

        server.start();
        System.out.println("Chat server started on port " + PORT);
    }
}
