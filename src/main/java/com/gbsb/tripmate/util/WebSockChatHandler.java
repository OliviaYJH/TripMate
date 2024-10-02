package com.gbsb.tripmate.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gbsb.tripmate.dto.ChatMessage;
import com.gbsb.tripmate.dto.ChatRoom;
import com.gbsb.tripmate.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.security.Principal;

@Slf4j
@RequiredArgsConstructor
@Component
public class WebSockChatHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper;
    private final ChatService chatService;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.info("payload {}", payload);
        ChatMessage chatMessage = objectMapper.readValue(payload, ChatMessage.class);
        ChatRoom room = chatService.findRoomById(chatMessage.getRoomId());
        room.handleActions(session, chatMessage, chatService);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String roomId = getRoomIdFromSession(session);
        String sender = getSenderFromSession(session);

        ChatRoom room = chatService.findRoomById(roomId);
        room.addParticipant(session);

        ChatMessage enterMessage = new ChatMessage();
        enterMessage.setType(ChatMessage.MessageType.ENTER);
        enterMessage.setRoomId(roomId);
        enterMessage.setSender(sender);
        enterMessage.setMessage(sender + "님이 입장했습니다.");

        room.sendMessage(enterMessage, chatService);
    }

    private String getRoomIdFromSession(WebSocketSession session) throws IllegalAccessException {
        String uri = session.getUri().toString();

        UriComponents uriComponents = UriComponentsBuilder.fromUriString(uri).build();
        String roomId = uriComponents.getQueryParams().getFirst("roomId");

        if (roomId == null) {
            throw new IllegalAccessException("roomId가 URI에 존재하지 않습니다.");
        }
        return roomId;
    }

    private String getSenderFromSession(WebSocketSession session) throws IllegalAccessException {
        Principal principal = session.getPrincipal();

        if (principal != null) {
            throw new IllegalAccessException("인증된 사용자 정보가 없습니다.");
        }

        return principal.getName();
    }
}
