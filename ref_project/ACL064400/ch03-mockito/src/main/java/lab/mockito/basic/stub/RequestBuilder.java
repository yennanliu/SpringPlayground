package lab.mockito.basic.stub;

import javax.servlet.http.HttpServletRequest;

public class RequestBuilder {
	public static QueryCountryRequest build(HttpServletRequest httpRequest) {
		QueryCountryRequest qcr = new QueryCountryRequest();
		qcr.setPage(getInt(httpRequest.getParameter("page")));
		qcr.setRowPerPage(getInt(httpRequest.getParameter("rp")));
		qcr.setSortOrder(SortOrder.convert(httpRequest.getParameter("sortorder")));
		qcr.setSortName(SortColumn.convert(httpRequest.getParameter("sortname")));
		qcr.setSerachQuery(httpRequest.getParameter("qtype"));

		return qcr;
	}

	private static Integer getInt(String val) {
		Integer retVal = null;
		try {
			retVal = Integer.parseInt(val);
		} catch (Exception e) {}
		return retVal;
	}
}
