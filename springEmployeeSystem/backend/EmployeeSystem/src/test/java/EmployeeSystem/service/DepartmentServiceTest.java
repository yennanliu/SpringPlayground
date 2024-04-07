package EmployeeSystem.service;

import EmployeeSystem.enums.Role;
import EmployeeSystem.model.Department;
import EmployeeSystem.model.User;
import EmployeeSystem.repository.DepartmentRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DepartmentServiceTest {

    @Mock
    DepartmentRepository departmentRepository;

    @InjectMocks
    DepartmentService departmentService;

    // TODO : double check
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetDepartments() {

        List<Department> departments = new ArrayList<>();

        User u1 = new User(1, "abby", "mary", "abby@google.com", Role.USER,"pwd", 1, 1);
        User u2 = new User(2, "stacy", "lin", "stacy@google.com", Role.USER,"pwd", 2, 2);

        HashSet<User> users = new HashSet<>();
        users.add(u1);
        users.add(u2);
        departments.add(new Department(1, "mkt", users));

        // mock
        when(departmentRepository.findAll()).thenReturn(departments);

        List<Department> result = departmentService.getDepartments();

        assertEquals(departments, result);
        assertEquals(departments.size(), 1);
    }

    @Test
    public void testGetDepartmentByIdExists() {

        Integer departmentId = 1;
        Department department = new Department();
        department.setId(departmentId);

        when(departmentRepository.findById(departmentId)).thenReturn(Optional.of(department));

        Department result = departmentService.getDepartmentById(departmentId);

        assertEquals(department, result);
        assertEquals(result.getId(), 1);
    }

    @Test
    public void testGetDepartmentByIdNotExists() {

        Integer departmentId = 1;

        when(departmentRepository.findById(departmentId)).thenReturn(Optional.empty());

        Department result = departmentService.getDepartmentById(departmentId);

        assertNull(result);
    }

}