package lab.mockito.more;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/DelegateController")
public class DelegateController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private final LoginController loginController;
	private final ErrorHandler errorHandler;
	private final MessageRepository messageRepository;

	public DelegateController(LoginController loginController, ErrorHandler errorHandler,
			MessageRepository messageRepository) {
		this.loginController = loginController;
		this.errorHandler = errorHandler;
		this.messageRepository = messageRepository;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) 
											throws ServletException, IOException {
		try {
			String path = req.getServletPath();
			if (path.equals("/")) {
				req.getRequestDispatcher("login.jsp").forward(req, res);
			} else if (path.equals("/logon.do")) {
				loginController.process(req, res);
			} else {
				req.setAttribute("error", "Invalid request path '" + path + "'");
				req.getRequestDispatcher("error.jsp").forward(req, res);
			}
		} catch (Exception ex) {

			Error error = new Error();
			error.setErrorTrace(ex.getStackTrace());
			errorHandler.mapTo(error);

			String errorMsg = null;
			if (error.getErrorCode() != null) {
				errorMsg = messageRepository.lookUp(error.getErrorCode());
			} else {
				errorMsg = ex.getMessage();
			}

			req.setAttribute("error", errorMsg);
			req.getRequestDispatcher("error.jsp").forward(req, res);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
