package com.example.demo;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author longzhonghua
 * @createdata 3/18/2019 7:59 AM
 * @description
 */

/**
 * Description: 加入註釋進行修飾
 */
@WebServlet(urlPatterns = "/ServletDemo02/*")
public class ServletDemo02 extends HttpServlet{
    /**
     * Description:
     * 重新定義doGet方法,父類別的HttpServlet的doGet方法是空的，沒有實現任何程式碼，子類別需要重新定義此方法。
     *客戶使用GET模式請求Servlet時，Web容器呼叫doGet方法處理請求。
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("doGet");
        resp.getWriter().print("Servlet ServletDemo02");
    }
}
