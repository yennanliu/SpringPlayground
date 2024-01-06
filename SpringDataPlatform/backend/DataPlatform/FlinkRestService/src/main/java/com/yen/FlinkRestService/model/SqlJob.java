package com.yen.FlinkRestService.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "sql_job")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SqlJob {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "statement")
    private String statement;
}
