package lab.mockito.basic.stub;

public enum SortColumn {

	ISO, NAME, PRINTABLE_NAME, ISO3, COUNTRY_CODE;

	public static SortColumn convert(String name) {
		for (SortColumn col : values()) {
			if (col.name().equalsIgnoreCase(name)) {
				return col;
			}
		}
		return null;
	}
}
