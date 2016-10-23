package com.michael.base.emp.web;

import com.michael.base.emp.service.EmpService;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 退出
 */
@Controller
@Scope("prototype")
public class LogoutCtrl {
    private Logger logger = Logger.getLogger(LogoutCtrl.class);
    @Resource
    private EmpService empService;


    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return "redirect:/main";
    }

}
