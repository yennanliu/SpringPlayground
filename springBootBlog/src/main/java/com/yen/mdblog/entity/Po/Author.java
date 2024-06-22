package com.yen.mdblog.entity.Po;

import java.util.*;
import javax.persistence.*;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@ToString
@Table(name = "authors")
public class Author {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column
  private Integer id;

  @Column private String name;

  @Column private String email;

  @Column private String url;

  @Column private Date createTime;

  @Column private Date updateTime;

  @Column
  // @OneToMany(mappedBy = "author")
  // private List<String> posts;
  private String posts;
}
