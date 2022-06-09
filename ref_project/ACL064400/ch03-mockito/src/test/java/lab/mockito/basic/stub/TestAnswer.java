package lab.mockito.basic.stub;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class TestAnswer {

	@Mock
	HttpServletRequest httpRequest;
	@Mock
	CountryDao countryDao;

	AjaxController ajaxController;
	List<Country> countries;

	@BeforeEach
	public void setUp() {
		ajaxController = new AjaxController(countryDao);
		countries = new ArrayList<Country>();
		countries.add(create("Argentina", "AR", "32"));
		countries.add(create("USA", "US", "01"));
		countries.add(create("Brazil", "BR", "05"));
		countries.add(create("India", "IN", "91"));
	}

	class SortAnswer implements Answer<List<Country>> {
		@Override
		public List<Country> answer(InvocationOnMock invocation) throws Throwable {
			QueryCountryRequest request 
					= (QueryCountryRequest) invocation.getArguments()[0];
			int order = request.getSortOrder().equals(SortOrder.ASC) ? 1 : -1;
			SortColumn col = request.getSortName();

			Collections.sort(countries, (c1, c2) -> {
				if (SortColumn.ISO.equals(col))
					return order * c1.getIso().compareTo(c2.getIso());
				return order * c1.getName().compareTo(c2.getName());
			});

			return countries;
		}
	}

	@Test
	public void Should_get_result_When_request_iso_column_desc_and_p1_from_2pages() {
		// Given
		when(httpRequest.getParameter(anyString()))
			.thenReturn("1", "2", SortOrder.DESC.name(), SortColumn.ISO.name());
		when(countryDao.retrieve(isA(QueryCountryRequest.class)))
			.thenAnswer(new SortAnswer());
		// When
		JsonDataWrapper<Country> response = ajaxController.retrieve(httpRequest);
		// Then (DESC, 1st/2pages)
		assertEquals(2, response.getRows().size());
		assertEquals("US", response.getRows().get(0).getIso());
		assertEquals("IN", response.getRows().get(1).getIso());
//		assertEquals("BR", response.getRows().get(0).getIso());
//		assertEquals("AR", response.getRows().get(1).getIso());
	}

	@Test
	public void Should_get_result_When_request_iso_column_asc_and_p2_from_2pages() {
		// Given
		when(httpRequest.getParameter(anyString()))
			.thenReturn("2", "2", SortOrder.ASC.name(), SortColumn.ISO.name());
		when(countryDao.retrieve(isA(QueryCountryRequest.class)))
			.thenAnswer(new SortAnswer());
		// When
		JsonDataWrapper<Country> response = ajaxController.retrieve(httpRequest);
		// Then (ASC, 2nd/2pages)
		assertEquals(2, response.getRows().size());
//		assertEquals("AR", response.getRows().get(0).getIso());
//		assertEquals("BR", response.getRows().get(1).getIso());
		assertEquals("IN", response.getRows().get(0).getIso());
		assertEquals("US", response.getRows().get(1).getIso());
	}

	private Country create(String name, String iso, String coutryCode) {
		Country country = new Country();
		country.setCountryCode(coutryCode);
		country.setIso(iso);
		country.setName(name);
		return country;
	}
}
