package EmployeeSystem.repository;

import EmployeeSystem.model.Vacation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VacationRepository extends JpaRepository<Vacation, Integer> {
}
