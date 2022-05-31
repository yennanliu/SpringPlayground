package com.yen.SpringBlog.utils;

// https://www.roshanadhikary.com.np/2021/05/build-a-markdown-based-blog-with-spring-boot-part-3.html

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.util.List;

public class MdToHtmlRenderer {

    public static String renderHtml(List<String> markdownLines){

        Parser parser = Parser.builder().build();
        HtmlRenderer renderer = HtmlRenderer.builder().build();

        StringBuilder renderedSB = new StringBuilder();

        for (String markDownLine : markdownLines){
            Node document = parser.parse(markDownLine);
            renderedSB.append(renderer.render(document));
        }
        return new String(renderedSB);
    }

}
