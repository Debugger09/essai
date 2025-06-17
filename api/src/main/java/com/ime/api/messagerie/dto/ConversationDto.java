package com.ime.api.messagerie.dto;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConversationDto {

    private Long id;
    private Long userAId;
    private Long userBId;
    private List<MessageDto> messages;
    private Date createdAt;
    private Date updatedAt;
}
