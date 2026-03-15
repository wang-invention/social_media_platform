package com.chat.test.domain;

import lombok.Data;

@Data
public class ChatMessage {
    private String roomId;   // 房间ID（自动创建房间的关键）
    private String fromUid;  // 谁发的
    private String toUid;    // 发给谁（单聊用）
    private String content;  // 消息内容
}