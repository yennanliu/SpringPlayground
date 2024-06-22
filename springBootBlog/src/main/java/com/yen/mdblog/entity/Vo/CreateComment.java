package com.yen.mdblog.entity.Vo;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateComment {

  private long id;
  private String userName;
  private Long postId;
  private String commentContent;
  private LocalDateTime createTime;
  private LocalDateTime updateTime;
}
