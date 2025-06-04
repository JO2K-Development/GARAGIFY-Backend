package com.jo2k.garagify.parking.mapper;

import com.jo2k.dto.UserDTO;
import com.jo2k.garagify.user.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(source = "id", target = "userId")
    @Mapping(source = "email", target = "email")
    UserDTO toDTO(User user);
}