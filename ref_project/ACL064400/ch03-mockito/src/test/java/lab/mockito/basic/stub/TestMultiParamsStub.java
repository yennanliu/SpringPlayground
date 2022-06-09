package lab.mockito.basic.stub;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(MockitoExtension.class)
public class TestMultiParamsStub {
	
	@Mock
	private HttpServletRequest httpRequest;
	@Mock
	private CountryDao countryDao;

	AjaxController ajaxController;
	List<Country> countryList;
	@BeforeEach
	public void setUp() {
		ajaxController = new AjaxController(countryDao);
		countryList = new ArrayList<>();
		countryList.add(new Country());
	}

	@Test
	public void Should_get_response_When_given_all_httpRequest_params() {
		// Given
		when(httpRequest.getParameter(anyString()))
			.thenReturn("1")
			.thenReturn("10")
			.thenReturn(SortOrder.ASC.name())
			.thenReturn(SortColumn.ISO.name());
//			.thenReturn("1", "10", SortOrder.ASC.name(), SortColumn.ISO.name());
		
		when(countryDao.retrieve(isA(QueryCountryRequest.class)))
			.thenReturn(countryList);
		// When
		JsonDataWrapper<Country> response = ajaxController.retrieve(httpRequest);
		// Then
		assertEquals(1, response.getPage());
		assertEquals(1, response.getTotal());
		assertEquals(1, response.getRows().size());
	}
}