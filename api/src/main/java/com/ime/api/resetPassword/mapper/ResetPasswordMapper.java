package com.ime.api.resetPassword.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;


import com.ime.api.resetPassword.dto.ResetPasswordRequest;
import com.ime.api.resetPassword.model.ResetPassword;
import com.ime.api.user.model.User;

@Mapper(componentModel = "spring")
public abstract class ResetPasswordMapper {

    @AfterMapping
    protected void setUser(ResetPasswordRequest request, User user, @MappingTarget ResetPassword token) {
        token.setUser(user);
    }
}
