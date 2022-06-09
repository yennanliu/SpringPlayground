package lab.mockito.basic.stub;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TestVerify {
	@Mock
	HttpServletRequest httpRequest;
	@Mock
	CountryDao countryDao;

	AjaxController ajaxController;

	@BeforeEach
	public void setUp() {
		ajaxController = new AjaxController(countryDao);
	}
	
	/*********************/
	/******verify()*******/
	/*********************/
	@Test
	public void Should_verify_failed_When_stub_equals() {
		// Then
		verify(httpRequest).equals(any());
	}
	@Test
	public void Should_verify_once_interaction_passed_When_one_method_is_verified() {
		// when
		httpRequest.getParameter("page");
		// Then
		verify(httpRequest).getParameter(anyString());
	}
	@Test
	public void Should_verify_all_interaction_passed_When_all_stub_methods_are_verified() {
		// Given
		when(httpRequest.getParameter(anyString()))
			.thenReturn("1", "10", SortOrder.ASC.name(), SortColumn.ISO.name());
		// When
		ajaxController.retrieve(httpRequest);
		// Then
		verify(httpRequest, times(5)).getParameter(anyString());
	}
	@Test
	public void Should_verify_never_interaction_passed_When_no_methods_are_verified() {
		// Then
		verify(httpRequest, never()).getParameter(anyString());
	}
	@Test
	public void Should_verify_atLeastOnce_interaction_passed_When_atLeastOnce_methods_are_verified() {
		// when
		httpRequest.getParameter("page");
		httpRequest.getParameter("test");
		// Then
		verify(httpRequest, atLeastOnce()).getParameter(anyString());
	}
	@Test
	public void Should_verify_atLeast_interaction_passed_When_atLeast_methods_are_verified() {
		// when
		httpRequest.getParameter("page");
		httpRequest.getParameter("test");
		// Then
		verify(httpRequest, atLeast(2)).getParameter(anyString());
	}
	@Test
	public void Should_verify_atMost_interaction_passed_When_atMost_methods_are_verified() {
		// when
		httpRequest.getParameter("page");
		httpRequest.getParameter("test");
		// Then
		verify(httpRequest, atMost(3)).getParameter(anyString());
	}
	
	@Test
	public void Should_verify_only_passed_When_one_method_is_called() throws Exception {
		// when
		httpRequest.getParameter("page");
		// Then
		verify(httpRequest, only()).getParameter(anyString());
	}
	@Test
	public void Should_verify_only_failed_When_more_methods_are_called() throws Exception {
		// when
		httpRequest.getParameter("page");
		httpRequest.getContextPath();
		// Then
		verify(httpRequest, only()).getParameter(anyString()); // should test failed!
	}
	@Test
	public void Should_verify_only_failed_When_getParameter_called_more_than_once() throws Exception {
		// Given
		when(httpRequest.getParameter(anyString()))
			.thenReturn("1", "10", SortOrder.ASC.name(), SortColumn.ISO.name());
		// When
		ajaxController.retrieve(httpRequest);
		// Then
		verify(httpRequest, only()).getParameter(anyString()); // should test failed!
	}
	

	/*********************************/
	/******verifyNoInteractions*******/
	/*********************************/
	
	@Test
	public void Should_verify_no_interaction_passed_When_never_call() {
		// Then
		verifyNoInteractions(httpRequest, countryDao);
	}
	
	@Test
	public void Should_verify_no_interaction_failed_When_ever_call() {
		// When
		httpRequest.getParameter("page");
		// Then
		verifyNoInteractions(httpRequest, countryDao);
	}
	
	@Test
	public void Should_verify_no_interaction_failed_When_ever_call_is_verified() {
		// When
		httpRequest.getParameter("page");
		// Then
		verify(httpRequest, times(1)).getParameter(anyString());
		verifyNoInteractions(httpRequest, countryDao);
	}


	/***************************************/
	/******verifyNoMoreInteractions()*******/
	/***************************************/
	
	@Test
	public void Should_verify_nomore_interaction_passed_When_never_call() {
		// Then
		verifyNoMoreInteractions(httpRequest, countryDao);
	}
	@Test
	public void Should_verify_nomore_interaction_passed_When_all_stub_methods_are_verified() {
		// When
		httpRequest.getParameter("page");
		// Then
		verify(httpRequest, times(1)).getParameter(anyString());
		verifyNoMoreInteractions(httpRequest);
	}
	@Test
	public void Should_verify_nomore_interaction_failed_When_stub_methods_are_not_all_verified() {
		// When
		httpRequest.getParameter("page");
		// Then
		verifyNoMoreInteractions(httpRequest);
	}


}