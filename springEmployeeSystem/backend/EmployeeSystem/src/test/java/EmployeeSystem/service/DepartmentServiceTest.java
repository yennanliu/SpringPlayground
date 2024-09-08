//package EmployeeSystem.service;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//import EmployeeSystem.enums.Role;
//import EmployeeSystem.model.Department;
//import EmployeeSystem.model.User;
//import EmployeeSystem.model.dto.DepartmentDto;
//import EmployeeSystem.repository.DepartmentRepository;
//import java.util.*;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
///**
// * NOTE :
// *
// * <p>use Mockito to mock the DepartmentRepository dependency and JUnit to write the actual tests.
// * The @Mock annotation is used to create a mock of the repository, and @InjectMocks is used to
// * inject the mock into the service being tested. The setUp method initializes the mocks before each
// * test.
// */
//class DepartmentServiceTest {
//
//  @Mock DepartmentRepository departmentRepository;
//
//  @InjectMocks DepartmentService departmentService;
//
//  // TODO : double check
//  @BeforeEach
//  public void setUp() {
//    MockitoAnnotations.initMocks(this);
//  }
//
//  @Test
//  public void testGetDepartments() {
//
//    List<Department> departments = new ArrayList<>();
//
//    User u1 = new User(1, "abby", "mary", "abby@google.com", Role.USER, "pwd", 1, 1, null);
//    User u2 = new User(2, "stacy", "lin", "stacy@google.com", Role.USER, "pwd", 2, 2, null);
//
//    HashSet<User> users = new HashSet<>();
//    users.add(u1);
//    users.add(u2);
//    departments.add(new Department(1, "mkt", users));
//
//    // mock
//    when(departmentRepository.findAll()).thenReturn(departments);
//
//    List<Department> result = departmentService.getDepartments();
//
//    assertEquals(departments, result);
//    assertEquals(departments.size(), 1);
//  }
//
//  @Test
//  public void testGetDepartmentByIdExists() {
//
//    Integer departmentId = 1;
//    Department department = new Department();
//    department.setId(departmentId);
//
//    when(departmentRepository.findById(departmentId)).thenReturn(Optional.of(department));
//
//    Department result = departmentService.getDepartmentById(departmentId);
//
//    assertEquals(department, result);
//    assertEquals(result.getId(), 1);
//  }
//
//  @Test
//  public void testGetDepartmentByIdNotExists() {
//
//    Integer departmentId = 1;
//
//    when(departmentRepository.findById(departmentId)).thenReturn(Optional.empty());
//
//    Department result = departmentService.getDepartmentById(departmentId);
//
//    assertNull(result);
//  }
//
//  @Test
//  public void testUpdateDepartment() {
//
//    DepartmentDto departmentDto = new DepartmentDto();
//    int newdepartmentId = 1;
//    departmentDto.setId(newdepartmentId);
//    departmentDto.setName("IT");
//
//    Department department = new Department();
//    int departmentId = 2;
//    department.setId(departmentId);
//    department.setName("HR");
//
//    // mock repo method
//    when(departmentRepository.findById(newdepartmentId)).thenReturn(Optional.of(department));
//
//    departmentService.updateDepartment(departmentDto);
//
//    assertEquals("IT", department.getName());
//    assertEquals(newdepartmentId, department.getId());
//  }
//
//  @Test
//  public void testAddDepartment() {
//
//    DepartmentDto departmentDto = new DepartmentDto();
//    departmentDto.setId(1);
//    departmentDto.setName("IT");
//
//    departmentService.addDepartment(departmentDto);
//
//    /**
//     * 1) verify(departmentRepository, times(1)): This part of the code is using Mockito's verify
//     * method to verify that a specific interaction with the departmentRepository mock occurred. In
//     * this case, it's checking that the save method was called on the departmentRepository.
//     *
//     * <p>2) times(1): This specifies how many times the save method should have been called. In
//     * this case, times(1) means that the save method should have been called exactly once.
//     *
//     * <p>3) any(Department.class): This is an argument matcher that matches any instance of the
//     * Department class. It's used to verify that the save method was called with any Department
//     * object as an argument.
//     *
//     * <p>So, Putting it all together, the verify statement is ensuring that the save method of the
//     * departmentRepository was called exactly once with any Department object as an argument. If
//     * this verification fails (i.e., if the save method was not called or was called more than
//     * once), the test will fail.
//     */
//    verify(departmentRepository, times(1)).save(any(Department.class));
//  }
//}
