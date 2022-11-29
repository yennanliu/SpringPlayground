package com.yen.wallet.utils;

// https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter05-wallet/src/main/java/com/wudimanong/wallet/utils/DateUtils.java

import java.text.SimpleDateFormat;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;

/**
 * date time utils
 */
@Slf4j
public class DateUtils {

    //线程局部变量
    public static final ThreadLocal<SimpleDateFormat> sf1 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        public SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyyMMdd");
        }
    };

    public static final ThreadLocal<SimpleDateFormat> sf2 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        public SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };

    public static final ThreadLocal<SimpleDateFormat> sf3 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        public SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyyMMddHHmmss");
        }
    };

    /**
     * 时间格式化方法
     *
     * @param date
     * @param fromat
     * @return
     */
    public static String getStringByFormat(Date date, ThreadLocal<SimpleDateFormat> fromat) {
        return fromat.get().format(date);
    }
}