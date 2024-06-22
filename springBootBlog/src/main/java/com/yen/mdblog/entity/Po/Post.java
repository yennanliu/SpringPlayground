package com.yen.mdblog.entity.Po;

import com.yen.mdblog.util.DateTimeUtil;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Entity
@Table(name = "posts")
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Post implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column
  private long id;

  @Column private String title;

  @Column(columnDefinition = "TEXT")
  private String content;

  @Column(length = 150)
  private String synopsis;

  //	@ManyToOne
  //	@JoinColumn(name = "author_id")
  //	private Author author;

  // @ManyToOne
  // @JoinColumn(name = "author_id")
  @Column private Integer authorId;

  @Column private String fontSize;

  @Column private String fontStyle;

  @Column private String fontColor;

  @Column
  @Convert(converter = DateTimeUtil.class)
  private LocalDateTime dateTime;
}
