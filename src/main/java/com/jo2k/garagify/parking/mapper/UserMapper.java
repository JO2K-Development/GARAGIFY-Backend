package com.jo2k.garagify.parking.mapper;

import com.jo2k.dto.UserDTO;
import com.jo2k.dto.UserListDTO;
import com.jo2k.garagify.user.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(source = "id", target = "userId")
    @Mapping(source = "email", target = "email")
    UserDTO toDTO(User user);

    default UserListDTO toUserListDTO(List<User> users) {
        UserListDTO dto = new UserListDTO();
        dto.setUsers(users.stream().map(this::toDTO).toList());
        return dto;
    }
}