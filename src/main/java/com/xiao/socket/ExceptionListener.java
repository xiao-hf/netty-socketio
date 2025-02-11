package com.xiao.socket;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DefaultExceptionListener;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class ExceptionListener extends DefaultExceptionListener implements com.corundumstudio.socketio.listener.ExceptionListener {
    @Override
    public void onEventException(Exception e, List<Object> list, SocketIOClient socketIOClient) {
        log.info("onEventException");
        super.onEventException(e, list, socketIOClient);
    }

    @Override
    public void onDisconnectException(Exception e, SocketIOClient socketIOClient) {
        log.info("onDisconnectException");
        super.onDisconnectException(e, socketIOClient);
    }

    @Override
    public void onConnectException(Exception e, SocketIOClient socketIOClient) {
        log.info("onConnectException");
        super.onConnectException(e, socketIOClient);
    }

    @Override
    public void onPingException(Exception e, SocketIOClient socketIOClient) {
        log.info("onPingException");
        super.onPingException(e, socketIOClient);

    }

    @Override
    public void onPongException(Exception e, SocketIOClient socketIOClient) {
        log.info("onPongException");
        super.onPongException(e, socketIOClient);
    }

    @Override
    public boolean exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable throwable) throws Exception {
      return super.exceptionCaught(channelHandlerContext, throwable);
    }
}
