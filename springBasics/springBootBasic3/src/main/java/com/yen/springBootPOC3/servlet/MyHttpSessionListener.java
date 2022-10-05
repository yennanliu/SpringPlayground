package com.yen.springBootPOC3.servlet;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/** book p. 65 */

public class MyHttpSessionListener implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        System.out.println(">>> Session is created ...");
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        System.out.println(">>> Servlet context init ...");
    }
    
}
