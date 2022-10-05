package com.yen.service;

import org.junit.Test;
import org.junit.Assert;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ReportServiceTest {

    @Autowired
    ReportService reportService;

    @Test
    public void getReportFieldTest1(){

        // test1
        String[] res1 = reportService.getReportField("trade");
        String[] exp1 = new String[]{"date", "trade_id", "amount"};

        // print java array
        // https://stackoverflow.com/questions/409784/whats-the-simplest-way-to-print-a-java-array
        System.out.println(Arrays.toString(res1));
        System.out.println(Arrays.toString(exp1));

        Assert.assertArrayEquals(res1, exp1);

        // test 2
        String[] res2 = reportService.getReportField("deposit");
        String[] exp2 = new String[]{"date", "deposit_id", "amount"};

        Assert.assertArrayEquals(res2, exp2);

        // test 3
        String[] res3 = reportService.getReportField("some_name");
        String[] exp3 = new String[]{};

        Assert.assertArrayEquals(res3, exp3);
    }

}
