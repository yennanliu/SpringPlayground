package EmployeeSystem.service;

import EmployeeSystem.enums.VacationStatus;
import EmployeeSystem.model.NotificationEmail;
import EmployeeSystem.model.Vacation;
import EmployeeSystem.model.dto.VacationDto;
import EmployeeSystem.repository.VacationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class VacationService {

    @Autowired
    VacationRepository vacationRepository;

    @Autowired
    MailService mailService;

    public List<Vacation> getVacations() {

        return vacationRepository.findAll();
    }

    public Vacation getVacationById(Integer vacationId) {

        if (vacationRepository.findById(vacationId).isPresent()){
            return vacationRepository.findById(vacationId).get();
        }
        log.warn("No vacation with vacationId = " + vacationId);
        return null;
    }

    public List<Vacation> getVacationByUserId(Integer userId) {

        List<Vacation> vacations = vacationRepository.findAll();
        return vacations.stream().filter(x -> {return x.getUserId().equals(userId);}
        ).collect(Collectors.toList());
    }

    public void addVacation(VacationDto vacationDto) {

        String userEmail = "employee_admin@dev.com";

        Vacation vacation = new Vacation();
        BeanUtils.copyProperties(vacationDto, vacation);
        // set default status as pending
        vacation.setStatus(VacationStatus.PENDING.getName());

        log.info("send vacation email...");
        mailService.sendMail(new NotificationEmail("Vacation created",
                userEmail, "Your vacation request is received, we will review and back to you ASAP !!"));

        vacationRepository.save(vacation);
    }

}
