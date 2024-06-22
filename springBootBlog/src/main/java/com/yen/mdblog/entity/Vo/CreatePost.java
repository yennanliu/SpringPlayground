package com.yen.mdblog.entity.Vo;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePost {

  private long id;
  private String title;
  private String content;
  private String synopsis;
  private String author;
  private String fontSize;
  private String fontStyle;
  private String fontColor;
  private LocalDateTime dateTime;
}
