package com.yen.FlinkRestService.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

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

    @Column(name = "statement", columnDefinition = "TEXT")
    private String statement;

    @Column(name = "session_handle")
    private String sessionHandle;

    @Column(name = "operation_handle")
    private String operationHandle;

    @Column(name = "status")
    private String status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
