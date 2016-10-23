package com.michael.core.pager;

import org.apache.log4j.Logger;

import javax.servlet.*;
import java.io.IOException;

/**
 * 分页过滤器：用来获取请求参数中的分页信息
 * 默认分页参数开始位置start、记录个数limit， *  例如:
 * /persons?start=0&limit=10
 * 表示查询person，从第一条元素开始，一共查询十条记录
 * 可以通过web.xml中配置start和limit对应的名称（即要传递的参数的名称）
 */
public class PagerFilter implements Filter {

    private Logger logger = Logger.getLogger(PagerFilter.class);
    private String _start = "start";
    private String _limit = "limit";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("------------ 初始化[分页过滤器] ---------------");
        String start = filterConfig.getInitParameter("start");
        if (start != null && !"".equals(start.trim())) {
            _start = start;
        }
        String limit = filterConfig.getInitParameter("limit");
        if (limit != null && !"".equals(limit.trim())) {
            _limit = limit;
        }

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String startStr = servletRequest.getParameter(_start);
        if (startStr != null && !"".equals(startStr.trim())) {
            if (startStr.matches("\\d+")) {
                Pager.setStart(Integer.parseInt(startStr));
            } else {
                logger.error("invalid param: start=[" + startStr + "],the parameter of pagination can only be a Number! ");
            }
        }

        String limitStr = servletRequest.getParameter(_limit);
        if (limitStr != null && !"".equals(limitStr.trim())) {
            if (limitStr.matches("\\d+")) {
                Pager.setLimit(Integer.parseInt(limitStr));
            } else {
                logger.error("invalid param: limit=[" + startStr + "],the parameter of pagination can only be a Number! ");
            }
        }
        String orderBy = servletRequest.getParameter("orderBy");
        if (orderBy != null && !"".equals(orderBy.trim())) {
            String reverse = servletRequest.getParameter("reverse");
            Pager.addOrder(orderBy, Boolean.parseBoolean(reverse));
        }

        filterChain.doFilter(servletRequest, servletResponse);

        Pager.removeStart();//reset pagination parameters after responded
        Pager.removeLimit();
        Pager.removeOrder();
    }

    @Override
    public void destroy() {
        logger.info("------------ 销毁[分页过滤器] ---------------");
    }
}
