package org.ostenant.mybatis.generator.plus.page;

interface Pageable {

    /**
     * 判断当前页是否为第一页
     *
     * @return 是否为第一页
     */
    boolean isFirstPage();

    /**
     * 判断当前页是否为最后一页
     *
     * @return 是否为最后一页
     */
    boolean isLastPage();

    /**
     * 判断当前页是否有下一页
     *
     * @return 是否有下一页
     */
    boolean hasNextPage();

    /**
     * 判断当前页是否有上一页
     *
     * @return 是否有上一页
     */
    boolean hasPrevPage();

    /**
     * 获取后一页编号
     *
     * @return 后一页编号
     */
    int getNextPageNo();

    /**
     * 获取前一页编号
     *
     * @return 前一页编号
     */
    int getPrevPageNo();

    /**
     * 获取最后一页编号
     *
     * @return 最后一页编号
     */
    int getLastPageNo();

    /**
     * 获取总页数编号
     *
     * @return 总页数编号
     */
    int getTotalPageNo();

    /**
     * 获取总记录数
     *
     * @return 总记录数
     */
    int getTotalCount();

    /**
     * 获取当前页编号
     *
     * @return 当前页编号
     */
    int getPageNo();

    /**
     * 获取每一页的显示的页面元素
     *
     * @return 每一页的显示的页面元素
     */
    int getPageSize();

    /**
     * 获取当前显示的第一页的编号
     *
     * @return 第一页的编号
     */
    int getThisPageFirstElementNo();

    /**
     * 获取当前显示的最后一页的编号
     *
     * @return 最后一页的编号
     */
    int getThisPageLastElementNo();

    /**
     * 获取当已显示页的页码总数
     *
     * @return 页码总数
     */
    int getThisPageTotalElementsNo();


}
