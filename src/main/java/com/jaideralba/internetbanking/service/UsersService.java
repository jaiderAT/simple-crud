package com.jaideralba.internetbanking.service;

import com.jaideralba.internetbanking.entity.UserEntity;
import com.jaideralba.internetbanking.exception.DuplicatedAccountException;
import com.jaideralba.internetbanking.exception.InvalidUserIdException;
import com.jaideralba.internetbanking.exception.UserNotFoundException;
import com.jaideralba.internetbanking.mapper.UserMapper;
import com.jaideralba.internetbanking.model.UserRequest;
import com.jaideralba.internetbanking.model.UserResponse;
import com.jaideralba.internetbanking.repository.UsersRepository;
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

    private UsersRepository repository;
    private UserMapper mapper;

    @Value("${business.exclusivePlanBalanceTrashold}")
    private BigDecimal EXCLUSIVE_PLAN_BALANCE_TRASHOLD;

    @Value("${database.ukAccountNumber}")
    private String ACCOUNT_NUMBER_UNIQUE_CONSTRAINT_NAME;

    public UsersService(UsersRepository repository, UserMapper mapper,
                        @Value("${business.exclusivePlanBalanceTrashold}") BigDecimal EXCLUSIVE_PLAN_BALANCE_TRASHOLD,
                        @Value("${database.ukAccountNumber}") String ACCOUNT_NUMBER_UNIQUE_CONSTRAINT_NAME) {
        this.repository = repository;
        this.mapper = mapper;
        this.EXCLUSIVE_PLAN_BALANCE_TRASHOLD = EXCLUSIVE_PLAN_BALANCE_TRASHOLD;
        this.ACCOUNT_NUMBER_UNIQUE_CONSTRAINT_NAME = ACCOUNT_NUMBER_UNIQUE_CONSTRAINT_NAME;
    }

    @Override
    public UserResponse get(Long id) {
        checkIdFilled(id);

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
        checkUserExistence(user.getId());

        UserEntity userEntity = mapper.toEntity(user);

        userEntity.setExclusivePlan(isUserEligibleForExclusivePlan(userEntity));

        UserEntity updatedUser = saveUserEntity(userEntity);

        return mapper.toModel(updatedUser);
    }

    @Override
    public UserResponse updatePartially(UserRequest userRequest) {
        checkIdFilled(userRequest.getId());

        UserEntity foundUser = repository.findById(userRequest.getId()).orElseThrow(UserNotFoundException::new);

        mapper.partialToEntity(foundUser, userRequest);

        foundUser.setExclusivePlan(isUserEligibleForExclusivePlan(foundUser));

        UserEntity updatedUser = saveUserEntity(foundUser);

        return mapper.toModel(updatedUser);
    }

    @Override
    public void delete(Long id) {
        checkUserExistence(id);
        repository.deleteById(id);
    }

    private void checkUserExistence(Long id) {
        checkIdFilled(id);

        if (repository.findById(id).isEmpty()) {
            throw new UserNotFoundException("Usuário não encontrado na base de dados");
        }
    }

    private void checkIdFilled(Long id) {
        if(Objects.isNull(id) || id < 0){
            throw new InvalidUserIdException();
        }
    }

    private void clearUserId(UserRequest user) {
        user.setId(null);
    }

    private boolean isUserEligibleForExclusivePlan(UserEntity user){
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
        return e.getMessage().contains(ACCOUNT_NUMBER_UNIQUE_CONSTRAINT_NAME); // TODO: maybe there's a better way to do it
    }
}
