package com.chat.service.impl;

import com.chat.domain.ChatMessage;
import com.chat.mapper.ChatMessageMapper;
import com.chat.service.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ChatMessageServiceImpl implements ChatMessageService {

    @Autowired
    private ChatMessageMapper chatMessageMapper;

    @Override
    public List<ChatMessage> getChatMessages(Integer userId, Integer otherUserId) {
        return chatMessageMapper.selectChatMessages(userId, otherUserId);
    }

    @Override
    public void saveChatMessage(ChatMessage chatMessage) {
        chatMessageMapper.insertChatMessage(chatMessage);
    }
}
