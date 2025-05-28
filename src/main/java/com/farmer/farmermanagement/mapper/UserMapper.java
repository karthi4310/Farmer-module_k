package com.farmer.farmermanagement.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.farmer.farmermanagement.dto.UserDTO;
import com.farmer.farmermanagement.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toDto(User user);

    @Mapping(target = "id", ignore = true)
    User toEntity(UserDTO userDto);

}
