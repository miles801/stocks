package com.michael.stock.db.web;

import com.michael.common.JspAccessType;
import com.michael.core.SystemContainer;
import com.michael.core.pager.PageVo;
import com.michael.core.web.BaseController;
import com.michael.stock.db.bo.DBBo;
import com.michael.stock.db.domain.DB;
import com.michael.stock.db.service.DBService;
import com.michael.stock.db.vo.DBVo;
import com.michael.stock.fn.bo.Fn3Bo;
import com.michael.stock.fn.bo.Fn4Bo;
import com.michael.stock.fn.schedule.FnSchedule;
import com.michael.stock.fn.service.Fn3Service;
import com.michael.stock.fn.service.Fn4Service;
import com.michael.utils.gson.GsonUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author Michael
 */
@Controller
@RequestMapping(value = {"/stock/db/dB"})
public class DBCtrl extends BaseController {
    @Resource
    private DBService dBService;

    @Resource
    private Fn3Service fn3Service;

    @Resource
    private Fn4Service fn4Service;

    @RequestMapping(value = {""}, method = RequestMethod.GET)
    public String toList() {
        return "stock/db/dB/dB_list";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String toAdd(HttpServletRequest request) {
        request.setAttribute(JspAccessType.PAGE_TYPE, JspAccessType.ADD);
        return "stock/db/dB/dB_edit";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public void save(HttpServletRequest request, HttpServletResponse response) {
        DB dB = GsonUtils.wrapDataToEntity(request, DB.class);
        dBService.save(dB);
        GsonUtils.printSuccess(response);
    }

    @RequestMapping(value = "/modify", params = {"id"}, method = RequestMethod.GET)
    public String toModify(@RequestParam String id, HttpServletRequest request) {
        request.setAttribute(JspAccessType.PAGE_TYPE, JspAccessType.MODIFY);
        request.setAttribute("id", id);
        return "stock/db/dB/dB_edit";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public void update(HttpServletRequest request, HttpServletResponse response) {
        DB dB = GsonUtils.wrapDataToEntity(request, DB.class);
        dBService.update(dB);
        GsonUtils.printSuccess(response);
    }

    @RequestMapping(value = {"/detail"}, params = {"id"}, method = RequestMethod.GET)
    public String toDetail(@RequestParam String id, HttpServletRequest request) {
        request.setAttribute(JspAccessType.PAGE_TYPE, JspAccessType.DETAIL);
        request.setAttribute("id", id);
        return "stock/db/dB/dB_edit";
    }

    @ResponseBody
    @RequestMapping(value = "/get", params = {"id"}, method = RequestMethod.GET)
    public void findById(@RequestParam String id, HttpServletResponse response) {
        DBVo vo = dBService.findById(id);
        GsonUtils.printData(response, vo);
    }

    @ResponseBody
    @RequestMapping(value = "/pageQuery", method = RequestMethod.POST)
    public void pageQuery(HttpServletRequest request, HttpServletResponse response) {
        DBBo bo = GsonUtils.wrapDataToEntity(request, DBBo.class);
        PageVo pageVo = dBService.pageQuery(bo);
        GsonUtils.printData(response, pageVo);
    }

    @ResponseBody
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public void query(HttpServletRequest request, HttpServletResponse response) {
        DBBo bo = GsonUtils.wrapDataToEntity(request, DBBo.class);
        List<DBVo> vos = dBService.query(bo);
        GsonUtils.printData(response, vos);
    }

    @ResponseBody
    @RequestMapping(value = "/delete", params = {"ids"}, method = RequestMethod.DELETE)
    public void deleteByIds(@RequestParam String ids, HttpServletResponse response) {
        String[] idArr = ids.split(",");
        dBService.deleteByIds(idArr);
        GsonUtils.printSuccess(response);
    }

    @ResponseBody
    @RequestMapping(value = "/calculate", params = "xtype", method = RequestMethod.POST)
    public void calculate(int xtype, HttpServletRequest request, HttpServletResponse response) {
        PageVo vo = null;
        if (xtype == 3) {
            Fn3Bo bo = GsonUtils.wrapDataToEntity(request, Fn3Bo.class);
            vo = fn3Service.pageQuery(bo);
        } else if (xtype == 4) {
            Fn4Bo bo = GsonUtils.wrapDataToEntity(request, Fn4Bo.class);
            vo = fn4Service.pageQuery(bo);
        }
        GsonUtils.printData(response, vo);
    }


    @ResponseBody
    @RequestMapping(value = "/reset", method = RequestMethod.POST)
    public void reset(HttpServletRequest request, HttpServletResponse response) {
        SystemContainer.getInstance().getBean(FnSchedule.class).resetFn();
        GsonUtils.printSuccess(response);
    }

}
