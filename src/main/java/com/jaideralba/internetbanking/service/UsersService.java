package com.jaideralba.internetbanking.service;

import com.jaideralba.internetbanking.entity.UserEntity;
import com.jaideralba.internetbanking.exception.DuplicatedAccountException;
import com.jaideralba.internetbanking.exception.InvalidUserIdException;
import com.jaideralba.internetbanking.exception.UserNotFoundException;
import com.jaideralba.internetbanking.mapper.UserMapper;
import com.jaideralba.internetbanking.model.UserRequest;
import com.jaideralba.internetbanking.model.UserResponse;
import com.jaideralba.internetbanking.repository.UsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
@Slf4j
public class UsersService implements Users {

    private UsersRepository repository;
    private UserMapper mapper;

    @Value("${business.exclusivePlanBalanceThreshold}")
    private BigDecimal EXCLUSIVE_PLAN_BALANCE_THRESHOLD;

    @Value("${database.ukAccountNumber}")
    private String ACCOUNT_NUMBER_UNIQUE_CONSTRAINT_NAME;

    public UsersService(UsersRepository repository, UserMapper mapper,
                        @Value("${business.exclusivePlanBalanceThreshold}") BigDecimal EXCLUSIVE_PLAN_BALANCE_THRESHOLD,
                        @Value("${database.ukAccountNumber}") String ACCOUNT_NUMBER_UNIQUE_CONSTRAINT_NAME) {
        this.repository = repository;
        this.mapper = mapper;
        this.EXCLUSIVE_PLAN_BALANCE_THRESHOLD = EXCLUSIVE_PLAN_BALANCE_THRESHOLD;
        this.ACCOUNT_NUMBER_UNIQUE_CONSTRAINT_NAME = ACCOUNT_NUMBER_UNIQUE_CONSTRAINT_NAME;
    }

    @Override
    public UserResponse get(Long id) {
        log.info("User query request received. User id: {}", id);

        checkIsIdFilled(id);

        UserEntity foundUser = findUser(id);

        log.info("User query request completed successfully. User id: {}", id);
        return mapper.toModel(foundUser);
    }

    @Override
    public List<UserResponse> listAll() {
        log.info("Users listAll request received");

        Iterable<UserEntity> foundUsers = repository.findAll();

        Stream<UserEntity> foundUsersStream = StreamSupport.stream(foundUsers.spliterator(), false);

        List<UserResponse> foundUsersResponse =
                foundUsersStream
                .map(mapper::toModel)
                .collect(Collectors.toList());

        log.info("Users listAll request completed. {} users found", foundUsersResponse.size());

        return foundUsersResponse;
    }

    @Override
    public UserResponse create(UserRequest user) {
        log.info("User create request received. Account number {}", user.getAccountNumber());

        clearUserId(user);

        UserEntity userEntity = mapper.toEntity(user);

        userEntity.setExclusivePlan(isUserEligibleForExclusivePlan(userEntity));

        UserEntity createdUser = saveUser(userEntity);

        log.info("User created successfully. User id: {}, Account number {}",
                createdUser.getId(), createdUser.getAccountNumber());

        return mapper.toModel(createdUser);
    }

    @Override
    public UserResponse update(UserRequest user) {
        log.info("User update request received. User id: {}, account number {}", user.getId(), user.getAccountNumber());
        checkUserExistence(user.getId());

        UserEntity userEntity = mapper.toEntity(user);

        userEntity.setExclusivePlan(isUserEligibleForExclusivePlan(userEntity));

        UserEntity updatedUser = saveUser(userEntity);

        log.info("User updated successfully. User id: {}, account number {}",
                updatedUser.getId(), updatedUser.getAccountNumber());

        return mapper.toModel(updatedUser);
    }

    @Override
    public UserResponse updatePartially(UserRequest userRequest) {
        log.info("User partial update request received. User id: {}, account number {}", userRequest.getId());

        checkIsIdFilled(userRequest.getId());

        UserEntity foundUser = findUser(userRequest.getId());

        mapper.partialToEntity(foundUser, userRequest);

        foundUser.setExclusivePlan(isUserEligibleForExclusivePlan(foundUser));

        UserEntity updatedUser = saveUser(foundUser);

        log.info("User partially updated successfully. User id: {}, account number {}",
                updatedUser.getId(), updatedUser.getAccountNumber());

        return mapper.toModel(updatedUser);
    }

    @Override
    public void delete(Long id) {
        log.info("User delete request received. User id: {}", id);

        checkUserExistence(id);

        repository.deleteById(id);

        log.info("User deleted successfully. User id: {}", id);
    }

    private UserEntity findUser(Long id) {
        Optional<UserEntity> foundUser = repository.findById(id);

        if(foundUser.isEmpty()){
            log.info("User id {} not found", id);
            throw new UserNotFoundException("Usuário não encontrado na base de dados");
        }

        return foundUser.get();
    }

    private void checkUserExistence(Long id) {
        checkIsIdFilled(id);

        if (repository.findById(id).isEmpty()) {
            log.error("User id {} not found", id);
            throw new UserNotFoundException("Usuário não encontrado na base de dados");
        }
    }

    private void checkIsIdFilled(Long id) {
        if(Objects.isNull(id) || id < 0){
            log.error("Invalid user id {}", id);
            throw new InvalidUserIdException();
        }
    }

    private void clearUserId(UserRequest user) {
        user.setId(null);
    }

    private boolean isUserEligibleForExclusivePlan(UserEntity user){
        boolean exclusivePlan = user.getBalance().compareTo(EXCLUSIVE_PLAN_BALANCE_THRESHOLD) >= 0;

        log.info("Account number {} elegible for exclusive plan: {}", user.getAccountNumber(), exclusivePlan);

        return exclusivePlan;
    }

    private UserEntity saveUser(UserEntity userEntity) {
        try {
            return repository.save(userEntity);
        }
        catch (DataIntegrityViolationException e){
            if(isUniqueConstraintViolation(e)){
                log.error("Account number {} already exists", userEntity.getAccountNumber());
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
