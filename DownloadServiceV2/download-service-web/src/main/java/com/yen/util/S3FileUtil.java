package com.yen.util;

import com.amazonaws.services.s3.model.S3ObjectInputStream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class S3FileUtil {

    // https://www.programcreek.com/java-api-examples/?api=com.amazonaws.services.s3.model.S3ObjectInputStream
    public void read(S3ObjectInputStream s3is){

        // read content
        ByteArrayOutputStream fos = new ByteArrayOutputStream();
        byte[] read_buf = new byte[1024];
        int read_len = 0;
        try {
            while ((read_len = s3is.read(read_buf)) > 0) {
                fos.write(read_buf, 0, read_len);
            }
            //return fos.toString(ConstantsUnicode.UTF8);
            System.out.println(fos.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                s3is.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                fos.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
