package lab.mockito.more;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class InjectMocksAnnotationTest {

	@Mock
	HttpServletRequest request;
	@Mock
	HttpServletResponse response;
	@Mock
	RequestDispatcher dispatcher;
	
	@Mock
	LoginController loginController;

	@InjectMocks
	DelegateController controller;

	@Test
	public void show_mocks_are_injected() throws Exception {
		// Given
		when(request.getServletPath()).thenReturn("/");
		when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
		// When
		controller.doGet(request, response);
		// Then
		verify(request).getRequestDispatcher(eq("login.jsp"));
	}

	@Test
	public void show_mocks_are_not_injected() throws Exception {
		// Given
		when(request.getServletPath()).thenReturn("/");
		when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
		// When
		DelegateController controller = new DelegateController(loginController, null, null);
		controller.doGet(request, response);
		// Then
		verify(request).getRequestDispatcher(eq("login.jsp"));
	}
}
