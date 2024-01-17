package com.yen.FlinkRestService.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

// entity for zeppelin notebook

@Entity
@Table(name = "notebook")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Notebook {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "zeppelin_note_id")
    private String zeppelinNoteId; // 2JNCWSUE4

    @Column(name = "interpreter_group")
    private String interpreterGroup; // spark, flink ...

    @Column(name = "insert_time")
    private Date insertTime;

    @Column(name = "update_time")
    private Date updateTime;
}
