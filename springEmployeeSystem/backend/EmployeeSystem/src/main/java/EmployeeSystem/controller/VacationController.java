package EmployeeSystem.controller;

import EmployeeSystem.common.ApiResponse;
import EmployeeSystem.model.Vacation;
import EmployeeSystem.model.dto.VacationDto;
import EmployeeSystem.service.VacationService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vacation")
public class VacationController {

  @Autowired VacationService vacationService;

  @GetMapping("/")
  public ResponseEntity<List<Vacation>> getVacations() {

    List<Vacation> vacations = vacationService.getVacations();
    return new ResponseEntity<>(vacations, HttpStatus.OK);
  }

  @GetMapping("/{userId}")
  public ResponseEntity<List<Vacation>> getDepartmentByUserId(
      @PathVariable("userId") Integer userId) {

    List<Vacation> vacationList = vacationService.getVacationByUserId(userId);
    return new ResponseEntity<>(vacationList, HttpStatus.OK);
  }

  @PostMapping("/add")
  public ResponseEntity<ApiResponse> addVacation(@RequestBody VacationDto vacationDto) {

    vacationService.addVacation(vacationDto);
    return new ResponseEntity<>(
        new ApiResponse(true, "Vacation has been added"), HttpStatus.CREATED);
  }
}
