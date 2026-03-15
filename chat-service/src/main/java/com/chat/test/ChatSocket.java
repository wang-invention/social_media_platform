package com.chat.test;

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/chat")
public class ChatSocket {

    // 房间 -> 所有连接的客户端
    public static final ConcurrentHashMap<String, Set<Session>> ROOM_SESSIONS = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session) {
        String roomId = session.getRequestParameterMap().get("roomId").get(0);
        ROOM_SESSIONS.computeIfAbsent(roomId, k -> ConcurrentHashMap.newKeySet()).add(session);
        System.out.println("用户进入房间：" + roomId);
    }

    // 前端发消息 → 后端收到 → 推送给同房间的人
    @OnMessage
    public void onMessage(String message, Session session) {
        // 从连接中获取房间ID
        String roomId = session.getRequestParameterMap().get("roomId").get(0);
        
        System.out.println("房间" + roomId + " 收到消息：" + message);
        
        // 发给房间内所有人（单聊就是两个人）
        sendToRoom(roomId, message);
    }

    @OnClose
    public void onClose(Session session) {
        String roomId = session.getRequestParameterMap().get("roomId").get(0);
        Set<Session> sessions = ROOM_SESSIONS.get(roomId);
        if (sessions != null) {
            sessions.remove(session);
        }
    }

    // 发送给指定房间
    public static void sendToRoom(String roomId, String msg) {
        Set<Session> sessions = ROOM_SESSIONS.getOrDefault(roomId, ConcurrentHashMap.newKeySet());
        
        for (Session session : sessions) {
            try {
                session.getBasicRemote().sendText(msg);
            } catch (Exception ignored) {}
        }
    }
}