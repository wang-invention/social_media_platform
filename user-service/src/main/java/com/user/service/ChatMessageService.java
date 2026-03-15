package com.user.service;

import com.user.domain.ChatMessage;
import java.util.List;

public interface ChatMessageService {
    List<ChatMessage> getChatMessages(Integer userId, Integer otherUserId);
}
