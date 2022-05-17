package com.yen.springBootPOC3.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/** book p.63 */

@WebServlet(urlPatterns = "/myServlet2", description = "Servlet intro")
public class MyServlet2 extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        System.out.println(">>> MyServlet2 doGet() run ");
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println(">>> MyServlet2 doPost() run ");

        resp.setContentType("text/html");
        resp.setCharacterEncoding("utf-8");
        PrintWriter out = resp.getWriter();
        out.println("<html>");
        out.println("<head>");
        out.println("<title>HELLO WORLD</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>this is : MyServlet2</h1>");
        out.println("</body>");
        out.println("</html>");
        //super.doPost(req, resp);
    }

}
