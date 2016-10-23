package com.michael.base.log.web;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.michael.base.log.bo.LoginLogBo;
import com.michael.base.log.domain.LoginLog;
import com.michael.base.log.service.LoginLogService;
import com.michael.base.log.vo.LoginLogVo;
import com.michael.common.JspAccessType;
import com.michael.core.pager.PageVo;
import com.michael.core.web.BaseController;
import com.michael.poi.exp.ExportEngine;
import com.michael.utils.gson.DateStringConverter;
import com.michael.utils.gson.GsonUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author Michael
 */
@Controller
@RequestMapping(value = {"/base/log/loginLog"})
public class LoginLogCtrl extends BaseController {
    @Resource
    private LoginLogService loginLogService;


    @RequestMapping(value = {""}, method = RequestMethod.GET)
    public String toList() {
        return "base/log/loginLog/loginLog_list";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String toAdd(HttpServletRequest request) {
        request.setAttribute(JspAccessType.PAGE_TYPE, JspAccessType.ADD);
        return "base/log/loginLog/loginLog_edit";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public void save(HttpServletRequest request, HttpServletResponse response) {
        LoginLog loginLog = GsonUtils.wrapDataToEntity(request, LoginLog.class);
        loginLogService.save(loginLog);
        GsonUtils.printSuccess(response);
    }

    @RequestMapping(value = "/modify", params = {"id"}, method = RequestMethod.GET)
    public String toModify(@RequestParam String id, HttpServletRequest request) {
        request.setAttribute(JspAccessType.PAGE_TYPE, JspAccessType.MODIFY);
        request.setAttribute("id", id);
        return "base/log/loginLog/loginLog_edit";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public void update(HttpServletRequest request, HttpServletResponse response) {
        LoginLog loginLog = GsonUtils.wrapDataToEntity(request, LoginLog.class);
        loginLogService.update(loginLog);
        GsonUtils.printSuccess(response);
    }

    @RequestMapping(value = {"/detail"}, params = {"id"}, method = RequestMethod.GET)
    public String toDetail(@RequestParam String id, HttpServletRequest request) {
        request.setAttribute(JspAccessType.PAGE_TYPE, JspAccessType.DETAIL);
        request.setAttribute("id", id);
        return "base/log/loginLog/loginLog_edit";
    }

    @ResponseBody
    @RequestMapping(value = "/get", params = {"id"}, method = RequestMethod.GET)
    public void findById(@RequestParam String id, HttpServletResponse response) {
        LoginLogVo vo = loginLogService.findById(id);
        GsonUtils.printData(response, vo);
    }

    @ResponseBody
    @RequestMapping(value = "/pageQuery", method = RequestMethod.POST)
    public void pageQuery(HttpServletRequest request, HttpServletResponse response) {
        LoginLogBo bo = GsonUtils.wrapDataToEntity(request, LoginLogBo.class);
        PageVo pageVo = loginLogService.pageQuery(bo);
        GsonUtils.printData(response, pageVo);
    }

    @ResponseBody
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public void query(HttpServletRequest request, HttpServletResponse response) {
        LoginLogBo bo = GsonUtils.wrapDataToEntity(request, LoginLogBo.class);
        List<LoginLogVo> vos = loginLogService.query(bo);
        GsonUtils.printData(response, vos);
    }

    @ResponseBody
    @RequestMapping(value = "/delete", params = {"ids"}, method = RequestMethod.DELETE)
    public void deleteByIds(@RequestParam String ids, HttpServletResponse response) {
        String[] idArr = ids.split(",");
        loginLogService.deleteByIds(idArr);
        GsonUtils.printSuccess(response);
    }

    // 导出数据
    @RequestMapping(value = "/export", method = RequestMethod.GET)
    public String export(HttpServletRequest request, HttpServletResponse response) {
        Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new DateStringConverter("yyyy-MM-dd HH:mm:ss"))
                .create();
        LoginLogBo bo = GsonUtils.wrapDataToEntity(request, LoginLogBo.class);
        List<LoginLogVo> data = loginLogService.query(bo);
        String json = gson.toJson(data);
        JsonElement element = gson.fromJson(json, JsonElement.class);
        JsonObject o = new JsonObject();
        o.add("c", element);
        String disposition = null;//
        try {
            disposition = "attachment;filename=" + URLEncoder.encode("登录日志数据" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".xlsx", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", disposition);
        try {
            InputStream inputStream = LoginLogCtrl.class.getClassLoader().getResourceAsStream("export_loginLog.xlsx");
            Assert.notNull(inputStream, "数据导出失败!模板文件不存在，请与管理员联系!");
            new ExportEngine().export(response.getOutputStream(), inputStream, o);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    // 下载模板
    @ResponseBody
    @RequestMapping(value = "/template", method = RequestMethod.GET)
    public void downloadTemplate(HttpServletResponse response) {
        InputStream input = LoginLogCtrl.class.getClassLoader().getResourceAsStream("import_loginLog.xlsx");
        Assert.notNull(input, "模板下载失败!登录日志数据导入模板不存在!");
        response.setContentType("application/vnd.ms-excel");
        String disposition = null;//
        try {
            disposition = "attachment;filename=" + URLEncoder.encode("登录日志数据导入模板.xlsx", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.setHeader("Content-disposition", disposition);
        try {
            IOUtils.copy(input, response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
