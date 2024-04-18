package EmployeeSystem.controller;

import EmployeeSystem.common.ApiResponse;
import EmployeeSystem.controller.VacationController;
import EmployeeSystem.model.Vacation;
import EmployeeSystem.model.dto.VacationDto;
import EmployeeSystem.service.VacationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class VacationControllerTest {

    @Mock
    private VacationService vacationService;

    @InjectMocks
    private VacationController vacationController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetVacations() {
        List<Vacation> vacations = new ArrayList<>();
        vacations.add(new Vacation());
        when(vacationService.getVacations()).thenReturn(vacations);

        ResponseEntity<List<Vacation>> responseEntity = vacationController.getVacations();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(vacations, responseEntity.getBody());
    }

    @Test
    public void testGetVacationByUserId() {
        List<Vacation> vacationList = new ArrayList<>();
        vacationList.add(new Vacation());
        when(vacationService.getVacationByUserId(1)).thenReturn(vacationList);

        ResponseEntity<List<Vacation>> responseEntity = vacationController.getDepartmentByUserId(1);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(vacationList, responseEntity.getBody());
    }

    @Test
    public void testAddVacation() {
        VacationDto vacationDto = new VacationDto();
        ApiResponse apiResponse = new ApiResponse(true, "Vacation has been added");
        //when(vacationService.addVacation(vacationDto)).thenReturn(apiResponse);

        ResponseEntity<ApiResponse> responseEntity = vacationController.addVacation(vacationDto);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        //assertEquals(apiResponse, responseEntity.getBody());
    }
}
