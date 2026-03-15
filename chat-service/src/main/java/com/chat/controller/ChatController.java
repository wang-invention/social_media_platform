package com.chat.controller;

import com.chat.domain.ChatMessage;
import com.chat.service.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private ChatMessageService chatMessageService;

    @PostMapping("/send")
    public ChatMessage sendMessage(@RequestBody ChatMessage chatMessage) {
        chatMessage.setMessageType(1);
        chatMessage.setIsRead(0);
        chatMessageService.saveChatMessage(chatMessage);
        return chatMessage;
    }
}
