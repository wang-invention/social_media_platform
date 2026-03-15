package com.chat.test;

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/ws")
public class WebRTCSignalingSocket {

    // 关键：用户ID -> 连接
    public static Map<String, Session> USER_MAP = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session) {
        String uid = session.getRequestParameterMap().get("uid").get(0);
        USER_MAP.put(uid, session);
        System.out.println("用户上线：" + uid);
    }

    // 消息转发：呼叫、挂断、offer、answer、冰结 全都走这里
    @OnMessage
    public void onMessage(String message, Session session) {
        try {
            String toUid = session.getRequestParameterMap().get("to").get(0);
            Session toSession = USER_MAP.get(toUid);
            if (toSession != null) {
                toSession.getBasicRemote().sendText(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClose
    public void onClose(Session session) {
        String uid = session.getRequestParameterMap().get("uid").get(0);
        USER_MAP.remove(uid);
    }
}