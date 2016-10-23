package com.michael.base.emp.web;

import com.michael.base.emp.service.EmpService;
import com.michael.base.emp.vo.EmpVo;
import com.michael.base.log.domain.LoginLog;
import com.michael.base.log.service.LoginLogService;
import com.michael.base.position.domain.Position;
import com.michael.base.position.service.PositionEmpService;
import com.michael.base.position.service.PositionService;
import com.michael.base.resource.service.ResourceService;
import com.michael.cache.OnlinePool;
import com.michael.core.SystemContainer;
import com.michael.core.context.Login;
import com.michael.core.context.SecurityContext;
import com.michael.core.pool.ThreadPool;
import com.michael.core.security.LoginInfo;
import com.michael.core.web.BaseController;
import com.michael.utils.NetUtils;
import com.michael.utils.gson.GsonUtils;
import com.michael.utils.string.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

/**
 * @author miles
 * @datetime 2014/3/20 11:06
 */
@Controller
@Scope("prototype")
public class LoginCtrl extends BaseController {
    private Logger logger = Logger.getLogger(LoginCtrl.class);
    @Resource
    private EmpService empService;


    /**
     * 用户登录
     * 必须传入username和password属性
     * 默认为admin/eccrm
     */
    @RequestMapping(value = {"/login", "/base/emp/login"}, method = RequestMethod.POST)
    @ResponseBody
    public void login(HttpServletResponse response, final HttpServletRequest request) {
        // 获得登录信息
        EmpTemp emp = GsonUtils.wrapDataToEntity(request, EmpTemp.class);
        Assert.notNull(emp, "登录失败!未获取到登录信息!");
        final EmpVo vo = empService.login(emp.getLoginName(), emp.getPassword());
        final HttpSession session = request.getSession();
        session.setAttribute(LoginInfo.HAS_LOGIN, true);
        session.setAttribute(LoginInfo.USERNAME, vo.getLoginName());
        session.setAttribute(LoginInfo.EMPLOYEE, vo.getId());
        session.setAttribute(LoginInfo.EMPLOYEE_NAME, vo.getName());
        session.setAttribute(LoginInfo.LOGIN_DATETIME, new Date().getTime());
        session.setAttribute(LoginInfo.ORG, vo.getOrgId());
        session.setAttribute(LoginInfo.ORG_NAME, vo.getOrgName());

        // 设置登录信息
        final Login login = new Login();
        SecurityContext.set(login);
        login.setEmpId(vo.getId());

        // 加载个人所具有的所有岗位ID
        SystemContainer systemContainer = SystemContainer.getInstance();
        List<String> positionIds = systemContainer.getBean(PositionEmpService.class).myPosition();
        if (positionIds != null && !positionIds.isEmpty()) {
            PositionService positionService = systemContainer.getBean(PositionService.class);
            for (String p : positionIds) {
                Position position = positionService.findById(p);
                if (position == null || StringUtils.isEmpty(position.getCode())) {
                    continue;
                }
                session.setAttribute("P_" + position.getCode(), true);
            }
        }

        // 加载个人的所有的操作资源编号
        List<String> resourceCodes = systemContainer.getBean(ResourceService.class)
                .queryElementResourceCode();
        if (resourceCodes != null) {
            for (String code : resourceCodes) {
                session.setAttribute(code, true);
            }
        }

        final String ip = NetUtils.getClientIpAddress(request);
        // 写入登录日志
        ThreadPool.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                LoginLogService loginLogService = SystemContainer.getInstance().getBean(LoginLogService.class);
                LoginLog log = new LoginLog();
                log.setIp(ip);
                log.setDescription(request.getHeader("User-Agent"));
                log.setLoginTime(new Date());
                log.setCreatorId(vo.getId());
                log.setCreatorName(vo.getName());
                loginLogService.save(log);

                OnlinePool.getInstance().add(vo.getId(), session);
            }
        });

        //写入Cookie
        try {
            response.addCookie(new Cookie("eccrmContext.id", vo.getId()));
            response.addCookie(new Cookie("eccrmContext.employeeName", URLEncoder.encode(URLEncoder.encode(vo.getName(), "utf-8"), "utf-8")));
            if (StringUtils.isNotEmpty(vo.getOrgId())) {
                response.addCookie(new Cookie("eccrmContext.orgId", vo.getOrgId()));
            }
            if (StringUtils.isNotEmpty(vo.getOrgName())) {
                response.addCookie(new Cookie("eccrmContext.orgName", URLEncoder.encode(URLEncoder.encode(vo.getOrgName(), "utf-8"), "utf-8")));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        GsonUtils.printJson(response, "success", true);
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String toLogin() {
        return "redirect:/index.html";
    }
}
