package com.xiaoze.springcloud.config;

import feign.Request;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

import static feign.Util.UTF_8;

/**
 * 转发所有header
 *
 * @author : xiaoze
 * @date : 2019/7/26
 */
@Configuration
public class FeignRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            Enumeration<String> headerNames = request.getHeaderNames();
            if (headerNames != null) {
                while (headerNames.hasMoreElements()) {
                    String name = headerNames.nextElement();
                    String values = request.getHeader(name);
                    template.header(name, values);

                }
            }

            // 转发参数
            Enumeration<String> bodyNames = request.getParameterNames();
            StringBuilder body = new StringBuilder();
            if (bodyNames != null) {
                while (bodyNames.hasMoreElements()) {
                    String name = bodyNames.nextElement();
                    String values = request.getParameter(name);
                    body.append(name).append("=").append(values).append("&");
                }
            }
            if (body.length() > 1) {
                body.deleteCharAt(body.length() - 1);
                template.body(Request.Body.encoded(body.toString().getBytes(UTF_8), UTF_8));
            }
        }
    }
}
