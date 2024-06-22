// package com.yen.springWarehouse.util;
//
// import com.yen.springWarehouse.controller.DownloadController;
// import org.junit.jupiter.api.Test;
//
// import java.io.File;
// import java.net.URI;
// import java.net.URISyntaxException;
// import java.net.URL;
// import java.util.HashMap;
// import java.util.Map;
//
// import static org.junit.jupiter.api.Assertions.*;
//
// class FileUtilTest {
//
//    @Test
//    public void testWriteJsonFile() throws URISyntaxException {
//
//        String userDirectory = new File("").getAbsolutePath();
//        System.out.println(">>> current path = " + userDirectory); // >>> crrent path =
// /Users/yennanliu/SpringPlayground/springWarehouse
//        String prefix = userDirectory + "/src/main/resources/report";
//
//        FileUtil fileUtil = new FileUtil();
//        String fileName = prefix + "/" + "test_output_2.json";
//        Map<String, Object> map = new HashMap<>();
//        map.put("name", "king");
//        map.put("age", 17);
//        Boolean result = fileUtil.writeJsonFile(map, fileName);
//        System.out.println("write json to : " + fileName);
//        System.out.println(result);
//    }
//
//    @Test
//    public void testWriteJsonFile2() throws URISyntaxException {
//
//        URL url = this.getClass().getResource("/report");
//        File parentDirectory = new File(new URI(url.toString()));
//
//        System.out.println("url = " + url);
//        System.out.println("url.getPath() = " + url.getPath());
//        System.out.println("parentDirectory = " + parentDirectory);
//
//    }
//
// }
