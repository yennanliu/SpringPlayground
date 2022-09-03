package com.example.demo.entity;

import com.example.demo.MyConstraint;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * Copyright (C), 2019-2019, XXX有限公司
 * FileName: User
 * Author:   longzhonghua
 * Date:     2019/4/18 18:12
 *
 * @Description: $description$
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改時間           版本號              描述
 */
@Data
public class User implements Serializable {
    /**
     * 主鍵ID
     */
    private Long id;

    @NotBlank(message = "使用者名稱不能為空")
    @Length(min = 5, max = 20, message = "使用者名稱長度為5-20個字元")
    private String name;

    @NotNull(message = "年齡不能為空")
    @Min(value = 18 ,message = "最小18歲")
    @Max(value = 60,message = "最大60歲")
    private Integer age;

/*    @NotBlank(message = "電話不可以為空")
    @Length(min = 1, max = 13, message = "電話長度需要在13個字元以內")
    private String phone;*/

    @Email(message = "請輸入信箱")
    @NotBlank(message = "信箱不能為空")
    private String email;

   /* @NotNull(message = "必須指定使用者狀態")
    @Min(value = 0, message = "使用者狀態非法")
    @Max(value = 1, message = "使用者狀態非法")
    private Integer status;*/

    @MyConstraint
    private String answer;

}
