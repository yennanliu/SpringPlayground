package com.yen.mdblog.entity.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditPost {

    private long id;
    private String title;
    private String content;
    private String synopsis;
    private String author;
    private LocalDateTime dateTime;
}
