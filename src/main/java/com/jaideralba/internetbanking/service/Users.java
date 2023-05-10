package com.jaideralba.internetbanking.service;

import com.jaideralba.internetbanking.model.User;

public interface Users {
    User get(Long id);

    User listAll();

    User create(User user);

    User update(User user, Long id);

    User updatePartially(User user, Long id);

    void delete(Long id);

}
