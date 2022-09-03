package com.example.demo.entity;

import lombok.Data;
import javax.persistence.*;
/**
 * @author longzhonghua
 * @data 2019/01/27 20:44
 */
@Entity
@Data
@Table(name = "stdu")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    @Column(columnDefinition = "enum('male','female')")
    private String sex;
    /**
     * Description:
     * 建立集合，指定關系是一對一，並且申明它在cart類別中的名稱
     * 關聯的表為card表，其主鍵是id
     * 指定外鍵名為card_id
     */
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "card_id")
     private Card card;
}

