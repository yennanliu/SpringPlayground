package com.wudimanong.wallet.utils;

import java.net.InetAddress;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Joe
 */
@Slf4j
public class IDutils {

    /**
     * WorkId获取 方式:根据机器IP获取工作进程Id,如果线上机器的IP二进制表示的最后10位不重复,建议使用此种方式,列如机器的IP为192.168.1.108,二进制表示:11000000 10101000
     * 00000001 01101100,截取最后10位 01 01101100,转为十进制364,设置workerId为364.
     */
    public static int getWorkId() {//性能待优化
        int workId = 1;
        try {
            //获取机器IP二进制
            InetAddress address = InetAddress.getLocalHost();
            String sIP = address.getHostAddress();
            sIP = sIP.replaceAll("\t", "").trim();
            String[] arr = sIP.split("\\.");
            String rs = "";
            for (String str : arr) {
                String s = Integer.toBinaryString(Integer.parseInt(str));
                if (s.length() < 8) {
                    int diff = 8 - s.length();
                    for (int i = 0; i < diff; i++) {
                        s = "0" + s;
                    }
                }
                rs += s;
            }
            if (!"".equals(rs)) {
                //截取IP二进制后10位
                String last10 = rs.substring(rs.length() - 10, rs.length());
                workId = Integer.parseInt(last10, 2);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage(), e);
        }
        return workId;
    }
}
