package lab.testdoubles.spy;

import java.sql.SQLException;

import lab.testdoubles.Student;
import lab.testdoubles.StudentDao;

public class StudentDaoSpy implements StudentDao {

	private MethodAudit audit;
	public StudentDaoSpy(MethodAudit audit) {
		this.audit = audit;
	}

	@Override
	public Student findByName(String name) throws SQLException {
		audit(name);
		return new Student("000", name);
	}

	private void audit(String name) {
		MethodInvocation invocation = new MethodInvocation();
		invocation.addParam(name).setMethod("findByName");
		audit.registerCall(invocation);
	}
}
