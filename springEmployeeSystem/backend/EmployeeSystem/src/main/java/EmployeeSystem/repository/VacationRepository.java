package EmployeeSystem.repository;

import EmployeeSystem.model.Vacation;
//import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VacationRepository extends R2dbcRepository<Vacation, Integer> {}
