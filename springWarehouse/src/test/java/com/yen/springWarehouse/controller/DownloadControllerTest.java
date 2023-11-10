package com.yen.springWarehouse.controller;

import com.yen.springWarehouse.util.DateTimeUtils;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;

import static org.junit.jupiter.api.Assertions.*;

class DownloadControllerTest {

    @Test
    public void testPrepareDownloadFile(){

        String timeStamp = new SimpleDateFormat("yyyyMMdd-HH-mm-ss").format(new java.util.Date());
        System.out.println(timeStamp);

        System.out.println();

        DateTimeUtils dateTimeUtils = new DateTimeUtils();
        System.out.println(dateTimeUtils.getCurrentDateYYYYMMDDHHMMSS());
    }

}