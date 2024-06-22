package com.yen.mdblog.config;

import java.util.Map;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.WebRequest;

@Configuration
public class CustomErrorConfig {

  @Bean
  public DefaultErrorAttributes customErrorAttributes() {
    return new DefaultErrorAttributes() {
      @Override
      public Map<String, Object> getErrorAttributes(
          WebRequest webRequest, boolean includeStackTrace) {

        Map<String, Object> errorAttributes =
            super.getErrorAttributes(webRequest, includeStackTrace);
        // Add custom error attributes if needed
        return errorAttributes;
      }
    };
  }
}
