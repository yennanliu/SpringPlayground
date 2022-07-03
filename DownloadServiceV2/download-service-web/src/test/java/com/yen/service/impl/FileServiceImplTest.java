package com.yen.service.impl;

import com.yen.service.FileService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FileServiceImplTest {

    @Autowired
    FileService fileService;

    @Test
    public void mergeDownloadCsvTest() throws IOException {

        String currentPath = new java.io.File(".").getCanonicalPath();
        System.out.println(">>> Current dir:" + currentPath);

        String prefix = currentPath + "/src/test/resources/";

        /**
         *  https://docs.oracle.com/javase/tutorial/essential/io/pathOps.html
         *    -> You can easily create a Path object by using one of the following get methods from the Paths (note the plural) helper class:
         */

        //Path p1 = Paths.get("/src/main/resources/data/test1.csv");
        Path p1 = Paths.get(prefix + "data/test1.csv");
        Path p2 = Paths.get(prefix + "data/test2.csv");
        Path p3 = Paths.get(prefix + "data/test3.csv");

        String destFile = "mergedOutput";

        Path[] paths = new Path[]{p1,p2,p3};
        //Path[] paths = new Path[]{p1};
        //System.out.println(">>> paths = " + paths.toString());

        for (Path p : paths){
            System.out.println(">>> p = " + p);
        }

        fileService.mergeDownloadCsv(paths, destFile);
    }

}
