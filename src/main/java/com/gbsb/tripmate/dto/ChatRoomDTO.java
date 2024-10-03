package com.gbsb.tripmate.dto;

import com.gbsb.tripmate.entity.ChatRoom;
import com.gbsb.tripmate.entity.User;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomDTO {

    private Long roomId;
    private String name;
    private Long meetingId;
}
