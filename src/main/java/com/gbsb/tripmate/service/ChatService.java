package com.gbsb.tripmate.service;

import com.gbsb.tripmate.dto.BaseResponse;
import com.gbsb.tripmate.dto.ChatMessageDTO;
import com.gbsb.tripmate.entity.Chat;
import com.gbsb.tripmate.entity.ChatRoom;
import com.gbsb.tripmate.entity.User;
import com.gbsb.tripmate.enums.ErrorCode;
import com.gbsb.tripmate.exception.MeetingException;
import com.gbsb.tripmate.repository.ChatRepository;
import com.gbsb.tripmate.repository.ChatRoomRepository;
import com.gbsb.tripmate.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final UserRepository userRepository;

    public void addUserToChat(Long roomId, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new MeetingException(ErrorCode.USER_NOT_FOUND));
        ChatRoom chatRoom = chatRoomRepository.findByRoomId(roomId)
                .orElseThrow(() -> new MeetingException(ErrorCode.CHATROOM_NOT_FOUND));

        chatRoom.addParticipant(user);
        chatRoomRepository.save(chatRoom);
    }

    public void removeUserFromChat(Long roomId, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new MeetingException(ErrorCode.USER_NOT_FOUND));
        ChatRoom chatRoom = chatRoomRepository.findByRoomId(roomId)
                .orElseThrow(() -> new MeetingException(ErrorCode.CHATROOM_NOT_FOUND));

        chatRoom.removeParticipant(user);
        chatRoomRepository.save(chatRoom);
    }

    public void sendSystemMessage (ChatMessageDTO message) {
        messagingTemplate.convertAndSend("/sub/chat/room" + message.getRoomId(), message);
    }

    @Transactional
    public BaseResponse<Chat> addChat(ChatMessageDTO chatMessageDTO) {
        ChatRoom chatRoom = chatRoomRepository.findByRoomId(chatMessageDTO.getRoomId())
                .orElseThrow(() -> new MeetingException(ErrorCode.CHATROOM_NOT_FOUND));

        Chat chater = chatRepository.save(chatMessageDTO.toChat(chatRoom));

        return new BaseResponse<>("채팅이 성공적으로 저장되었습니다.", chater);
    }
}

