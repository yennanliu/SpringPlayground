package com.yen.SpringBlog.utils;

// https://www.roshanadhikary.com.np/2021/05/build-a-markdown-based-blog-with-spring-boot-part-4.html

import java.util.List;
import java.util.Optional;

import org.jsoup.Jsoup;

public class PostUtil {

    public static String getHtmlContentFromMdLines(List<String> mdLines){
        Optional<List<String>> mdLinesOpt = Optional.ofNullable(mdLines);
        if (mdLinesOpt == null){
            return "";
        }
        return MdToHtmlRenderer.renderHtml(mdLinesOpt.get());
    }

    public static String getSynopsisFromHtmlContent(String htmlContent){
        String content = Jsoup.parse(htmlContent).text();
        if (content.length() < 150){
            return content;
        }
        // TODO : fix below
        return content.substring(0, 149);
    }

}
