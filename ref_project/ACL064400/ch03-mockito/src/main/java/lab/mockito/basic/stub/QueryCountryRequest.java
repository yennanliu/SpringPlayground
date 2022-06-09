package lab.mockito.basic.stub;

public class QueryCountryRequest {
	private int page;
	private int rowPerPage;
	private SortColumn sortName;
	private SortOrder sortOrder;
	private String serachQuery;
	private String queryType;

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getRowPerPage() {
		return rowPerPage;
	}

	public void setRowPerPage(int rowPerPage) {
		this.rowPerPage = rowPerPage;
	}

	public SortColumn getSortName() {
		return sortName;
	}

	public void setSortName(SortColumn sortname) {
		this.sortName = sortname;
	}

	public SortOrder getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(SortOrder sortOrder) {
		this.sortOrder = sortOrder;
	}

	public String getSerachQuery() {
		return serachQuery;
	}

	public void setSerachQuery(String serachQuery) {
		this.serachQuery = serachQuery;
	}

	public String getQueryType() {
		return queryType;
	}

	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}

}
