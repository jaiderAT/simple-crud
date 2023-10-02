package com.jaideralba.internetbanking.service;

import com.jaideralba.internetbanking.entity.UserEntity;
import com.jaideralba.internetbanking.exception.DuplicatedAccountException;
import com.jaideralba.internetbanking.exception.InvalidUserIdException;
import com.jaideralba.internetbanking.exception.UserNotFoundException;
import com.jaideralba.internetbanking.mapper.UserMapper;
import com.jaideralba.internetbanking.model.UserRequest;
import com.jaideralba.internetbanking.model.UserResponse;
import com.jaideralba.internetbanking.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

    @Value("${business.exclusivePlanBalanceTrashold}")
    private BigDecimal EXCLUSIVE_PLAN_BALANCE_TRASHOLD;

    @Override
    public UserResponse get(Long id) {
        UserEntity foundUser = repository.findById(id).orElseThrow(UserNotFoundException::new);
        return mapper.toModel(foundUser);
    }

    @Override
    public List<UserResponse> listAll() {
        Iterable<UserEntity> foundUsers = repository.findAll();

        Stream<UserEntity> foundUsersStream = StreamSupport.stream(foundUsers.spliterator(), false);

        return foundUsersStream
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponse create(UserRequest user) {
        clearUserId(user);

        UserEntity userEntity = mapper.toEntity(user);

        userEntity.setExclusivePlan(isUserEligibleForExclusivePlan(userEntity));

        UserEntity createdUser = saveUserEntity(userEntity);

        return mapper.toModel(createdUser);
    }

    @Override
    public UserResponse update(UserRequest user) {
        checkUserId(user.getId());

        UserEntity userEntity = mapper.toEntity(user);

        userEntity.setExclusivePlan(isUserEligibleForExclusivePlan(userEntity));

        UserEntity updatedUser = saveUserEntity(userEntity);

        return mapper.toModel(updatedUser);
    }

    @Override
    public UserResponse updatePartially(UserRequest userRequest) {
        UserEntity foundUser = repository.findById(userRequest.getId()).orElseThrow(UserNotFoundException::new);

        mapper.partialToEntity(foundUser, userRequest);

        foundUser.setExclusivePlan(isUserEligibleForExclusivePlan(foundUser));

        UserEntity updatedUser = saveUserEntity(foundUser);

        return mapper.toModel(updatedUser);
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

    public boolean isUserEligibleForExclusivePlan(UserEntity user){
        return user.getBalance().compareTo(EXCLUSIVE_PLAN_BALANCE_TRASHOLD) >= 0;
    }

    private UserEntity saveUserEntity(UserEntity userEntity) {
        try {
            return repository.save(userEntity);
        }
        catch (DataIntegrityViolationException e){
            if(isUniqueConstraintViolation(e)){
                throw new DuplicatedAccountException(String.format("Conta com número %s já cadastrada",
                        userEntity.getAccountNumber()));
            }
            throw e;
        }
    }

    private boolean isUniqueConstraintViolation(DataIntegrityViolationException e) {
        return e.getMessage().contains("UK_ACCOUNTNUMBER"); // TODO: maybe there's a better way to do it
    }
}
