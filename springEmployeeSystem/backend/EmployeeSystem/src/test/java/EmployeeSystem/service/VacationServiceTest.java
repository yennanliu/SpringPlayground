package EmployeeSystem.service;

import static org.mockito.Mockito.*;

import EmployeeSystem.enums.VacationStatus;
import EmployeeSystem.model.NotificationEmail;
import EmployeeSystem.model.Vacation;
import EmployeeSystem.model.dto.VacationDto;
import EmployeeSystem.repository.VacationRepository;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class VacationServiceTest {

  @Mock private VacationRepository vacationRepository;

  @Mock private MailService mailService;

  @InjectMocks private VacationService vacationService;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }


//  @Test
//  public void testAddVacation() {
//
//    VacationDto vacationDto = new VacationDto();
//    vacationDto.setUserId(1);
//    vacationDto.setStartDate(LocalDate.now());
//    vacationDto.setEndDate(LocalDate.now().plusDays(5));
//
//    Vacation vacation = new Vacation();
//    vacation.setUserId(vacationDto.getUserId());
//    vacation.setStartDate(vacationDto.getStartDate());
//    vacation.setEndDate(vacationDto.getEndDate());
//    vacation.setStatus(VacationStatus.PENDING.getName());
//
//    doNothing().when(mailService).sendMail(any(NotificationEmail.class));
//    when(vacationRepository.save(any(Vacation.class))).thenReturn(vacation);
//
//    vacationService.addVacation(vacationDto);
//
//    verify(mailService, times(1)).sendMail(any(NotificationEmail.class));
//    verify(vacationRepository, times(1)).save(any(Vacation.class));
//  }
}
