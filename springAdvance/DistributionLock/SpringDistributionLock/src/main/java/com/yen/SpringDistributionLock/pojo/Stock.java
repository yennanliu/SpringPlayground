package com.yen.SpringDistributionLock.pojo;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@TableName("db_stock")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Stock implements Serializable {

    private static final long serialVersionUID = 585369667604L;

    //@TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private String productCode;

    private String warehouse;

    private Integer count;
}
