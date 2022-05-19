package com.yen.springBootPOC2AdminSystem.config;

// https://www.youtube.com/watch?v=AOepp1XLSR0&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=61

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.zaxxer.hikari.util.DriverDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Arrays;

/** druid data source conf */

@Configuration
public class MyDataSourceConfig {

    /** Note :
     *   -> default setting : to check conditionalOnMissingBean(DataSource.class)
     *   (check if container's setting above)
     *    -> so in our case, since conditionalOnMissingBean(DataSource.class) is false
     *    -> Spring boot will use our setting (MyDataSourceConfig)
     */
    @ConfigurationProperties("spring.datasource") // NOTE !!! help load param under spring.datasource from application.yml
    @Bean
    public DataSource dataSource() throws SQLException {
        DruidDataSource druidDataSource = new DruidDataSource();
        // we will set up below in conf
//        druidDataSource.setUrl();
//        druidDataSource.setUsername();
//        druidDataSource.setPassword();

        // enable 1) druid monitor mysql request, 2) fire wall
        druidDataSource.setFilters("stat,wall");
        return druidDataSource;
    }

    // set up druid monitor page
    @Bean
    public ServletRegistrationBean statViewServlet(){
        StatViewServlet statViewServlet = new StatViewServlet();
        ServletRegistrationBean registrationBean = new ServletRegistrationBean<StatViewServlet>(statViewServlet, "/druid/*");

        // setup login account, pwd (druid page) (we can set below via application.yml as well)
//        registrationBean.addInitParameter("loginUsername", "admin");
//        registrationBean.addInitParameter("loginPassword", "123");

        return registrationBean;
    }

    // setup WebStatFiler, for web-jdbc conn monitor
    @Bean
    public FilterRegistrationBean webStatFilter(){
        WebStatFilter webStatFilter = new WebStatFilter();
        FilterRegistrationBean<WebStatFilter> filterRegistrationBean = new FilterRegistrationBean<WebStatFilter>(webStatFilter);
        // set up monitor url
        filterRegistrationBean.setUrlPatterns(Arrays.asList("/*"));
        // set up NOT monitor url
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        return filterRegistrationBean;
    }

}
