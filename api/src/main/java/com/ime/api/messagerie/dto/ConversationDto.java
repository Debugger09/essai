package com.ime.api.messagerie.dto;

import java.util.Date;
import java.util.List;

import com.ime.api.user.dto.UserDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConversationDto {

    private Long id;
    private Long userAId;
    private Long userBId;
    private UserDTO userA;
    private UserDTO userB;
    private List<MessageDto> messages;
    private Date createdAt;
    private Date updatedAt;
}
