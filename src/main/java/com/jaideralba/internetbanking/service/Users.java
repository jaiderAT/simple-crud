package com.jaideralba.internetbanking.service;

import com.jaideralba.internetbanking.model.UserRequest;
import com.jaideralba.internetbanking.model.UserResponse;

import java.util.List;

public interface Users {
    UserRequest get(Long id);

    List<UserResponse> listAll();

    UserRequest create(UserRequest user);

    UserRequest update(UserRequest user);

    UserRequest updatePartially(UserRequest user);

    void delete(Long id);

}
