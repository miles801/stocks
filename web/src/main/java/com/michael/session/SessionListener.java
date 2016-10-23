package com.michael.session;

import com.michael.base.log.service.LoginLogService;
import com.michael.cache.OnlinePool;
import com.michael.core.SystemContainer;
import com.michael.core.security.LoginInfo;
import com.michael.utils.string.StringUtils;
import org.apache.log4j.Logger;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.Date;

/**
 * HttpSession在创建和销毁时的监听器
 * 同时记录当前session的数量
 * 销毁时会记录用户的退出时间
 *
 * @author Michael
 */
@WebListener
public class SessionListener implements HttpSessionListener {
    private Logger logger = Logger.getLogger(SessionListener.class);

    // session的数量
    private volatile int count;

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        count++;
        logger.info("=========== HttpSession :  new，当前session数量：" + count);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        HttpSession session = se.getSession();
        String empId = (String) session.getAttribute(LoginInfo.EMPLOYEE);
        if (StringUtils.isNotEmpty(empId)) {
            LoginLogService loginLogService = SystemContainer.getInstance().getBean(LoginLogService.class);
            Long lastAccessTime = session.getLastAccessedTime();
            if (lastAccessTime + 60000 < new Date().getTime()) { // 如果最后一次访问时间距离现在超过了5秒，则视为超时
                loginLogService.logout(empId, lastAccessTime, LoginLogService.LOGOUT_TYPE_TIMEOUT);
            } else {
                loginLogService.logout(empId, lastAccessTime, LoginLogService.LOGOUT_TYPE_NORMAL);
            }

            // 将用户从当前列表中移除
            OnlinePool.getInstance().remove(empId);
            logger.info("当前在线用户数:" + OnlinePool.getInstance().onlineCount());

        }
        count--;
        logger.info("=========== HttpSession :  destroy，当前session数量：" + count);
    }
}
