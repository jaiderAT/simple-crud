package com.jaideralba.internetbanking.service;

import com.jaideralba.internetbanking.entity.UserEntity;
import com.jaideralba.internetbanking.exception.DuplicatedAccountException;
import com.jaideralba.internetbanking.exception.InvalidUserIdException;
import com.jaideralba.internetbanking.exception.UserNotFoundException;
import com.jaideralba.internetbanking.mapper.UserMapper;
import com.jaideralba.internetbanking.model.UserRequest;
import com.jaideralba.internetbanking.model.UserResponse;
import com.jaideralba.internetbanking.repository.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UsersServiceTest {

    private UsersService service;

    @Mock
    private UsersRepository repository;

    @Mock
    private UserMapper mapper;

    private static final String UK_CONSTRAINT = "UK_ACCOUNTNUMBER";
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new UsersService(repository, mapper, new BigDecimal(100000), UK_CONSTRAINT);
    }

    @Test
    public void testGetUserById() {
        Long userId = 1L;
        UserEntity userEntity = new UserEntity();
        userEntity.setId(userId);
        when(repository.findById(userId)).thenReturn(Optional.of(userEntity));

        UserResponse userResponse = new UserResponse();
        when(mapper.toModel(userEntity)).thenReturn(userResponse);

        UserResponse result = service.get(userId);

        assertNotNull(result);
        assertEquals(userResponse, result);
    }

    @Test
    public void testGetUserByIdWithInvalidId() {
        assertThrows(InvalidUserIdException.class, () -> service.get(-1L));
    }

    @Test
    public void testGetUserByIdWithUserNotFound() {
        Long userId = 1L;
        when(repository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> service.get(userId));
    }

    @Test
    public void testUpdateUser() {
        UserEntity existingUser = new UserEntity(1L, "John Doe", false, new BigDecimal(10000),"12345", LocalDate.now());
        UserEntity updatedUser = new UserEntity(1L, "Updated John Doe", true, new BigDecimal(10000),"12345", LocalDate.now());
        UserRequest userRequest = new UserRequest(1L, "Updated John Doe", new BigDecimal(10000),"12345", LocalDate.now());
        UserResponse userResponse = new UserResponse(1L, "Updated John Doe", new BigDecimal(10000),"12345", LocalDate.now(), false);

        when(repository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(repository.save(updatedUser)).thenReturn(updatedUser);
        when(mapper.toEntity(userRequest)).thenReturn(updatedUser);
        when(mapper.toModel(updatedUser)).thenReturn(userResponse);

        UserResponse result = service.update(userRequest);

        assertEquals(updatedUser.getName(), result.getName());
        assertFalse(updatedUser.getExclusivePlan());
    }

    @Test
    public void testUpdateUserWithExclusivePlan() {
        UserEntity existingUser = new UserEntity(1L, "John Doe", false, new BigDecimal(10000),"12345", LocalDate.now());
        UserEntity updatedUser = new UserEntity(1L, "Updated John Doe", false, new BigDecimal(200000),"12345", LocalDate.now());
        UserRequest userRequest = new UserRequest(1L, "Updated John Doe", new BigDecimal(200000),"12345", LocalDate.now());
        UserResponse userResponse = new UserResponse(1L, "Updated John Doe", new BigDecimal(200000),"12345", LocalDate.now(), true);

        when(repository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(repository.save(updatedUser)).thenReturn(updatedUser);
        when(mapper.toEntity(userRequest)).thenReturn(updatedUser);
        when(mapper.toModel(updatedUser)).thenReturn(userResponse);

        UserResponse result = service.update(userRequest);

        assertEquals(updatedUser.getName(), result.getName());
        assertTrue(updatedUser.getExclusivePlan());
    }

    @Test
    public void testUpdateUserWithUserNotFound() {
        UserRequest userRequest = new UserRequest();
        userRequest.setId(1L);

        when(repository.findById(userRequest.getId())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> service.update(userRequest));
    }

    @Test
    public void testUpdateUserWithInvalidID() {
        UserRequest userRequest = new UserRequest();
        userRequest.setId(-1L);
        assertThrows(InvalidUserIdException.class, () -> service.update(userRequest));
    }

    @Test
    public void testUpdateUserWithDuplicateAccountNumber() {
        UserRequest userRequest = new UserRequest();
        userRequest.setId(1L);
        userRequest.setBalance(new BigDecimal(100000));

        UserEntity userEntity = new UserEntity();
        userEntity.setBalance(new BigDecimal(100000));

        when(repository.findById(1L)).thenReturn(Optional.of(userEntity));
        when(repository.save(any(UserEntity.class))).thenThrow(new DataIntegrityViolationException(UK_CONSTRAINT));
        when(mapper.toEntity(any(UserRequest.class))).thenReturn(userEntity);

        assertThrows(DuplicatedAccountException.class, () -> service.update(userRequest));
    }

    @Test
    public void testUpdatePartialUser() {
        UserEntity existingUser = new UserEntity(1L, "John Doe", true, new BigDecimal(200000),"12345", LocalDate.now());
        UserEntity updatedUser = new UserEntity(1L, "Updated John Doe", true, new BigDecimal(10000),"12345", LocalDate.now());
        UserRequest userRequest = new UserRequest(1L, "Updated John Doe", null,null, null);
        UserResponse userResponse = new UserResponse(1L, "Updated John Doe", new BigDecimal(10000),"12345", LocalDate.now(), false);

        when(repository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(repository.save(updatedUser)).thenReturn(updatedUser);
        when(mapper.toEntity(userRequest)).thenReturn(updatedUser);
        when(mapper.toModel(updatedUser)).thenReturn(userResponse);

        UserResponse result = service.update(userRequest);

        assertEquals(updatedUser.getName(), result.getName());
        assertFalse(updatedUser.getExclusivePlan());
    }

    @Test
    public void testUpdatePartialUserWithExclusivePlan() {
        UserEntity existingUser = new UserEntity(1L, "John Doe", false, new BigDecimal(200000),"12345", LocalDate.now());
        UserEntity updatedUser = new UserEntity(1L, "Updated John Doe", false, new BigDecimal(200000),"12345", LocalDate.now());
        UserRequest userRequest = new UserRequest(1L, "Updated John Doe", null,null, null);
        UserResponse userResponse = new UserResponse(1L, "Updated John Doe", new BigDecimal(200000),"12345", LocalDate.now(), false);

        when(repository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(repository.save(updatedUser)).thenReturn(updatedUser);
        when(mapper.toEntity(userRequest)).thenReturn(updatedUser);
        when(mapper.toModel(updatedUser)).thenReturn(userResponse);

        UserResponse result = service.update(userRequest);

        assertEquals(updatedUser.getName(), result.getName());
        assertTrue(updatedUser.getExclusivePlan());
    }

    @Test
    public void testUpdatePartialUserWithInvalidID() {
        UserRequest userRequest = new UserRequest();
        userRequest.setId(-1L);
        assertThrows(InvalidUserIdException.class, () -> service.updatePartially(userRequest));
    }

    @Test
    public void testUpdatePartialUserWithDuplicateAccountNumber() {
        UserRequest userRequest = new UserRequest();
        userRequest.setId(1L);
        userRequest.setBalance(new BigDecimal(100000));

        UserEntity userEntity = new UserEntity();
        userEntity.setBalance(new BigDecimal(100000));

        when(repository.findById(1L)).thenReturn(Optional.of(userEntity));
        when(repository.save(any(UserEntity.class))).thenThrow(new DataIntegrityViolationException(UK_CONSTRAINT));
        when(mapper.toEntity(any(UserRequest.class))).thenReturn(userEntity);

        assertThrows(DuplicatedAccountException.class, () -> service.updatePartially(userRequest));
    }

    @Test
    public void testDeleteUser() {
        when(repository.findById(1L)).thenReturn(Optional.of(new UserEntity()));
        assertDoesNotThrow(() -> service.delete(1L));
    }

    @Test
    public void testDeleteUserWithInvalidID() {
        assertThrows(InvalidUserIdException.class, () -> service.delete(-1L));
    }

    @Test
    public void testDeleteUserWithUserNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> service.delete(1L));
    }

    @Test
    public void testCreateUser() {
        UserEntity updatedUser = new UserEntity(1L, "John Doe", true, new BigDecimal(10000),"12345", LocalDate.now());
        UserRequest userRequest = new UserRequest(1L, "John Doe", new BigDecimal(10000),"12345", LocalDate.now());
        UserResponse userResponse = new UserResponse(1L, "John Doe", new BigDecimal(10000),"12345", LocalDate.now(), false);

        when(repository.save(updatedUser)).thenReturn(updatedUser);
        when(mapper.toEntity(userRequest)).thenReturn(updatedUser);
        when(mapper.toModel(updatedUser)).thenReturn(userResponse);

        UserResponse result = service.create(userRequest);

        assertEquals(updatedUser.getName(), result.getName());
        assertFalse(updatedUser.getExclusivePlan());
    }

    @Test
    public void testCreateUserWithExclusivePlan() {
        UserEntity updatedUser = new UserEntity(1L, "John Doe", false, new BigDecimal(200000),"12345", LocalDate.now());
        UserRequest userRequest = new UserRequest(1L, "John Doe", new BigDecimal(200000),"12345", LocalDate.now());
        UserResponse userResponse = new UserResponse(1L, "John Doe", new BigDecimal(200000),"12345", LocalDate.now(), false);

        when(repository.save(updatedUser)).thenReturn(updatedUser);
        when(mapper.toEntity(userRequest)).thenReturn(updatedUser);
        when(mapper.toModel(updatedUser)).thenReturn(userResponse);

        UserResponse result = service.create(userRequest);

        assertEquals(updatedUser.getName(), result.getName());
        assertTrue(updatedUser.getExclusivePlan());
    }

    @Test
    public void testCreateUserWithDuplicateAccountNumber() {
        UserRequest userRequest = new UserRequest();
        userRequest.setBalance(new BigDecimal(100000));

        UserEntity userEntity = new UserEntity();
        userEntity.setBalance(new BigDecimal(100000));

        when(repository.save(any(UserEntity.class))).thenThrow(new DataIntegrityViolationException(UK_CONSTRAINT));
        when(mapper.toEntity(any(UserRequest.class))).thenReturn(userEntity);

        assertThrows(DuplicatedAccountException.class, () -> service.create(userRequest));
    }

    @Test
    public void testListAllUsers() {
        UserEntity userEntity = new UserEntity();
        when(repository.findAll()).thenReturn(Collections.singletonList(userEntity));

        UserResponse userResponse = new UserResponse();
        when(mapper.toModel(userEntity)).thenReturn(userResponse);

        List<UserResponse> result = service.listAll();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(userResponse, result.get(0));
    }


}
