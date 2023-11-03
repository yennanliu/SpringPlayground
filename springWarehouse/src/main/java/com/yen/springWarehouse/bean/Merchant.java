package com.yen.springWarehouse.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
//@TableName("merchant")
@Table(name="merchant")
public class Merchant implements Serializable {

    private static final long serialVersionUID = 234345356533250815L;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    //@TableId(type = IdType.AUTO)
    private int id;

    @TableField("name")
    private String name;

    @TableField("city")
    private String city;

    @TableField("type")
    private String type;

    @TableField("status")
    private String status;
}
