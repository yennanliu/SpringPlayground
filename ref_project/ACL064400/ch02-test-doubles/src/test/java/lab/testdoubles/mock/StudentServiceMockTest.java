package lab.testdoubles.mock;

import org.junit.jupiter.api.Test;

import lab.testdoubles.Student;
import lab.testdoubles.StudentService;
import lab.testdoubles.StudentServiceImpl;

public class StudentServiceMockTest {
	@Test
	public void Should_get_audit_records_When_find_student() {
		// Given
		StudentDaoMock mockDao = new StudentDaoMock();
		StudentService service = new StudentServiceImpl(mockDao);
		String name = "jim";

		// When
		service.findStudent(name);

		// Then
		mockDao.expect(new Student("000", name));
		mockDao.verifyEquals();
	}
}
