package com.yen.FlinkRestService.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "job")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "job_id")
    private String jobId;

    @Column(name = "name")
    private String name;

    @Column(name = "start_time")
    private Long startTime;

    @Column(name = "end_time")
    private Long endTime;

    @Column(name = "duration")
    private Long duration;

    @Column(name = "state")
    private String state;

    @Column(name = "last_modify")
    private Long lastModify;
}
