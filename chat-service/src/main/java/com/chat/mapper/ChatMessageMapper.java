package com.chat.mapper;

import com.chat.domain.ChatMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ChatMessageMapper {
    List<ChatMessage> selectChatMessages(@Param("userId") Integer userId, @Param("otherUserId") Integer otherUserId);
    void insertChatMessage(ChatMessage chatMessage);
}
