package lab.testdoubles.fake;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import lab.testdoubles.Student;

public class StudentDaoTest {

	@Test
	public void Should_rollbacks_tarnsaction_When_row_count_does_not_match() {
		// Given
		List<Student> students = new ArrayList<>();
		students.add(new Student(null, "Jim"));

		StudentDaoFake dao = new StudentDaoFake();
		int[] assumeReturned = { 0 };
		dao.setAssumeResult(assumeReturned);

		
		assertThrows(IllegalStateException.class, () -> {
			// When
			dao.multipleUpdate(students);
		});
	}

	@Test
	public void Should_creates_student_When_new_student() {
		// Given
		List<Student> students = new ArrayList<>();
		students.add(new Student(null, "Jim"));

		// When
		StudentDaoFake dao = new StudentDaoFake();
		dao.multipleUpdate(students);

		// Then
		int actualInsertCount = dao.getSqlCount().get("insert_student_sql");
		int expectedInsertCount = 1;
		assertEquals(expectedInsertCount, actualInsertCount);
	}

	@Test
	public void Should_updates_student_successfully_When_existing_student() {
		// Given
		List<Student> students = new ArrayList<>();
		students.add(new Student("001", "Bill"));

		// When
		StudentDaoFake dao = new StudentDaoFake();
		dao.multipleUpdate(students);

		// Then
		int actualUpdateCount = dao.getSqlCount().get("update_student_sql");
		int expectedUpdate = 1;
		assertEquals(expectedUpdate, actualUpdateCount);
	}

	@Test
	public void Should_creates_and_updates_students_When_new_and_existing_students() {

		// Given
		List<Student> students = new ArrayList<>();
		students.add(new Student("001", "Student-1"));
		students.add(new Student(null, "Student-2"));
		students.add(new Student("002", "Student-3"));

		// When
		StudentDaoFake dao = new StudentDaoFake();
		dao.multipleUpdate(students);

		// Then
		int actualUpdateCount = dao.getSqlCount().get("update_student_sql");
		int expectedUpdate = 2;
		assertEquals(expectedUpdate, actualUpdateCount);

		int actualInsertCount = dao.getSqlCount().get("insert_student_sql");
		int expectedInsert = 1;
		assertEquals(expectedInsert, actualInsertCount);
	}

}
