package lab.testdoubles.stub;

import java.sql.SQLException;

import lab.testdoubles.Student;
import lab.testdoubles.StudentDao;

public class StudentDaoErrorStub implements StudentDao {
	@Override
	public Student findByName(String name) throws SQLException {
		throw new SQLException("DB connection timed out");
	}
}
