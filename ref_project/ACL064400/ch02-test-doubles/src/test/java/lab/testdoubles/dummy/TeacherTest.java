package lab.testdoubles.dummy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TeacherTest {

	@Test
	public void Should_return_very_good_by_dummy_When_marks_above_75() {
		// Given
		DummyStudent ds = new DummyStudent();

		Mark m1 = new Mark(ds, "English", new BigDecimal("81.00"));
		Mark m2 = new Mark(ds, "Math", new BigDecimal("97.00"));
		Mark m3 = new Mark(ds, "History", new BigDecimal("79.00"));

		List<Mark> marks = new ArrayList<>();
		marks.add(m3);
		marks.add(m2);
		marks.add(m1);
		
		// When
		Grades grade = new Teacher().generateGrade(marks);
		
		// Then
		assertEquals(Grades.VeryGood, grade);
	}

	@Test
	public void Should_return_very_good_by_null_When_marks_above_75() {
		// Given
		Mark m1 = new Mark(null, "English", new BigDecimal("81.00"));
		Mark m2 = new Mark(null, "Math", new BigDecimal("97.00"));
		Mark m3 = new Mark(null, "History", new BigDecimal("79.00"));

		List<Mark> marks = new ArrayList<>();
		marks.add(m3);
		marks.add(m2);
		marks.add(m1);

		// When
		Grades grade = new Teacher().generateGrade(marks);
		
		// Then
		assertEquals(Grades.VeryGood, grade);
	}

}
