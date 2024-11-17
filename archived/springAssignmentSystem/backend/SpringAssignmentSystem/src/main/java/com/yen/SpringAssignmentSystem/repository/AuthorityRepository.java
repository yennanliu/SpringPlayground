package com.yen.SpringAssignmentSystem.repository;

import com.yen.SpringAssignmentSystem.domain.Authorities;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authorities, Long>{

}
