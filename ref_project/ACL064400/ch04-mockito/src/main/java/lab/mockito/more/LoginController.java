package lab.mockito.more;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginController {

	private final LDAPManager ldapManager;

	public LoginController(LDAPManager ldapMngr) {
		this.ldapManager = ldapMngr;
	}

	public void process(HttpServletRequest req, HttpServletResponse res) 
										throws ServletException, IOException {
		String userName = req.getParameter("userName");
		String encrypterPassword = req.getParameter("encrypterPassword");
		RequestDispatcher dispatcher;
		if (this.ldapManager.isValidUser(userName, encrypterPassword)) {
			HttpSession session = req.getSession(true);
			session.setAttribute("user", userName);
			dispatcher = req.getRequestDispatcher("home.jsp");
			dispatcher.forward(req, res);
		} else {
			req.setAttribute("error", "Invalid user name or password");
			dispatcher = req.getRequestDispatcher("login.jsp");
			dispatcher.forward(req, res);
		}
	}
}
