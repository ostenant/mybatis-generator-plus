package org.ostenant.mybatis.generator.plus.page;

import org.springframework.stereotype.Component;

@Component
public class PageProperty {

    /**
     * 默认每页的记录数目
     */
    public static int DEFAULT_PAGE_SIZE = 200;

    /**
     * 分页显示的初始化页码数
     */
    public static int DEFAULT_PAGINATION = 10;

    /**
     * 分页显示的最大页码数
     */
    public static int MAX_PAGINATION = 20;

    /**
     * 分页显示的最大页码数
     */
    public static int MIN_PAGINATION = 5;

}
