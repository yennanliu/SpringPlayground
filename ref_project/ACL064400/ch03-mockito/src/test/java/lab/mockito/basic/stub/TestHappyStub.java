package lab.mockito.basic.stub;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class TestHappyStub {

	@Mock
	private CountryDao countryDao;

	@Test
	public void Should_get_empty_country_list_When_stub() {
		// Given
		when(countryDao.retrieve(isA(QueryCountryRequest.class)))
			.thenReturn(Collections.emptyList());
		// When
		List<Country> countries = countryDao.retrieve(new QueryCountryRequest());
		// Then
		assertEquals(0, countries.size());
	}

	@Test
	public void Should_get_not_empty_country_list_When_stub() {
		// Given
		List<Country> list = new ArrayList<>();
		list.add(new Country());
		when(countryDao.retrieve(isA(QueryCountryRequest.class)))
			.thenReturn(list);
		// When
		List<Country> countries = countryDao.retrieve(new QueryCountryRequest());
		// Then
		assertEquals(1, countries.size());
	}
}