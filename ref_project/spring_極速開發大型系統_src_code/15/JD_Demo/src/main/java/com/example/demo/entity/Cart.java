package com.example.demo.entity;

import lombok.Data;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Copyright (C), 2019-2019, XXX有限公司
 * FileName: Cart
 * Author:   longzhonghua
 * Date:     2019/4/12 19:05
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改時間           版本號              描述
 */

@Entity
@Data
public class Cart extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//由資料庫控制,auto是程式統一控制
    /**
     * @Description: 主鍵id
     */  private Long id;
    /**
     * @Description: 商品id
     */
    private Long product_id;
/**
* @Description: 產品目前名稱,可能會變化
*/
    private String product_name;
    /**
     * @Description: 產品數量
     */
    private Long product_num;
    /**
     * @Description: 使用者id
     */
    private Long user_id;
    /**
    * @Description: 商品目前價格
    */
    private Float product_price;
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
