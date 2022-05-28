package com.yen.springBootPOC3.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/** book p.61 */

// TODO : fix this
public class MyServlet1 extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        System.out.println(">>> MyServlet1 doGet() run ");
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        System.out.println(">>> MyServlet1 doPost() run ");
        resp.setContentType("text/html");
        resp.setCharacterEncoding("utf-8");
        PrintWriter out = resp.getWriter();
        out.println("<html>");
        out.println("<head>");
        out.println("<title>HELLO WORLD</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>this is : MyServlet1</h1>");
        out.println("</body>");
        out.println("</html>");
        //super.doPost(req, resp);
    }

}
