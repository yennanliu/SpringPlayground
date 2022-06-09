package lab.testdoubles.spy;

import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import lab.testdoubles.StudentDao;
import lab.testdoubles.StudentService;
import lab.testdoubles.StudentServiceImpl;

public class StudentServiceSpyTest {
	@Test
	public void Should_get_audit_records_When_find_student() {
		// Given
		MethodAudit audit = new MethodAudit();
		StudentDao dao = new StudentDaoSpy(audit);
		StudentService service = new StudentServiceImpl(dao);
		String name = "jim";
		
		// When
		service.findStudent(name);
		
		// Then
		assertEquals(1, audit.getInvocationQuantity("findByName"));
		List<Object> params = audit.getInvocation("findByName", 1).getParams();
		assertEquals(name, params.get(0));
	}
}
