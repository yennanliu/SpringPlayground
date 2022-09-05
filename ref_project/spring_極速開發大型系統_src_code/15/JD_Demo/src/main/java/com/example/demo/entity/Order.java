package com.example.demo.entity;

import lombok.Data;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Copyright (C), 2019-2019, XXX有限公司
 * FileName: Order
 * Author:   longzhonghua
 * Date:     2019/4/12 18:40
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改時間           版本號              描述
 */

@Entity
@Data
//這裡不能用order作為MySQL的表名，違背了MySQL表名規則
@Table(name = "orders")
public class Order extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//由資料庫控制,auto是程式統一控制
    /**
     * @Description: 主鍵id
     */
    private Long id;
    //@Column(nullable = false, unique = true)

    /**
     * @Description: 商品id
     */
    private Long product_id;
    /**
     * @Description: 使用者id
     */
    private Long user_id;
    /**
     * @Description: 訂單金額
     */
    private Float amount;
    /**
    * @Description: 訂單狀態
    */
   private boolean status;

    /**
     * @Description: 建立時間
     */
    private Long createTime;
    /**
     * @Description: 修改時間
     */
    @LastModifiedDate
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Long updateTime;
}
