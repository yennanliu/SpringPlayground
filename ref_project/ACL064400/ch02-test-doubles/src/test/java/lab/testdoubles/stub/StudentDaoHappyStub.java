package lab.testdoubles.stub;

import java.sql.SQLException;

import lab.testdoubles.Student;
import lab.testdoubles.StudentDao;

public class StudentDaoHappyStub implements StudentDao {
	@Override
	public Student findByName(String name) throws SQLException {
		return new Student("000", name);
	}
}
