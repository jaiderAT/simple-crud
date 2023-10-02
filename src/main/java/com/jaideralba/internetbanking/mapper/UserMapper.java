package com.jaideralba.internetbanking.mapper;

import com.jaideralba.internetbanking.entity.UserEntity;
import com.jaideralba.internetbanking.model.UserRequest;
import com.jaideralba.internetbanking.model.UserResponse;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel="spring")
public interface UserMapper {

    UserEntity toEntity(UserRequest user);
    UserResponse toModel(UserEntity user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialToEntity(@MappingTarget UserEntity userEntity, UserRequest userRequest);
}
