package com.yen.SpringBlog.entities;

// https://www.roshanadhikary.com.np/2021/05/build-a-markdown-based-blog-with-spring-boot-part-1.html

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "authors")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private String name;

    @Column
    private String email;

    @Column
    private String url;

    @Column
    @OneToMany(mappedBy = "author")
    private List<Post> posts;
}
