package com.jaideralba.internetbanking.mapper;

import com.jaideralba.internetbanking.entity.UserEntity;
import com.jaideralba.internetbanking.model.User;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.Mapping;

@Mapper(componentModel="spring")
public interface UserMapper {

    UserEntity toEntity(User user);
    User toModel(UserEntity user);
}
