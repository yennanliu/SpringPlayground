package com.yen.SpringBlog.utils;

// https://www.roshanadhikary.com.np/2021/05/build-a-markdown-based-blog-with-spring-boot-part-3.html

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 *  Util class for reading markdown file line by line
 */

@Slf4j
public class MdFileReader {

    /**
     *  The readLinesFromMdFile method takes a file name as an argument and creates an InputStream
     *  from the ClassPathResource available under that name inside resources/posts/ directory.
     */
    public static List<String> readLinesFromMdFile(String fileName){
        try{
            String mdFile = "/posts/" + fileName;
            log.info(">>> mdFile = {}", mdFile);

            InputStream iStream = new ClassPathResource(mdFile).getInputStream();
            BufferedReader bReader = new BufferedReader(new InputStreamReader(iStream));

            return bReader.lines().collect(Collectors.toList());

        }catch (IOException e){
            log.error(">>> MdFileReader failed");
            e.printStackTrace();
            return null;
        }
    }

    /**
     *  In the getTitleFromFileName method, we separate the extension (.md) from the rest of the file name,
     *  and split the remainder string excluding the ID portion.
     */
    public static String getTitleFromFileName(String filename) {

        String fileNameBeforeExtension = filename.split(".md")[0];
        String[] tokens = fileNameBeforeExtension.split("_");
        String[] titleTokens = Arrays.copyOfRange(tokens, 1, tokens.length);

        log.info(">>> fileNameBeforeExtension = {}, tokens = {},  titleTokens = {}", fileNameBeforeExtension, tokens, titleTokens);

        return String.join(" ", titleTokens);
    }

    /**
     *  In the getIdFromFileName method, again, we separate the extension from the rest of the file name.
     *  Then, we parse the ID portion as a long value.
     */
    public static long getIdFromFileName(String filename) {

        String fileNameBeforeExtension = filename.split(".md")[0];
        log.info(">>> fileNameBeforeExtension = {}", fileNameBeforeExtension);

        return Long.parseLong(fileNameBeforeExtension.split("_")[0]);
    }

}
