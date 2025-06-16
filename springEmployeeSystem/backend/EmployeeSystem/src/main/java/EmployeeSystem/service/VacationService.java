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
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class VacationService {

  private final String adminEmail = "employee_admin@dev.com";
  @Autowired VacationRepository vacationRepository;
  @Autowired EmailService emailService;

  public List<Vacation> getVacations() {

    return vacationRepository.findAll();
  }

  public Vacation getVacationById(Integer vacationId) {

    if (vacationRepository.findById(vacationId).isPresent()) {
      return vacationRepository.findById(vacationId).get();
    }
    log.warn("No vacation with vacationId = " + vacationId);
    return null;
  }

  public List<Vacation> getVacationByUserId(Integer userId) {

    List<Vacation> vacations = vacationRepository.findAll();
    return vacations.stream()
        .filter(
            x -> {
              return x.getUserId().equals(userId);
            })
        .collect(Collectors.toList());
  }

  @Async
  public void addVacation(VacationDto vacationDto) {

    Vacation vacation = new Vacation();
    BeanUtils.copyProperties(vacationDto, vacation);
    // set default status as pending
    vacation.setStatus(VacationStatus.PENDING.getName());

    // Send vacation notification emails asynchronously using dedicated email thread pool
    log.info("Sending vacation notification emails for vacation: " + vacation);
    
    // Send notification to admin
    emailService.sendAdminNotificationAsync(
        adminEmail,
        vacation.getUserId(),
        vacation.getType(),
        vacation.getStartDate().toString(),
        vacation.getEndDate().toString()
    );
    
    // TODO: Send notification to user (need to get user email from userId)
    // For now, we can add a placeholder for future enhancement
    log.info("User notification email would be sent here once user email lookup is implemented");
    vacationRepository.save(vacation);
  }
}
