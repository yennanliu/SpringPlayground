package com.yen.springBootPOC3.dao;

import com.yen.springBootPOC3.entity.Person3;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/** book p.89 */

public interface Person3Repository extends MongoRepository<Person3, String> {
    public Person3 findByFirstName(String firstName);
    public List<Person3> findByLastName(String lastName);
}
