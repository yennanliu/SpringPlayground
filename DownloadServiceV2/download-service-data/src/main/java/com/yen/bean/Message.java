package com.yen.bean;

// https://dzone.com/articles/pagination-and-sorting-with-spring-data-jpa
// https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories
// https://matthung0807.blogspot.com/2019/09/spring-data-jpa-pagination-and-sorting.html

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "message")
public class Message {

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "CONTENT")
    private String content;

    @Column(name = "CREATE_TIME") // sort by this col
    private Long createTime;

    // getter, setter
    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

}
