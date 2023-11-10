package com.yen.springWarehouse.bean;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Report {

    String dataTime;
    String merchantName;
    String productName;
    Integer count;
}
