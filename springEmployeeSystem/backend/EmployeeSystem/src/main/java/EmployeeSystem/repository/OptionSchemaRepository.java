package EmployeeSystem.repository;

import EmployeeSystem.model.OptionSchema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionSchemaRepository extends JpaRepository<OptionSchema, Integer> {
}
