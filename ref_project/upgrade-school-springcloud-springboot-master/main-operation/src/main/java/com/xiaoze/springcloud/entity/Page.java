package com.xiaoze.springcloud.entity;

import lombok.Data;
import java.io.Serializable;
import java.util.List;

/**
 *
 * Page
 *
 * @author xiaoze
 * @date 2018/6/8
 *
 */

@Data
public class Page<T> implements Serializable {

    private static final long serialVersionUID = -5832154734512599075L;
    /**
     * 当前页
     */
    private int pageNum;

    /**
     * 每页的数量
     */
    private int pageSize;

    /**
     * 当前页的数量
     */
    private int size;

    /**
     * 总记录数
     */
    private long total;

    /**
     * 结果集
     */
    private List<T> list;

    /*
     * 由于startRow和endRow不常用，这里说个具体的用法
     * 可以在页面中"显示startRow到endRow 共size条数据"
     */

    /**
     * 当前页面第一个元素在数据库中的行号
     */
    private int startRow;
    
    /**
     * 当前页面最后一个元素在数据库中的行号
     */
    private int endRow;
    
    /**
     * 总页数
     */
    private int pages;

    /**
     * 前一页
     */
    private int prePage;

    /**
     * 下一页
     */
    private int nextPage;

    /**
     * 是否为第一页
     */
    private boolean isFirstPage = false;

    /**
     * 是否为最后一页
     */
    private boolean isLastPage = false;

    /**
     * 是否有前一页
     */
    private boolean hasPreviousPage = false;

    /**
     * 是否有下一页
     */
    private boolean hasNextPage = false;

    /**
     * 导航页码数
     */
    private int navigatePages;

    /**
     * 所有导航页号
     */
    private int[] navigatepageNums;

    /**
     * 导航条上的第一页
     */
    private int navigateFirstPage;

    /**
     * 导航条上的最后一页
     */
    private int navigateLastPage;

}
