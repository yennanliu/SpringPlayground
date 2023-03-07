package com.yen.gulimall.product.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author yen
 * @email yen_dev@gmail.com
 * @date 2023-02-01 08:23:29
 */
@Data
@TableName("pms_category")
public class CategoryEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 分类id
     */
    @TableId
    private Long catId;
    /**
     * 分类名称
     */
    private String name;
    /**
     * 父分类id
     */
    private Long parentCid;
    /**
     * 层级
     */
    private Integer catLevel;
    /**
     * 是否显示[0-不显示，1显示]
     *
     *  Update: logic deletion
     *      - https://youtu.be/6in5XKRnxNY?t=602
     *      0 : delete (delval = "0")
     *      1 : NOT delete (value = "1")
     */
    @TableLogic(value = "1", delval = "0") // logic deletion
    private Integer showStatus;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 图标地址
     */
    private String icon;
    /**
     * 计量单位
     */
    private String productUnit;
    /**
     * 商品数量
     */
    private Integer productCount;

    // https://youtu.be/i3NZnXNTYBk?t=436
    @JsonInclude(JsonInclude.Include.NON_EMPTY) // only return value when json is NOT empty
    @TableField(exist = false)
    // https://youtu.be/5aWkhC7plsc?t=646
    private List<CategoryEntity> children;

}
