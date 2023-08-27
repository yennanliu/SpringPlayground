package com.yen.SpringAssignmentSystem.domain;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// https://youtu.be/KMecT1HBm4c?si=ZvXY7O_2RNviIrYq&t=180

@Entity
@Table(name="users") // rename table as "users" in DB, since User is a reserved name in Mysql
@Data
@ToString
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate cohortStartDate;
    private String password;
    @ElementCollection(targetClass=Assignment.class) // https://stackoverflow.com/questions/3774198/org-hibernate-mappingexception-could-not-determine-type-for-java-util-list-at
    private List<Assignment> assignments =  new ArrayList<>();

}
