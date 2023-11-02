package com.yen.mdblog.entity.Po;

import lombok.Data;
import lombok.ToString;
import javax.persistence.*;
import java.util.*;

@Data
@Entity
@ToString
@Table(name = "comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;

    @Column
    private Integer authorId;

    @Column
    private Long postId;

    @Column(columnDefinition = "TEXT")
    private String commentContent;

    @Column
    private Date createTime;

    @Column
    private Date updateTime;
}
