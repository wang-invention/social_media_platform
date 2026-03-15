package com.chat.controller;

import com.chat.domain.ChatMessage;
import com.chat.service.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/chat-messages")
public class ChatMessageController {

    @Autowired
    private ChatMessageService chatMessageService;

    @GetMapping
    public List<ChatMessage> getChatMessages(@RequestParam Integer userId, @RequestParam Integer otherUserId) {
        return chatMessageService.getChatMessages(userId, otherUserId);
    }
}
