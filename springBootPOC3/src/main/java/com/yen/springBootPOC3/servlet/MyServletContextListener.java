package com.yen.springBootPOC3.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/** book p.65 */

public class MyServletContextListener implements ServletContextListener {

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println(">>> MyServletContextListener contextDestroyed()");
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println(">>> MyServletContextListener contextInitialized()");
    }

}
