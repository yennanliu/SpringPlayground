package com.yen.bookingSystem.repository;

import com.yen.bookingSystem.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResourceRepository extends JpaRepository<Resource, String> {

    List<Resource> findByType(String type);

    List<Resource> findByActiveTrue();
}
