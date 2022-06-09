package lab.testdoubles.stub;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import lab.testdoubles.FindStudentResponse;
import lab.testdoubles.StudentDao;
import lab.testdoubles.StudentService;
import lab.testdoubles.StudentServiceImpl;

public class StudentServiceStubTest {
	@Test
	public void Should_not_get_student_When_dao_throw_SQLException() {
		StudentDao dao = new StudentDaoErrorStub();
		StudentService service = new StudentServiceImpl(dao);
		String name = "jim";
		FindStudentResponse resp = service.findStudent(name);
		assertFalse(resp.isSuccess());
	}

	@Test
	public void Should_get_student_When_dao_find_student() {
		StudentDao dao = new StudentDaoHappyStub();
		StudentService service = new StudentServiceImpl(dao);
		String name = "jim";
		FindStudentResponse resp = service.findStudent(name);
		assertTrue(resp.isSuccess());
		assertEquals(name, resp.getStudent().getName());
	}
}
