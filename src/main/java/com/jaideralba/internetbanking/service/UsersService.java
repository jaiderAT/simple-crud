package com.jaideralba.internetbanking.service;

import com.jaideralba.internetbanking.entity.UserEntity;
import com.jaideralba.internetbanking.exception.UserNotFoundException;
import com.jaideralba.internetbanking.mapper.UserMapper;
import com.jaideralba.internetbanking.model.User;
import com.jaideralba.internetbanking.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsersService implements Users {

    @Autowired
    private UsersRepository repository;

    @Autowired
    private UserMapper mapper;

    @Override
    public User get(Long id) {
        UserEntity foundUser = repository.findById(id).orElseThrow(UserNotFoundException::new);
        return mapper.toModel(foundUser);
    }

    @Override
    public User listAll() {
        return null;
    }

    @Override
    public User create(User user) {
        UserEntity entity = mapper.toEntity(user);
        User createdUser = mapper.toModel(repository.save(entity));
        return createdUser;
    }

    @Override
    public User update(User user, Long id) {
        return null;
    }

    @Override
    public User updatePartially(User user, Long id) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
