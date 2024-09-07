package EmployeeSystem.service;

import EmployeeSystem.enums.VacationStatus;
import EmployeeSystem.model.NotificationEmail;
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
import reactor.core.publisher.Flux;

@Slf4j
@Service
public class VacationService {

  private final String adminEmail = "employee_admin@dev.com";
  @Autowired VacationRepository vacationRepository;
  @Autowired MailService mailService;

  public Flux<Vacation> getVacations() {

    return vacationRepository.findAll();
  }

  public Vacation getVacationById(Integer vacationId) {

    Vacation vacation = vacationRepository.findById(vacationId).block();
    if (vacation != null) {
      return vacation;
    }
    log.warn("No vacation with vacationId = " + vacationId);
    return null;
  }

  public List<Vacation> getVacationByUserId(Integer userId) {

    List<Vacation> vacations = vacationRepository.findAll().toStream().collect(Collectors.toList());
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

    // TODO : fix/check why async send email seems NOT working
    log.info("send vacation email start ... " + vacation);
    mailService.sendMail(
        new NotificationEmail(
            "Vacation created - " + vacation.getUserId() + " - " + vacation.getType(),
            adminEmail,
            "Hi, "
                + vacation.getUserId()
                + "\n"
                + "Your vacation is received,"
                + "\n"
                + "Start date = "
                + vacation.getStartDate()
                + ", End date = "
                + vacation.getEndDate()
                + "\n"
                + "We will review and back to you ASAP !!"));
    vacationRepository.save(vacation);
  }
}
