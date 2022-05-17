package com.yen.springBootPOC2AdminSystem.servlet;

// https://www.youtube.com/watch?v=oi6ChwpC6rc&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=57

import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@Slf4j
@WebListener
public class MyServletContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        log.info(">>> ServletContextListener listen project init ...");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        log.info(">>> ServletContextListener listen project destroyed ...");
    }

}
