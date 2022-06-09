package lab.mockito.basic.stub;

import java.io.Serializable;
import java.util.List;

public class JsonDataWrapper<T> implements Serializable {

	private static final long serialVersionUID = 1L;
	// current page
	private int page;
	// total number of records for the given entity.
	private int total;
	// list of records to be displayed.
	private List<T> rows;

	public JsonDataWrapper(int page, int total, List<T> rows) {
		this.page = page;
		this.total = total;
		this.rows = rows;
	}

	public int getPage() {
		return page;
	}

	public int getTotal() {
		return total;
	}

	public List<T> getRows() {
		return rows;
	}
}
