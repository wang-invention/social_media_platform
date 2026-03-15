package com.chat.test;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
public class ChatRoomConsumer {

    // 自动监听所有 chat_room_ 开头的房间
    @KafkaListener(topicPattern = "chat_room_.*", groupId = "chat_group_all")
    public void listen(String message, @Header("kafka_receivedTopic") String topic) {
        System.out.println("收到消息：" + topic + " → " + message);
        
        // 推送给对应房间的前端
        ChatSocket.sendToRoom(topic, message);
    }
}