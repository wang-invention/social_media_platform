package com.chat.service.impl;

import com.chat.domain.ChatMessage;
import com.chat.mapper.ChatMessageMapper;
import com.chat.service.ChatMessageService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatMessageServiceImpl implements ChatMessageService {

    @Autowired
    private ChatMessageMapper chatMessageMapper;

    // 注入你定义的线程池
    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Override
    public List<ChatMessage> getChatMessages(Integer userId, Integer otherUserId) {
        return chatMessageMapper.selectChatMessages(userId, otherUserId);
    }

    @Override
    public void saveChatMessage(ChatMessage chatMessage) {
        // 提交给线程池异步执行
        threadPoolTaskExecutor.execute(() -> {
            try {
                // 写入 TDengine
                chatMessageMapper.insertChatMessage(chatMessage);
                System.out.println("数据库写入聊天记录成功：" + chatMessage.getContent());
            } catch (Exception e) {
                e.printStackTrace();
                // 失败可以做重试 / 日志 / 存入MQ
            }
        });
    }
}
