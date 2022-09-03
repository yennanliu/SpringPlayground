package com.example.demo.Servlet.Filter;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import org.springframework.core.annotation.Order;

@Order(1)//多個filter的時候,該序號越小,越早執行
@WebFilter(filterName = "FilterDemo", urlPatterns = "/tttt")//url過濾組態,非包組態
public class FilterDemo implements Filter{

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //init邏輯,該init將在伺服器啟動時呼叫
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        //request處理邏輯
        //request在封裝邏輯
        //chain重新寫回request和response
    }

    @Override
    public void destroy() {
        //寫destroy邏輯,該destroy邏輯將在伺服器關閉時呼叫
    }
}