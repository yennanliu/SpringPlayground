package com.yen.gulimall.product.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 *
 * @author yen
 * @email yen_dev@gmail.com
 * @date 2023-02-01 08:23:29
 */
/**
 *  Update:  https://youtu.be/4Y8KO4uvrn0?t=296
 */
@Data
@TableName("pms_spu_info_desc")
public class SpuInfoDescEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 商品id
	 */
	@TableId(type = IdType.INPUT) // not use auto-incremental ID, but use our input. // https://youtu.be/4Y8KO4uvrn0?t=321
	private Long spuId;
	/**
	 * 商品介绍
	 */
	private String decript;

}
