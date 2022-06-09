package lab.mockito.more;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class DelegateControllerTest_DoReturnNotSafe {

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

	@Test
	public void demo_do_return_is_not_safe() throws Exception {
		when(request.getServletPath()).thenReturn("/logon.do");
		assertEquals("/logon.do", request.getServletPath());

		doReturn(1.111d).when(request.getServletPath());
		request.getServletPath();
	}

}
