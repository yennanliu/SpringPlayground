package lab.mockito.more;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class LoginControllerTest {

	private LoginController controller;
	
	@Mock
	private HttpServletRequest request;
	@Mock
	private HttpServletResponse response;
	@Mock
	private LDAPManager ldapManager;
	@Mock
	private HttpSession session;
	@Mock
	private RequestDispatcher dispatcher;

	@BeforeEach
	public void setup() {
		controller = new LoginController(ldapManager);
	}

	@Test
	public void Should_route_to_home_page_When_user_credentials_is_valid() throws Exception {
		// Given
		when(ldapManager.isValidUser(anyString(), anyString())).thenReturn(true);
		when(request.getSession(true)).thenReturn(session);
		when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
		when(request.getParameter(anyString())).thenReturn("user", "pwd");
		// When
		controller.process(request, response);
		// Then
		verify(request).getSession(true);
		verify(session).setAttribute(anyString(), anyString());
		verify(request).getRequestDispatcher(eq("home.jsp"));
		verify(dispatcher).forward(request, response);
	}

	@Test
	public void Should_route_to_login_page_When_user_credentials_is_invalid() throws Exception {
		// Given
		when(ldapManager.isValidUser(anyString(), anyString())).thenReturn(false);
		when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
		when(request.getParameter(anyString())).thenReturn("user", "pwd");
		// When
		controller.process(request, response);
		// Then
		verify(request).getRequestDispatcher(eq("login.jsp"));
		verify(dispatcher).forward(request, response);
	}
}
