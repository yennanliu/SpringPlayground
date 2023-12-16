package EmployeeSystem.service;

import EmployeeSystem.model.Vacation;
import EmployeeSystem.model.dto.VacationDto;
import EmployeeSystem.repository.VacationRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VacationService {

    @Autowired
    VacationRepository vacationRepository;

    public List<Vacation> getVacations() {

        return vacationRepository.findAll();
    }

    public Vacation getVacationById(Integer vacationId) {

        return vacationRepository.findById(vacationId).get();
    }

    public List<Vacation> getVacationByUserId(Integer userId) {

        List<Vacation> vacations = vacationRepository.findAll();
        return vacations.stream().filter(x -> {return x.equals(userId);}
        ).collect(Collectors.toList());
    }

    public void addVacation(VacationDto vacationDto) {

        Vacation vacation = new Vacation();
        BeanUtils.copyProperties(vacationDto, vacation);
        vacationRepository.save(vacation);
    }

}
