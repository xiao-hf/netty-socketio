package com.xiao.domain;

import lombok.Data;

import java.util.Date;

@Data
public class ChatMessage {
    String userId;
    Object message;
    Date sendTime;

    public ChatMessage(String userId, Object message) {
        this.userId = userId;
        this.message = message;
    }
}
