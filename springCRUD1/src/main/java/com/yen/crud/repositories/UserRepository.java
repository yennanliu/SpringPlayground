package com.yen.crud.repositories;

// https://www.baeldung.com/spring-boot-crud-thymeleaf
// https://github.com/eugenp/tutorials/blob/master/spring-boot-modules/spring-boot-crud/src/main/java/com/baeldung/crud/repositories/UserRepository.java

import com.yen.crud.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    //List<User> findByName(String name);
}