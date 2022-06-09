package lab.testdoubles;

import java.sql.SQLException;

public interface StudentDao {
	public Student findByName(String name) throws SQLException;
}
