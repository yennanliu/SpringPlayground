package com.yen.SpringAssignmentSystem.domain;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

// https://youtu.be/KMecT1HBm4c?si=9vYvZrJ7pb4kl5zJ&t=633

@Entity
//@Table(name="assignment")
@Data
@ToString
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String status;
    private String githubUrl;
    private String branch;
    @ManyToOne(optional = false) // can't have an assignment without user
    private User user;
    //private User assignTo;

}
