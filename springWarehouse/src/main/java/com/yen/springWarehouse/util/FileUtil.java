package com.yen.springWarehouse.util;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;

@Slf4j
public class FileUtil {

  public Boolean writeFile(String intputString, String fileName) {

    try {
      String value = intputString; // "Hello";
      FileOutputStream fos = new FileOutputStream(fileName);
      DataOutputStream outStream = new DataOutputStream(new BufferedOutputStream(fos));
      outStream.writeUTF(value);
      outStream.close();
      return true;
    } catch (Exception e) {
      log.error("writeFile error : " + e);
      return false;
    }
  }

  // https://www.tutorialspoint.com/how-to-write-create-a-json-file-using-java
  public Boolean writeJsonFile(Map<String, Object> map, String fileName) {
    try {
      JSONObject jsonObject = new JSONObject();
      // insert map to jsonObject
      for (String key : map.keySet()) {
        jsonObject.put(key, map.get(key));
      }
      FileWriter file = new FileWriter(fileName);
      file.write(jsonObject.toJSONString());
      file.close();
      return true;
    } catch (Exception e) {
      log.error("writeJsonFile error : " + e);
      return false;
    }
  }
}
