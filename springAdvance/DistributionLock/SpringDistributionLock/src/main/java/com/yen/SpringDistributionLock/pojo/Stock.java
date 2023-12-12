package com.yen.SpringDistributionLock.pojo;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@TableName("dbs_stock")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Stock {

    private Long id;
    private String productCode;
    private String warehouse;
    private Integer count;
}
