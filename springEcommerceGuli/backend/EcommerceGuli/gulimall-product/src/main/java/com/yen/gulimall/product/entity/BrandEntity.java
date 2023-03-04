package com.yen.gulimall.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.yen.gulimall.common.valid.AddGroup;
import com.yen.gulimall.common.valid.ListValue;
import com.yen.gulimall.common.valid.UpdateGroup;
import com.yen.gulimall.common.valid.UpdateStatusGroup;
import lombok.Data;
import lombok.NonNull;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.*;

// https://youtu.be/8sIUw0bQyKU?t=93

/**
 * 
 * 
 * @author yen
 * @email yen_dev@gmail.com
 * @date 2023-02-01 08:23:29
 */
@Data
@TableName("pms_brand")
public class BrandEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 品牌id
	 */
	@NotNull(message = "have to set brandId when modify", groups = {UpdateGroup.class}) // https://youtu.be/bS08n6JKa-w?t=216
	@Null(message = "adding can't set brandId", groups = {AddGroup.class})
	@TableId
	private Long brandId;
	/**
	 * 品牌名
	 */
	@NotBlank
	private String name;
	/**
	 * 品牌logo地址
	 */
	@NotEmpty
	@URL(message = "logo must be a validated URL", groups = {UpdateGroup.class, AddGroup.class} ) // validate if logo is an URL address
	private String logo;
	/**
	 * 介绍
	 */
	private String descript;
	/**
	 * 显示状态[0-不显示；1-显示]
	 */
	@NotNull(groups = {AddGroup.class, UpdateStatusGroup.class})
	@ListValue(vals={0, 1}, groups = {AddGroup.class, UpdateStatusGroup.class})
	private Integer showStatus;
	/**
	 * 检索首字母
	 */
	@Pattern(regexp = "/^[a-zA-Z]$/", message = "first character must be a alphabet", groups = {UpdateGroup.class, AddGroup.class} )  // can use regex pattern
	private String firstLetter;
	/**
	 * 排序
	 */
	@NotNull
	@Min(value = 0, message = "sort must > 0")
	private Integer sort;

}
