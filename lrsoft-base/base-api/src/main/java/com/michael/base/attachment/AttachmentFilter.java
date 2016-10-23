package com.michael.base.attachment;

import com.michael.utils.string.StringUtils;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author miles
 * @datetime 2014/5/12 15:23
 */
public class AttachmentFilter implements Filter {
    private String params = "attachmentIds";
    private Logger logger = Logger.getLogger(AttachmentFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String cfg = filterConfig.getInitParameter("params");
        if (!StringUtils.isEmpty(cfg)) {
            params = cfg;
        }
        logger.info("============== 加载附件过滤器:过滤参数为[" + params + "] ==================");

    }

    @Override
    @SuppressWarnings("unchecked")
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String ids = request.getParameter(params);
        HttpSession session = request.getSession();
        if (!StringUtils.isEmpty(ids)) {
            // 获得所有的id
            String[] idArr = ids.split(",");
            AttachmentThreadLocal.set(idArr);
        }
        filterChain.doFilter(request, servletResponse);
        //清空Session中的附件信息
        if (!StringUtils.isEmpty(ids)) {
            session.setAttribute(AttachmentConstant.ATTACHMENT_INFO, null);
            AttachmentThreadLocal.clear();
        }
    }

    @Override
    public void destroy() {
        logger.info("============== 销毁附件过滤器 ==================");
    }
}
