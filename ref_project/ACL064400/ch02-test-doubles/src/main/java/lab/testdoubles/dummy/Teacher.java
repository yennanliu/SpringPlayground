package lab.testdoubles.dummy;

import java.math.BigDecimal;
import java.util.List;

public class Teacher {
	public Grades generateGrade(List<Mark> marks) {
		BigDecimal sum = BigDecimal.ZERO;
		for (Mark mark : marks) {
			sum = sum.add(mark.getMarks());
		}
		BigDecimal avg = BigDecimal.valueOf(sum.doubleValue() / marks.size());
		if (avg.compareTo(new BigDecimal("90.00")) > 0) {
			return Grades.Excellent;
		}
		if (avg.compareTo(new BigDecimal("75.00")) > 0) {
			return Grades.VeryGood;
		}
		if (avg.compareTo(new BigDecimal("60.00")) > 0) {
			return Grades.Good;
		}
		if (avg.compareTo(new BigDecimal("40.00")) > 0) {
			return Grades.Average;
		}
		return Grades.Poor;
	}
}
