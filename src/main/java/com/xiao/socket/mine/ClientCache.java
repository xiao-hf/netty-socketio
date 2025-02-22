package com.xiao.socket.mine;

import com.corundumstudio.socketio.SocketIOClient;
import com.xiao.domain.User;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ClientCache { // 缓存
    private static final Map<UUID, User> userMap = new ConcurrentHashMap<>();
    private static final Map<UUID, SocketIOClient> clientMap = new ConcurrentHashMap<>();
    public static void saveClient(UUID sessionId, SocketIOClient socketIOClient, User user) {
        userMap.put(sessionId, user);
        clientMap.put(sessionId, socketIOClient);
    }
    public static SocketIOClient getClient(UUID uuid) { // 获取客户端连接
        return clientMap.get(uuid);
    }
    public static void deleteSessionClient(UUID sessionId) { // 删除客户端连接
        clientMap.remove(sessionId);
        userMap.remove(sessionId);
    }
    public static int getCount() { // 获取连接数
        return clientMap.size();
    }
    public static Map<UUID, User> getUserMap() {
        return userMap;
    }
    // 推送消息
    public static void sendMapToAll(Map<String, Object> map, String ... events) {
        userMap.forEach((uuid, user1) -> {
            for (String event : events)
                clientMap.get(uuid).sendEvent(event, map);
        });
    }
    // 推送异常消息
    public static void sendException(Object exception, Set<UUID> uuids, String ... events) {
        uuids.forEach(uuid -> {
            if (clientMap.containsKey(uuid)) {
                for (String event : events) {
                    clientMap.get(uuid).sendEvent(event, exception);
                }
            }
        });
    }
}
