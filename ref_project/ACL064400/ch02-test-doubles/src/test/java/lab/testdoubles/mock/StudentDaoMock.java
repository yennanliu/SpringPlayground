package lab.testdoubles.mock;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

import lab.testdoubles.Student;
import lab.testdoubles.StudentDao;

public class StudentDaoMock implements StudentDao {

	private Student found;
	private Student expected;

	@Override
	public Student findByName(String name) throws SQLException {
		this.found = new Student("000", name);
		return found;
	}

	public void expect(Student student) {
		this.expected = student;
	}

	public void verifyEquals() {
		assertEquals(expected.getName(), found.getName());
	}
}
