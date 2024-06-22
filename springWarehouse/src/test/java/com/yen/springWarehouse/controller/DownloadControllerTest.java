// package com.yen.springWarehouse.controller;
//
// import com.yen.springWarehouse.util.DateTimeUtils;
// import org.junit.jupiter.api.Test;
// import org.springframework.core.io.ClassPathResource;
// import org.springframework.core.io.Resource;
//
// import java.io.IOException;
// import java.text.SimpleDateFormat;
//
// import static org.junit.jupiter.api.Assertions.*;
//
// class DownloadControllerTest {
//
//    @Test
//    public void testPrepareDownloadFile(){
//
//        String timeStamp = new SimpleDateFormat("yyyyMMdd-HH-mm-ss").format(new java.util.Date());
//        System.out.println(timeStamp);
//
//        System.out.println();
//
//        DateTimeUtils dateTimeUtils = new DateTimeUtils();
//        System.out.println(dateTimeUtils.getCurrentDateYYYYMMDDHHMMSS());
//    }
//
//    @Test
//    public void testGetResourcePath() throws IOException {
//
//        ClassPathResource r1 = new ClassPathResource("application.properties");
//        System.out.println(r1.getPath());
//        System.out.println(r1.exists());
//        System.out.println(r1.getURL());
//
////        //ClassPathResource r2 = new ClassPathResource("/report/20231123-16-16-09_report.json");
////        ClassPathResource r2 = new ClassPathResource("20231123-16-52-30_report.json");
////        System.out.println(r2.getPath());
////        System.out.println(r2.exists());
////        System.out.println(r2.getURL());
//    }
//
// }
