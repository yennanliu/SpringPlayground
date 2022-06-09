package lab.testdoubles.fake;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lab.testdoubles.Student;

public class StudentDaoImpl implements StudentDao {

	@Override
	public void multipleUpdate(List<Student> students) {

		List<Student> insertList = new ArrayList<>();
		List<Student> updateList = new ArrayList<>();

		for (Student student : students) {
			if (student.getRollNumber() == null) {
				insertList.add(student);
			} else {
				updateList.add(student);
			}
		}

		int rowsInserted = 0;
		int rowsUpdated = 0;
		if (!insertList.isEmpty()) {
			List<Map<String, Object>> paramList = new ArrayList<>();
			for (Student std : insertList) {
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("name", std.getName());
				paramList.add(param);
			}
			int[] rowCount = update("insert_student_sql", paramList);
			rowsInserted = sum(rowCount);
		}

		if (!updateList.isEmpty()) {
			List<Map<String, Object>> paramList = new ArrayList<>();
			for (Student std : updateList) {
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("roll_number", std.getRollNumber());
				param.put("name", std.getName());
				paramList.add(param);
			}
			int[] rowCount = update("update_student_sql", paramList);
			rowsUpdated = sum(rowCount);
		}

		if (students.size() != (rowsInserted + rowsUpdated)) {
			throw new IllegalStateException("Database update error, expected " + students.size()
					+ " records chanaged but actual " + (rowsInserted + rowsUpdated));
		}

	}

	int[] update(String sql, List<Map<String, Object>> params) {
		// perform real database access
		return new JdbcSupport().batchUpdate(sql, params);
	}

	private int sum(int[] rows) {
		int sum = 0;
		for (int val : rows) {
			sum += val;
		}
		return sum;
	}
}
