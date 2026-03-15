package com.chat.domain;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ChatMessage {
    private Integer id;
    private Integer senderId;
    private Integer receiverId;
    private String content;
    private Integer messageType;
    private Integer isRead;
    private LocalDateTime createdAt;
}
