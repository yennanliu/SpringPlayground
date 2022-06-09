package lab.testdoubles.fake;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentDaoFake extends StudentDaoImpl {
	
	private int[] assumeResult;
	private Map<String, Integer> sqlCount = new HashMap<>();

	public void setAssumeResult(int[] assumeResult) {
		this.assumeResult = assumeResult;
	}

	public Map<String, Integer> getSqlCount() {
		return sqlCount;
	}

	@Override
	int[] update(String sql, List<Map<String, Object>> params) {
		Integer count = sqlCount.get(sql);
		if (count == null) {
			sqlCount.put(sql, params.size());
		} else {
			sqlCount.put(sql, count + params.size());
		}

		if (assumeResult != null) {
			return assumeResult;
		}

		int[] val = new int[params.size()];
		for (int i = 0; i < params.size(); i++) {
			val[i] = 1;
		}

		return val;
	}
}
