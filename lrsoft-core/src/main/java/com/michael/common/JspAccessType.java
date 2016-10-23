package com.michael.common;

/**
 * Created by miles on 13-11-26.
 * jsp页面的访问类型，一般为新建、更新、明细、列表等常用值
 */
public interface JspAccessType {
    /**
     * 页面类型
     */
    String PAGE_TYPE = "pageType";
    /**
     * 新增页面
     */
    String ADD = "add";
    /**
     * 更新页面
     */
    String MODIFY = "modify";

    /**
     * 列表页面
     */
    String LIST = "list";

    /**
     * 详细信息页面
     */
    String DETAIL = "detail";
}
