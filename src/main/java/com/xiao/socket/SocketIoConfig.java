package com.xiao.socket;



import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketConfig;
import com.corundumstudio.socketio.SocketIOServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author: byh
 * @create: 2023/7/7
 * @Description:
 * @ClassName: SocketIoConfig
 */
@Component
@Slf4j
public class SocketIoConfig implements InitializingBean {

    @Autowired
    private EventListener eventListener;


    @Autowired
    private ExceptionListener exceptionListener1;

    @Value("${socketio.port}")
    private int serverPort;

    @Value("${socketio.host}")
    private String serverHost;

    @Value("${socketio.bossCount}")
    private int bossCount;

    @Value("${socketio.workCount}")
    private int workCount;

    @Value("${socketio.allowCustomRequests}")
    private boolean allowCustomRequests;

    @Value("${socketio.upgradeTimeout}")
    private int upgradeTimeout;

    @Value("${socketio.pingTimeout}")
    private int pingTimeout;

    @Value("${socketio.pingInterval}")
    private int pingInterval;

    @Override
    public void afterPropertiesSet() throws Exception {
        Configuration config = new Configuration();
        config.setExceptionListener(exceptionListener1);
        config.setPort(serverPort);
        config.setHostname(serverHost);
        config.setOrigin("*");
        config.setBossThreads(bossCount);
        config.setWorkerThreads(workCount);
        config.setAllowCustomRequests(allowCustomRequests);
        config.setUpgradeTimeout(upgradeTimeout);
        config.setPingTimeout(pingTimeout);
        config.setPingInterval(pingInterval);
        SocketConfig socketConfig = new SocketConfig();
        socketConfig.setReuseAddress(true);
        socketConfig.setTcpNoDelay(true);
        socketConfig.setSoLinger(0);

        config.setSocketConfig(socketConfig);

        config.setAllowHeaders("*");
        SocketIOServer server = new SocketIOServer(config);
        server.addListeners(eventListener);
        server.start();
        log.info("启动正常");
    }
}
