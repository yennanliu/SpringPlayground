package EmployeeSystem.repository;

import EmployeeSystem.model.OptionSchema;
//import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionSchemaRepository extends R2dbcRepository<OptionSchema, Integer> {}
