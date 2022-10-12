package com.yen.springSSO.utils;

// book p.3-28
// https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter03-sso-authserver/src/main/java/com/wudimanong/authserver/utils/Md5Utils.java

import java.security.MessageDigest;
import java.util.UUID;

    public class Md5Utils {

        private static final String[] hexDigits = {
                "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
                "a", "b", "c", "d", "e", "f"};

        /**
         * get MD5 hash value
         *
         * @param origin
         * @param charsetname
         * @return
         */
        public static String md5Hex(String origin, String charsetname) {
            String resultString = null;
            try {
                resultString = new String(origin);
                MessageDigest md = MessageDigest.getInstance("MD5");
                if (charsetname == null || "".equals(charsetname)) {
                    resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
                } else {
                    resultString = byteArrayToHexString(md.digest(resultString.getBytes(charsetname)));
                }
            } catch (Exception exception) {
            }
            return resultString;
        }

        private static String byteArrayToHexString(byte b[]) {
            StringBuffer resultSb = new StringBuffer();
            for (int i = 0; i < b.length; i++) {
                resultSb.append(byteToHexString(b[i]));
            }

            return resultSb.toString();
        }

        private static String byteToHexString(byte b) {
            int n = b;
            if (n < 0) {
                n += 256;
            }
            int d1 = n / 16;
            int d2 = n % 16;
            return hexDigits[d1] + hexDigits[d2];
        }

        public void main(String[] args) {
            String salt = UUID.randomUUID().toString().replaceAll("-", "");
            String passWord = "123456&" + salt;
            String realPassWord = Md5Utils.md5Hex(passWord, "UTF-8");
            System.out.println("salt->" + salt + ";realPassword->" + realPassWord);
        }
    }
