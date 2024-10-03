package com.gbsb.tripmate.dto;

import java.time.LocalDateTime;

import com.gbsb.tripmate.entity.Chat;
import com.gbsb.tripmate.entity.ChatRoom;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageDTO {

    private Long roomId;
    private Long id;
    private String writer;
    private String message;
    private String createdAt;

    public Chat toChat(ChatRoom chatRoom) {
        return Chat.builder()
                .message(message)
                .writer(writer)
                .chatRoom(chatRoom)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
