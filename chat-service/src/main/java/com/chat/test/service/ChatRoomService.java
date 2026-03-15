package com.chat.test.service;

import com.chat.test.domain.ChatMessage;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ChatRoomService {

    private final KafkaAdmin kafkaAdmin;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public ChatRoomService(KafkaAdmin kafkaAdmin, KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaAdmin = kafkaAdmin;
        this.kafkaTemplate = kafkaTemplate;
    }

    // 发送消息 → 自动创建房间（Topic）
    public void sendMessage(ChatMessage msg) {
        String topic = "chat_room_" + msg.getRoomId(); // 房间 = topic
        
        // 自动创建 Topic（房间）
        createTopicIfNotExists(topic);
        
        // 发送到对应房间
        kafkaTemplate.send(topic, msg.getContent());
    }

    // 自动创建 Kafka Topic（自动建房间）
    private void createTopicIfNotExists(String topic) {
        NewTopic newTopic = new NewTopic(topic, 1, (short) 1);
        kafkaAdmin.createOrModifyTopics(newTopic);
    }
}