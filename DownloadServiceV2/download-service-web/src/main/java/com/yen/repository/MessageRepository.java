package com.yen.repository;

// https://matthung0807.blogspot.com/2019/09/spring-data-jpa-pagination-and-sorting.html

import com.yen.bean.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, String> {

}