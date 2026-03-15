package com.user.service.impl;

import com.user.domain.ChatMessage;
import com.user.mapper.ChatMessageMapper;
import com.user.service.ChatMessageService;
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
}
