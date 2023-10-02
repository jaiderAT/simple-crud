package com.jaideralba.internetbanking.service;

import com.jaideralba.internetbanking.entity.UserEntity;
import com.jaideralba.internetbanking.exception.InvalidUserIdException;
import com.jaideralba.internetbanking.exception.UserNotFoundException;
import com.jaideralba.internetbanking.mapper.UserMapper;
import com.jaideralba.internetbanking.model.UserRequest;
import com.jaideralba.internetbanking.model.UserResponse;
import com.jaideralba.internetbanking.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
public class UsersService implements Users {

    @Autowired
    private UsersRepository repository;

    @Autowired
    private UserMapper mapper;

    @Override
    public UserResponse get(Long id) {
        UserEntity foundUser = repository.findById(id).orElseThrow(UserNotFoundException::new);
        return mapper.toModel(foundUser);
    }

    @Override
    public List<UserResponse> listAll() {
        Iterable<UserEntity> entities = repository.findAll();

        Stream<UserEntity> entityStream = StreamSupport.stream(entities.spliterator(), false);

        return entityStream
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponse create(UserRequest user) {
        clearUserId(user);

        UserEntity entity = mapper.toEntity(user);

        entity.calculateExclusivePlan();

        UserResponse createdUser = mapper.toModel(repository.save(entity));

        return createdUser;
    }

    @Override
    public UserResponse update(UserRequest user) {
        checkUserId(user.getId());

        UserEntity userEntity = mapper.toEntity(user);

        userEntity.calculateExclusivePlan();

        UserResponse updatedUser = mapper.toModel(repository.save(userEntity));

        return updatedUser;
    }

    @Override
    public UserResponse updatePartially(UserRequest userRequest) {
        UserEntity foundUser = repository.findById(userRequest.getId()).orElseThrow(UserNotFoundException::new);

        mapper.partialToEntity(foundUser, userRequest);

        foundUser.calculateExclusivePlan();

        UserResponse updatedUser = mapper.toModel(repository.save(foundUser));

        return updatedUser;
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

    private void clearUserId(UserRequest user) {
        user.setId(null);
    }
}
