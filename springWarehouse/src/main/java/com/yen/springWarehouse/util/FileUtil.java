package com.yen.springWarehouse.util;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;

@Slf4j
public class FileUtil {

    public Boolean writeFile(String intputString, String fileName){

        try{
            String value = intputString; //"Hello";
            FileOutputStream fos = new FileOutputStream(fileName);
            DataOutputStream outStream = new DataOutputStream(new BufferedOutputStream(fos));
            outStream.writeUTF(value);
            outStream.close();
            return true;
        }catch (Exception e){
            log.error("writeFile error : " + e);
            return false;
        }
    }

}
