package lab.testdoubles.dummy;

import java.math.BigDecimal;

import lab.testdoubles.Student;

public class Mark {
	private final Student student;
	private final String subjectId;
	private final BigDecimal marks;

	public Mark(Student student, String subjectId, BigDecimal marks) {
		this.student = student;
		this.subjectId = subjectId;
		this.marks = marks;
	}

	public Student getStudent() {
		return student;
	}
	public String getSubjectId() {
		return subjectId;
	}
	public BigDecimal getMarks() {
		return marks;
	}
}

