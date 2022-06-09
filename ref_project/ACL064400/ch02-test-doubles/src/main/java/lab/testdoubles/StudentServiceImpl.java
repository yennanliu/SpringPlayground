package lab.testdoubles;

import java.sql.SQLException;

public class StudentServiceImpl implements StudentService {

	private final StudentDao studentDAO;
	public StudentServiceImpl(StudentDao studentDAO) {
		this.studentDAO = studentDAO;
	}

	@Override
	public FindStudentResponse findStudent(String name) {
		FindStudentResponse response = null;
		try {
			Student student = studentDAO.findByName(name);
			response = new FindStudentResponse(null, student);
		} catch (SQLException e) {
			response = new FindStudentResponse(e.getMessage(), null);
		}
		return response;
	}
}
