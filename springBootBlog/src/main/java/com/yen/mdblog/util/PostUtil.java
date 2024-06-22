package com.yen.mdblog.util;

import java.util.List;
import java.util.Optional;
import org.jsoup.Jsoup;
import org.thymeleaf.util.StringUtils;

public class PostUtil {
  public static String getHtmlContentFromMdLines(List<String> mdLines) {

    Optional<List<String>> mdLinesOpt = Optional.ofNullable(mdLines);
    return mdLinesOpt.isEmpty() ? "" : MdToHtmlRenderer.renderHtml(mdLinesOpt.get());
  }

  // get sample content
  public static String getSynopsisFromHtmlContent(String htmlContent) {

    String content = Jsoup.parse(htmlContent).text();
    return content.length() <= 150 ? content : content.substring(0, 149);
  }

  public static String getSynopsis(String content) {
    if (!StringUtils.isEmpty(content) && content.length() > 10) {
      return content.substring(0, 10);
    } else {
      return content;
    }
  }
}
