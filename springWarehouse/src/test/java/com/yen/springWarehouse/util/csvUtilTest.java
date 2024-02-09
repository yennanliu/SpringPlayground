//package com.yen.springWarehouse.util;
//
//import com.yen.springWarehouse.bean.Order;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.mock.web.MockMultipartFile;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.PrintWriter;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//import static java.time.LocalTime.now;
//
//
//class csvUtilTest {
//
//    csvUtil csv_util = new csvUtil();
//
//    @Test
//    public void testOrderToCSV(){
//
//        Order o1 = new Order("some_id", 1, 2, 1, "some_status", LocalDateTime.now(), LocalDateTime.now());
//        System.out.println("o1 = " + o1.toString());
//
//        List<String[]> dataLines = new ArrayList<>();
//
//        String[] o1_str = o1.toString().split(",");
//        dataLines.add(o1_str);
//        //dataLines.add(new String[]{ "John", "Doe", "38", "Comment Data\nAnother line of comment data" });
//        //String[] o1_array = csv_util.covertArrayToCSV(o1_str);
//        //System.out.println("dataLines = " + dataLines);
//
//    }
//
//    @Test
//    public void testArrayToCSV() throws FileNotFoundException {
//
//        // https://www.baeldung.com/java-csv
//
//        String CSV_FILE_NAME = "my_csv.csv";
//
//        List<String[]> dataLines = new ArrayList<>();
//        dataLines.add(new String[]
//                { "John", "Doe", "38", "Comment Data\nAnother line of comment data" });
//        dataLines.add(new String[]
//                { "Jane", "Doe, Jr.", "19", "She said \"I'm being quoted\"" });
//
//        File csvOutputFile = new File(CSV_FILE_NAME);
//        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
//            dataLines.stream()
//                    .map(csv_util::covertArrayToCSV)
//                    .forEach(pw::println);
//        }
//    }
//
//    @Test
//    public void testLoadCsvAsList(){
//
//        byte[] inputArray = "id, name \n 1, jack \n 2, may".getBytes();
//        MockMultipartFile mockMultipartFile = new MockMultipartFile("tempFileName",inputArray);
//        System.out.println(csv_util.loadCsvAsList(mockMultipartFile).toString());
//
//        Assertions.assertFalse(mockMultipartFile.isEmpty());
//        Assertions.assertEquals(inputArray.length,mockMultipartFile.getSize());
//    }
//
//    @Test
//    public void testLoadNullCsvAsList(){
//
//        byte[] inputArray = null;
//        MockMultipartFile mockMultipartFile = new MockMultipartFile("tempFileName",inputArray);
//        System.out.println(csv_util.loadCsvAsList(mockMultipartFile));
//        Assertions.assertTrue(mockMultipartFile.isEmpty());
//    }
//
//}