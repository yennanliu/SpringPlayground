package com.example.demo.entity;

import lombok.Data;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * Copyright (C), 2019-2019, XXX有限公司
 * FileName: Product
 * Author:   longzhonghua
 * Date:     2019/4/12 17:35
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改時間           版本號              描述
 */

@Entity
@Data
public class Product extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//由資料庫控制,auto是程式統一控制
   /**
   * @Description: 主鍵id
   */  private Long id;
    @Column(nullable = false, unique = true)
    @NotEmpty(message = "產品名稱不能為空")
    /**
     * @Description: 商品名
     */
    private String name;
    /**
     * @Description: 價格
     */
    private Float price;
    /**
     * @Description: 庫存數量
     */
    private Double stockCount;
    /**
     * @Description: 秒殺開始時間
     */
    private Long seckill_start_time;
    /**
     * @Description: 秒殺結束時間
     */
    private Long seckill_end_time;
    /**
     * @Description: 分類別
     */
    private String category;
    /**
     * @Description: 品牌
     */
    private String brand;
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
