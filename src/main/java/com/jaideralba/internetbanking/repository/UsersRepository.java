package com.jaideralba.internetbanking.repository;

import com.jaideralba.internetbanking.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends CrudRepository<UserEntity, Long> {


}
