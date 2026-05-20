package EmployeeSystem.repository;

import EmployeeSystem.model.Vacation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VacationRepository extends JpaRepository<Vacation, Integer> {

  List<Vacation> findByUserId(Integer userId);
}
