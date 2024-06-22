package com.yen.mdblog.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;

public class CustomErrorController implements ErrorController {

  @Override
  public String getErrorPath() {
    return "/error";
  }

  @RequestMapping("/error")
  public String handleError() {

    // Provide logic to determine the error status and redirect accordingly
    // For example, you can use the HttpServletResponse.getStatus() method
    return "error"; // Thymeleaf template name (e.g., "error.html")
  }
}
