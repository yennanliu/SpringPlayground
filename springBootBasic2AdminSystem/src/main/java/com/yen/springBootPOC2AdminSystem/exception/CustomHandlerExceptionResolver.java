package com.yen.springBootPOC2AdminSystem.exception;

// https://www.youtube.com/watch?v=TOwcNVQtniU&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=56

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/** custom HandlerExceptionResolver */

//@Order(value = Ordered.HIGHEST_PRECEDENCE) // set this ExceptionResolver as top priority. (small number -> high priority)
@Component
public class CustomHandlerExceptionResolver implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

        try {
            response.sendError(911, "yen error");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ModelAndView();
    }

}
