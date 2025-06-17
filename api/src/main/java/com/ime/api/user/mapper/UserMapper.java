package com.ime.api.user.mapper;

import com.ime.api.user.dto.UserDTO;
import com.ime.api.user.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toDto(User user);
    User toEntity(UserDTO userDTO);
}
