package EmployeeSystem.controller;

import EmployeeSystem.model.Vacation;
import EmployeeSystem.service.VacationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/vacation")
public class VacationController {

    @Autowired
    VacationService vacationService;

    @GetMapping("/")
    public ResponseEntity<List<Vacation>> getVacations(){

        List<Vacation> vacations = vacationService.getVacations();
        return new ResponseEntity<>(vacations, HttpStatus.OK);
    }

    @GetMapping("/{VacationById}")
    public ResponseEntity<Vacation> getDepartmentById(@PathVariable("VacationById") Integer VacationById){

        Vacation vacation = vacationService.getVacationById(VacationById);
        return new ResponseEntity<>(vacation, HttpStatus.OK);
    }

}
