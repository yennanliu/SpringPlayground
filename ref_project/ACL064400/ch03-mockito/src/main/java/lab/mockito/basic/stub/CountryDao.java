package lab.mockito.basic.stub;

import java.util.List;

public interface CountryDao {

	List<Country> retrieve(QueryCountryRequest command);
}
