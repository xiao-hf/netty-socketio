//package com.xiao.socket.deepseek;
//
//import com.corundumstudio.socketio.*;
//import com.corundumstudio.socketio.listener.*;
//import java.util.*;
//import java.util.concurrent.*;
//
//public class ChatServer {
//
//    // 存储群组消息（线程安全）
//    private static final Map<String, PriorityQueue<ChatMessage>> groupMessages = new ConcurrentHashMap<>();
//
//    // 存储用户ID与Socket的映射
//    private static final Map<String, SocketIOClient> userClients = new ConcurrentHashMap<>();
//
//    public static void main(String[] args) {
//        Configuration config = new Configuration();
//        config.setHostname("localhost");
//        config.setPort(9090);
//
//        final SocketIOServer server = new SocketIOServer(config);
//
//        // 处理用户登录
//        server.addEventListener("login", String.class, (client, userId, ackSender) -> {
//            client.set("userId", userId);
//            userClients.put(userId, client);
//            System.out.println("User logged in: " + userId);
//        });
//
//        // 处理加入群组
//        server.addEventListener("join_group", String.class, (client, groupId, ackSender) -> {
//            client.joinRoom(groupId);
//
//            // 发送历史消息（倒序）
//            if (groupMessages.containsKey(groupId)) {
//                List<ChatMessage> reversed = new ArrayList<>(groupMessages.get(groupId));
//                Collections.reverse(reversed);
//                client.sendEvent("history", reversed);
//            }
//        });
//
//        // 处理消息发送
//        server.addEventListener("chat_message", ChatMessage.class, (client, message, ackSender) -> {
//            String userId = client.get("userId");
//            message.setSender(userId);
//            message.setTimestamp(System.currentTimeMillis());
//
//            // 存储消息（自动排序）
//            groupMessages
//                    .computeIfAbsent(message.getGroupId(), k ->
//                            new PriorityQueue<>((m1, m2) -> Long.compare(m2.getTimestamp(), m1.getTimestamp())))
//                    .add(message);
//
//            // 广播到群组
//            server.getRoomOperations(message.getGroupId())
//                    .sendEvent("new_message", message);
//        });
//
//        // 连接监听
//        server.addConnectListener(client -> {
//            System.out.println("Client connected: " + client.getSessionId());
//        });
//
//        // 断开连接监听
//        server.addDisconnectListener(client -> {
//            String userId = client.get("userId");
//            userClients.remove(userId);
//            System.out.println("Client disconnected: " + userId);
//        });
//
//        server.start();
//        System.out.println("Chat server started");
//    }
//
//    static class ChatMessage implements Comparable<ChatMessage> {
//        private String groupId;
//        private String content;
//        private String sender;
//        private long timestamp;
//
//        // getters/setters 和 compareTo 方法
//        @Override
//        public int compareTo(ChatMessage o) {
//            return Long.compare(o.timestamp, this.timestamp); // 倒序排列
//        }
//    }
//}
