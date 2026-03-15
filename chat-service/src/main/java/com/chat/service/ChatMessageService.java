package com.chat.service;

import com.chat.domain.ChatMessage;
import java.util.List;

public interface ChatMessageService {
    List<ChatMessage> getChatMessages(Integer userId, Integer otherUserId);
    void saveChatMessage(ChatMessage chatMessage);
}
