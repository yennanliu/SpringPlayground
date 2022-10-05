package com.yen.springBootPOC3.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/** book p.144 */

@Data
@Entity
@ToString
public class DemoInfo {

    @Id
    @GeneratedValue
    private long id;

    private String name;

    private String pwd;

    private Integer state;
}
