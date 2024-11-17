package com.yen.springMultiThread.dao.entity;

// https://github.com/swathisprasad/spring-boot-completable-future/blob/master/src/main/java/com/techshard/future/dao/entity/Car.java

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.*;
//import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@EqualsAndHashCode
@Entity
public class Car implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "Id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Column(nullable = false)
    private String manufacturer;

    @NotNull
    @Column(nullable = false)
    private String model;

    @NotNull
    @Column(nullable = false)
    private String type;
}
