package lab.mockito.more;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class DelegateControllerTest_ArgCaptor {

	private DelegateController controller;

	@Mock
	private HttpServletRequest request;
	@Mock
	private HttpServletResponse response;
	@Mock
	private RequestDispatcher dispatcher;
	@Mock
	private LoginController loginController;
	@Mock
	private MessageRepository repository;
	@Mock
	private ErrorHandler errorHandler;

	@BeforeEach
	public void beforeEveryTest() {
		controller = new DelegateController(loginController, errorHandler, repository);
	}

	@Test
	public void Should_capture_error_code_When_subsystem_throws_exception()
			throws Exception {
		// Given
		doThrow(new IllegalStateException("LDAP error"))
			.when(loginController).process(request, response);

		doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				Error err = (Error) invocation.getArguments()[0];
				err.setErrorCode("123");
				return null;
			}
		}).when(errorHandler).mapTo(isA(Error.class));

		when(request.getServletPath()).thenReturn("/logon.do");
		when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);

		// When
		controller.doGet(request, response);

		// Then
		ArgumentCaptor<String[]> captor = ArgumentCaptor.forClass(String[].class);
		verify(repository).lookUp(captor.capture());
		assertEquals("123", captor.getValue());

		verify(request).getRequestDispatcher(eq("error.jsp"));
		verify(dispatcher).forward(request, response);
	}

}
