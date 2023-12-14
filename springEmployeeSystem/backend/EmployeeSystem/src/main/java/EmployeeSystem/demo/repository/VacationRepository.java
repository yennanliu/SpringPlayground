package EmployeeSystem.demo.repository;

import EmployeeSystem.demo.model.Vacation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VacationRepository extends JpaRepository<Vacation, Integer> {
}
