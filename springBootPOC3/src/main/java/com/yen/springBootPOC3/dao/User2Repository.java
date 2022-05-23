package com.yen.springBootPOC3.dao;

import com.yen.springBootPOC3.entity.Person2;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/** book p. 81 */

@RepositoryRestResource(collectionResourceRel = "people", path="people")
public class User2Repository {

    // request path
    public interface PersonRepository extends PagingAndSortingRepository<Person2, Long>{
        List<Person2> findByLastName(@Param("name") String name);
    }

}
