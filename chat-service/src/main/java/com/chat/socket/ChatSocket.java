package com.chat.socket;

import com.chat.domain.ChatMessage;
import com.chat.service.ChatMessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/chat/{userId}")
public class ChatSocket {

    private static ChatMessageService chatMessageService;

    @Autowired
    public void setChatMessageService(ChatMessageService service) {
        ChatSocket.chatMessageService = service;
    }

    private static final ConcurrentHashMap<Integer, Set<Session>> USER_SESSIONS = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("userId") Integer userId) {
        USER_SESSIONS.computeIfAbsent(userId, k -> ConcurrentHashMap.newKeySet()).add(session);
        System.out.println("用户 " + userId + " 已连接");
    }

    @OnMessage
    public void onMessage(String message, Session session, @PathParam("userId") Integer userId) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ChatMessage chatMessage = objectMapper.readValue(message, ChatMessage.class);
            
            chatMessage.setSenderId(userId);
            chatMessage.setMessageType(1);
            chatMessage.setIsRead(0);
            
            chatMessageService.saveChatMessage(chatMessage);
            
            Integer receiverId = chatMessage.getReceiverId();
            sendToUser(receiverId, message);
            
            System.out.println("用户 " + userId + " 发送给用户 " + receiverId + " 的消息：" + chatMessage.getContent());
        } catch (Exception e) {
            System.err.println("处理消息失败: " + e.getMessage());
        }
    }

    @OnClose
    public void onClose(Session session, @PathParam("userId") Integer userId) {
        Set<Session> sessions = USER_SESSIONS.get(userId);
        if (sessions != null) {
            sessions.remove(session);
            if (sessions.isEmpty()) {
                USER_SESSIONS.remove(userId);
            }
        }
        System.out.println("用户 " + userId + " 已断开连接");
    }

    @OnError
    public void onError(Session session, Throwable error) {
        System.err.println("WebSocket错误: " + error.getMessage());
    }

    public static void sendToUser(Integer userId, String message) {
        Set<Session> sessions = USER_SESSIONS.getOrDefault(userId, ConcurrentHashMap.newKeySet());
        
        for (Session session : sessions) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                System.err.println("发送消息失败: " + e.getMessage());
            }
        }
    }
}
