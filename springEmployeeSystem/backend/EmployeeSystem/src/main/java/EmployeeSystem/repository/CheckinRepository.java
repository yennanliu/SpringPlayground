package EmployeeSystem.repository;

import EmployeeSystem.model.Checkin;
//import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckinRepository extends R2dbcRepository<Checkin, Integer> {}
