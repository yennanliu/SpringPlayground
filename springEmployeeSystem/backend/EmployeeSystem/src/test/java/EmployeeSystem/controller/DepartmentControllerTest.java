package EmployeeSystem.controller;

import EmployeeSystem.common.ApiResponse;
import EmployeeSystem.model.Department;
import EmployeeSystem.model.dto.DepartmentDto;
import EmployeeSystem.service.DepartmentService;
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

public class DepartmentControllerTest {

    @Mock
    private DepartmentService departmentService;

    @InjectMocks
    private DepartmentController departmentController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetDepartment() {
        List<Department> departments = new ArrayList<>();
        departments.add(new Department());
        when(departmentService.getDepartments()).thenReturn(departments);

        ResponseEntity<List<Department>> responseEntity = departmentController.getDepartment();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(departments, responseEntity.getBody());
    }

    @Test
    public void testGetDepartmentById() {
        int departmentId = 1;
        Department department = new Department();
        when(departmentService.getDepartmentById(departmentId)).thenReturn(department);

        ResponseEntity<Department> responseEntity = departmentController.getDepartmentById(departmentId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(department, responseEntity.getBody());
    }

    @Test
    public void testUpdateDepartment() {
        DepartmentDto departmentDto = new DepartmentDto();

        ResponseEntity<ApiResponse> responseEntity = departmentController.updateDepartment(departmentDto);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(new ApiResponse(true, "Department has been updated").getMessage(), responseEntity.getBody().getMessage());
        verify(departmentService, times(1)).updateDepartment(departmentDto);
    }

    @Test
    public void testAddDepartment() {
        DepartmentDto departmentDto = new DepartmentDto();

        ResponseEntity<ApiResponse> responseEntity = departmentController.addDepartment(departmentDto);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(new ApiResponse(true, "Department has been added").getMessage(), responseEntity.getBody().getMessage());
        verify(departmentService, times(1)).addDepartment(departmentDto);
    }

}
