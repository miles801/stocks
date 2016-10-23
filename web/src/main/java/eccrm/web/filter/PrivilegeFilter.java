package eccrm.web.filter;

import com.michael.core.security.LoginInfo;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 权限过滤器
 * Created by Michael on 2014/9/15.
 */
public class PrivilegeFilter implements Filter {
    private Logger logger = Logger.getLogger(PrivilegeFilter.class);

    @Override
    public void destroy() {
        logger.info("销毁[权限过滤器]....");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpSession session = request.getSession();
        // 获得当前用户
        String userId = (String) session.getAttribute(LoginInfo.EMPLOYEE);
        // 获得当前用户的所有权限

        // 判断当前访问的页面是否在权限范文内

    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("初始化[权限过滤器]....");
    }
}
