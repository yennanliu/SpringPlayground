package com.yen.springBootPOC3.entity;

import lombok.Data;

import javax.persistence.*;

/** book p.139 */

@Data
@Entity
@Table(name = "t_account")
public class Account {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(length = 50)
    private String userName;

    private float balance;
}
