package com.yen.FlinkRestService.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "cluster")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Cluster {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "url")
    private String url;

    @Column(name = "port")
    private Integer port;

    @Column(name = "status")
    private String status;
}
