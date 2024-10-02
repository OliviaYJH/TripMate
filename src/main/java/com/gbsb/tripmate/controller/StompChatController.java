package com.gbsb.tripmate.controller;

import com.gbsb.tripmate.dto.ChatMessageDTO;
import com.gbsb.tripmate.entity.Meeting;
import com.gbsb.tripmate.entity.User;
import com.gbsb.tripmate.enums.ErrorCode;
import com.gbsb.tripmate.exception.MeetingException;
import com.gbsb.tripmate.repository.MeetingMemberRepository;
import com.gbsb.tripmate.repository.MeetingRepository;
import com.gbsb.tripmate.repository.UserRepository;
import com.gbsb.tripmate.service.ChatService;
import com.gbsb.tripmate.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class StompChatController {

    private final SimpMessagingTemplate template;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final MeetingRepository meetingRepository;
    private final MeetingMemberRepository meetingMemberRepository;
    private final ChatService chatService;

    @MessageMapping(value = "/chat/message")
    public void message(ChatMessageDTO message, @Header("Authorization") String token) {

        String email = jwtUtil.getUsernameFromJWT(token.substring(7));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new MeetingException(ErrorCode.USER_NOT_FOUND));

        message.setWriter(user.getNickname());

        Meeting meeting = meetingRepository.findById(message.getRoomId())
                .orElseThrow(() -> new MeetingException(ErrorCode.MEETING_NOT_FOUND));

        boolean isMember = meetingMemberRepository.existsByMeetingAndUser(meeting, user);

        if (isMember) {
            chatService.addChat(message);
            template.convertAndSend("/sub/chat/room" + message.getRoomId(), message);
        } else {
            throw new MeetingException(ErrorCode.USER_NOT_A_MEMBER_OF_MEETING);
        }
    }
}
