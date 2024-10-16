package com.yen.mdblog.entity.Po;

import java.util.*;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Entity
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "comment")
public class Comment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column
  private Integer id;

  @Column private String userName;

  @Column private Long postId;

  @Column(columnDefinition = "TEXT")
  private String commentContent;

  @Column private Date createTime;

  @Column private Date updateTime;
}
