package com.yen.mdblog.entity.Vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateComment {

    private long id;
    private Integer authorId;
    private Long postId;
    private String commentContent;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
