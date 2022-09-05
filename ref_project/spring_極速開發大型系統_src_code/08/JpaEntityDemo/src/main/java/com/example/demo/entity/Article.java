package com.example.demo.entity;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;


/**
 * Copyright (C), 2019-2019, XXX有限公司
 * FileName: article
 * Author:   longzhonghua
 * Date:     2019/4/27 16:01
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改時間           版本號              描述
 */
@Entity
@Data
public class Article  implements Serializable {
    @Id
    /**
     * Description: 由資料庫控制,auto是程式統一控制
     */
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    @NotEmpty(message = "標題不能為空")
    private String title;
    /**
     * Description: 列舉型態
     */
    @Column(columnDefinition="enum('圖','圖文','文')")
    private String type;//型態
    /**
     * Description:  Boolean型態預設false
     */
    private Boolean available = Boolean.FALSE;
    @Size(min=0, max=20)
    private String keyword;
    @Size(max = 255)
    private String description;
    @Column(nullable = false)
    private String body;


   /**
    * Description: 建立虛擬字段
    */
   @Transient
    private List keywordlists;

    public List getKeywordlists() {

        return Arrays.asList(this.keyword.trim().split("|"));
    }
    public void setKeywordlists(List keywordlists) {
        this.keywordlists = keywordlists;

    }

}
