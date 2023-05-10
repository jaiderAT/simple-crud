package com.jaideralba.internetbanking.service;

import com.jaideralba.internetbanking.entity.UserEntity;
import com.jaideralba.internetbanking.exception.InvalidUserIdException;
import com.jaideralba.internetbanking.exception.UserNotFoundException;
import com.jaideralba.internetbanking.mapper.UserMapper;
import com.jaideralba.internetbanking.model.User;
import com.jaideralba.internetbanking.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

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
        clearUserId(user);
        UserEntity entity = mapper.toEntity(user);
        User createdUser = mapper.toModel(repository.save(entity));
        return createdUser;
    }

    @Override
    public User update(User user) {
        checkUserId(user.getId());

        UserEntity userEntity = mapper.toEntity(user);
        User updatedUser = mapper.toModel(repository.save(userEntity));
        return updatedUser;
    }

    @Override
    public User updatePartially(User user) {
        return null;
    }

    @Override
    public void delete(Long id) {
        checkUserId(id);
        repository.deleteById(id);
    }

    private void checkUserId(Long id) {
        if (!isFilled(id)) {
            throw new InvalidUserIdException();
        }

        if (repository.findById(id).isEmpty()) {
            throw new UserNotFoundException("Usuário não encontrado na base de dados");
        }
    }

    private boolean isFilled(Long id) {
        return Objects.nonNull(id) && id > 0;
    }

    private void clearUserId(User user) {
        user.setId(null);
    }
}
