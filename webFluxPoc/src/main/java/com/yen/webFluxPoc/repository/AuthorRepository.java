package com.yen.webFluxPoc.repository;

// https://youtu.be/42MTtF44XAs?si=7C6_aKe_Cn_zoE7f&t=123

import com.yen.webFluxPoc.model.Author;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.Collection;
import java.util.List;

// V1
// syntax : ReactiveCrudRepository<class_name, primary_key_type>
//@Repository
//public interface AuthorRepository extends ReactiveCrudRepository<Author, Integer> {
//    // will inherit basic CRUD methods via "extends ReactiveCrudRepository"
//    // similar as mybatis-plus
//}

// syntax : ReactiveCrudRepository<class_name, primary_key_type>
@Repository
public interface AuthorRepository extends R2dbcRepository<Author, Integer> {

    // will inherit basic CRUD methods and sorting via "extends R2dbcRepository"

    // setup custom SQL
    // https://youtu.be/42MTtF44XAs?si=sbJq-Feq2qPe4iNM&t=454
    //Flux<Author> findAllByIdAndNameLike(List<Integer> id, String name);
    // modified by GPT
    @Query("SELECT * FROM author WHERE id IN (:ids) AND name LIKE :name")
    Flux<Author> findAllByIdAndNameLike(List<Integer> ids, String name);

}
