package lab.mockito.basic.stub;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class TestCustomArgumentMatcher {
	@Mock
	HttpServletRequest httpRequest;
	@Mock
	CountryDao countryDao;
	AjaxController ajaxController;
	
	Country c1, c2, c3, c4;
	List<Country> countryAll, countryP1, countryP2;

	@BeforeEach
	public void setUp() {
		ajaxController = new AjaxController(countryDao);

		c1 = create("Argentina", "AR", "32");
		c2 = create("USA", "US", "01");
		c3 = create("Brazil", "BR", "05");
		c4 = create("India", "IN", "91");

		countryAll = new ArrayList<Country>();
		countryAll.add(c1);
		countryAll.add(c2);
		countryAll.add(c3);
		countryAll.add(c4);

		countryP1 = new ArrayList<Country>();
		countryP1.add(c1);
		countryP1.add(c2);

		countryP2 = new ArrayList<Country>();
		countryP2.add(c3);
		countryP2.add(c4);
	}

	@Test
	public void Should_get_rows_When_given_countryList_sortedBy_ISO_in_asc_order() {
		// Given
		when(httpRequest.getParameter(anyString()))
			.thenReturn("1", "2", SortOrder.ASC.name(), SortColumn.ISO.name());
		when(countryDao.retrieve(argThat(new SortByIsoInAscOrderMatcher())))
			.thenReturn(countryAll);
		// When
		JsonDataWrapper<Country> response = ajaxController.retrieve(httpRequest);
		// Then
		assertEquals(2, response.getRows().size());
		assertEquals(countryP1, response.getRows());
	}

	@Test
	public void Should_get_rows_When_given_countryList_sortedBy_ISO_in_desc_order() {
		// Given
		when(httpRequest.getParameter(anyString()))
			.thenReturn("2", "2", SortOrder.DESC.name(), SortColumn.ISO.name());
		when(countryDao.retrieve(argThat(new SortByIsoInDescOrderMatcher())))
			.thenReturn(countryAll);
		// When
		JsonDataWrapper<Country> response = ajaxController.retrieve(httpRequest);
		// Then
		assertEquals(2, response.getRows().size());
		assertEquals(countryP2, response.getRows());
	}

	private Country create(String name, String iso, String coutryCode) {
		Country country = new Country();
		country.setCountryCode(coutryCode);
		country.setIso(iso);
		country.setName(name);
		return country;
	}

	class SortByIsoInAscOrderMatcher implements ArgumentMatcher<QueryCountryRequest> {
		@Override
		public boolean matches(QueryCountryRequest argument) {
			SortOrder sortOrder = argument.getSortOrder();
			SortColumn col = argument.getSortName();
			return SortOrder.ASC.equals(sortOrder) && SortColumn.ISO.equals(col);
		}
	}
	
	class SortByIsoInDescOrderMatcher implements ArgumentMatcher<QueryCountryRequest> {
		@Override
		public boolean matches(QueryCountryRequest argument) {
			SortOrder sortOrder = argument.getSortOrder();
			SortColumn col = argument.getSortName();
			return SortOrder.DESC.equals(sortOrder) && SortColumn.ISO.equals(col);
		}
	}

}