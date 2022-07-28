package com.yen.SpringBlog.entities;

// https://www.roshanadhikary.com.np/2021/05/build-a-markdown-based-blog-with-spring-boot-part-1.html

import com.yen.SpringBlog.utils.LocalDateTimeConverter;
import lombok.Data;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private long id;

    @Column
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(length = 150)
    private String synopsis; // outline

    @ManyToOne
    //@JoinColumn(name = "author_id")
    // https://blog.csdn.net/h1101723183/article/details/78400768  // TODO : fix below (DDL error)
    //@JoinColumn(name = "author_id", foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    private Author author;

    @Column
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime localDateTime;
}
