package com.yen.SpringAssignmentSystem.domain;

import lombok.Data;
import lombok.ToString;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

// https://youtu.be/KMecT1HBm4c?si=9vYvZrJ7pb4kl5zJ&t=633

@Entity
@Table(name="assignment")
@Data
@ToString
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer number;

    private String name;

    private String status;

    @Column(name="github_url") // https://stackoverflow.com/questions/25283198/spring-boot-jpa-column-name-annotation-ignored
    private String githubUrl;

    private String branch;

    @Column(name="code_review_video_url")
    private String codeReviewVideoUrl;

    // https://www.baeldung.com/hibernate-detached-entity-passed-to-persist
    @ManyToOne(cascade = CascadeType.MERGE)
    private User user;

    @ManyToOne(cascade = CascadeType.MERGE)
    //@Column(name="code_reviewer")
    private User codeReviewer;

    @Column(name="submitted_date")
    private LocalDateTime submittedDate;

    @Column(name="created_date")
    private LocalDateTime createdDate;

    @Column(name="last_modified")
    private LocalDateTime lastModified;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGithubUrl() {
        return githubUrl;
    }

    public void setGithubUrl(String githubUrl) {
        this.githubUrl = githubUrl;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getCodeReviewVideoUrl() {
        return codeReviewVideoUrl;
    }

    public void setCodeReviewVideoUrl(String codeReviewVideoUrl) {
        this.codeReviewVideoUrl = codeReviewVideoUrl;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public User getCodeReviewer() {
        return codeReviewer;
    }

    public void setCodeReviewer(User codeReviewer) {
        this.codeReviewer = codeReviewer;
    }

    public LocalDateTime getSubmittedDate() {
        return submittedDate;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }

    public void setLastModified(LocalDateTime lastModified) {
        this.lastModified = lastModified;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Assignment that = (Assignment) o;
        return number.equals(that.number) && user.equals(that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, user);
    }

    public void setSubmittedDate(LocalDateTime submittedDate) {
        this.submittedDate = submittedDate;
    }

}
