package com.yen.FlinkRestService.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

/**
 *  A controller that fetch Zeppelin content
 *  and read by FE vue app,
 *  so Zeppelin UI can be shown on vue UI
 */
@Controller
public class ZeppelinProxyController {
    @GetMapping("/zeppelin")
    public String proxyZeppelin() {

        // Redirect to Zeppelin UI
        return "redirect:http://localhost:8082/";
    }

    @GetMapping("/zeppelin/**")
    public String proxyZeppelinSubpath(HttpServletRequest request) {

        // Handle subpaths to Zeppelin UI
        return "forward:http://localhost:8082/" + request.getRequestURI().substring("/zeppelin".length());
    }

}
