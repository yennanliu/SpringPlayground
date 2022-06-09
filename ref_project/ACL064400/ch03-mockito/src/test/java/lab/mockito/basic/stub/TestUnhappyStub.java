package lab.mockito.basic.stub;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith(MockitoExtension.class)
public class TestUnhappyStub {
	@Mock
	HttpServletRequest request;
	@Mock
	CountryDao countryDao;
	
	AjaxController ajaxController;

	@BeforeEach
	public void setUp() {
		ajaxController = new AjaxController(countryDao);
	}

	@Test
	public void Should_get_exception_When_countryDao_retrieve_failed() {
		// Given
		when(request.getParameter(anyString()))
			.thenReturn("1", "10", SortOrder.DESC.name(), SortColumn.ISO.name());
		when(countryDao.retrieve(isA(QueryCountryRequest.class)))
			.thenThrow(new RuntimeException("Database failure"));
		// Then
		assertThrows(RuntimeException.class, () -> {
			// When
			ajaxController.retrieve(request);
		});		
	}

	public void Should_get_exception_When_countryDao_retrieve_failed2() {
		// Given
		when(request.getParameter(anyString()))
			.thenReturn("1", "10", SortOrder.DESC.name(), SortColumn.ISO.name());
		when(countryDao.retrieve(isA(QueryCountryRequest.class)))
			.thenThrow(new RuntimeException("Database failure"));
		// When
		try {
			ajaxController.retrieve(request);
			fail();
		} catch (RuntimeException re) {
			// Then
			assertEquals("Database failure", re.getMessage());
		} catch (Exception re) {
			fail();
		}
	}
}