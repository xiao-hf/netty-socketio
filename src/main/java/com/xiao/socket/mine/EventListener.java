package com.xiao.socket.mine;

import cn.hutool.json.JSONUtil;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.xiao.common.R;
import com.xiao.domain.User;
import com.xiao.mapper.UserMapper;
import com.xiao.utils.RedisUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class EventListener {
    @Resource
    RedisUtil redisUtil;

    @Resource
    UserMapper userMapper;

    @OnConnect
    public void onConnect(SocketIOClient client) {
        UUID sessionId = client.getSessionId();
        ClientCache.saveClient(sessionId, client, new User());
        redisUtil.set(client.getSessionId().toString(), "", 30, TimeUnit.MINUTES);
        log.info("新连接{}, 连接数:{}", sessionId, ClientCache.getCount());
    }

    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        ClientCache.deleteSessionClient(client.getSessionId());
        log.info("{}断开连接, 连接数:{}", client.getSessionId(), ClientCache.getCount());
    }

    @OnEvent("telegram")
    public R<String> telegram(SocketIOClient client, HashMap<String, Object> map) {
        Integer userId = (Integer) map.get("id");
        String message = (String) map.get("msg");
        UUID sessionId = client.getSessionId();
        if (ClientCache.getClient(sessionId) == null) {
            log.info("{}已断开连接!", sessionId);
            return R.fail(sessionId + "已断开连接!", null);
        }
        User user = userMapper.selectById(userId);
        if (user == null) {
            log.info("用户未登录!");
            return R.fail("用户未登录!", null);
        }
        if (!redisUtil.contains(sessionId.toString())) {
            log.info("用户{}会话已过期!请重新连接!", userId);
            return R.fail("用户 " + userId + " 会话已过期!请重新连接!", null);
        }
        redisUtil.expire(sessionId.toString(), 30, TimeUnit.MINUTES);
        log.info("收到来自{}消息:{}", user.getUsername(), message);
        return R.success(JSONUtil.toJsonStr(message));
    }
}
