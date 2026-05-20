package EmployeeSystem.service;

import EmployeeSystem.enums.VacationStatus;

import EmployeeSystem.model.Vacation;
import EmployeeSystem.model.dto.VacationDto;
import EmployeeSystem.repository.VacationRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class VacationService {

  private final String adminEmail = "employee_admin@dev.com";
  @Autowired VacationRepository vacationRepository;
  @Autowired EmailService emailService;

  public List<Vacation> getVacations() {

    return vacationRepository.findAll();
  }

  public Page<Vacation> getVacationsPage(int page, int size) {
    return vacationRepository.findAll(PageRequest.of(page, size, Sort.by("id").descending()));
  }

  public Vacation getVacationById(Integer vacationId) {

    if (vacationRepository.findById(vacationId).isPresent()) {
      return vacationRepository.findById(vacationId).get();
    }
    log.warn("No vacation with vacationId = " + vacationId);
    return null;
  }

  public List<Vacation> getVacationByUserId(Integer userId) {
    return vacationRepository.findByUserId(userId);
  }

  @Transactional
  public void addVacation(VacationDto vacationDto) {

    Vacation vacation = new Vacation();
    BeanUtils.copyProperties(vacationDto, vacation);
    vacation.setStatus(VacationStatus.PENDING.getName());
    vacationRepository.save(vacation);

    // fire-and-forget: runs on emailTaskExecutor thread pool
    emailService.sendAdminNotificationAsync(
        adminEmail,
        vacation.getUserId(),
        vacation.getType(),
        vacation.getStartDate().toString(),
        vacation.getEndDate().toString()
    );
  }
}
