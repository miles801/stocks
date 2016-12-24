package com.michael.stock.fn.web;

import com.michael.common.JspAccessType;
import com.michael.core.pager.PageVo;
import com.michael.core.web.BaseController;
import com.michael.stock.fn.bo.Fn4Bo;
import com.michael.stock.fn.domain.Fn4;
import com.michael.stock.fn.service.Fn4Service;
import com.michael.stock.fn.service.Handle;
import com.michael.stock.fn.vo.Fn4Vo;
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
@RequestMapping(value = {"/stock/fn/fn4"})
public class Fn4Ctrl extends BaseController {
    @Resource
    private Fn4Service fn4Service;


    @RequestMapping(value = {""}, method = RequestMethod.GET)
    public String toList() {
        return "stock/fn/fn4/fn4_list";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String toAdd(HttpServletRequest request) {
        request.setAttribute(JspAccessType.PAGE_TYPE, JspAccessType.ADD);
        return "stock/fn/fn4/fn4_edit";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public void save(HttpServletRequest request, HttpServletResponse response) {
        Fn4 fn4 = GsonUtils.wrapDataToEntity(request, Fn4.class);
        fn4Service.save(fn4);
        GsonUtils.printSuccess(response);
    }

    @RequestMapping(value = "/modify", params = {"id"}, method = RequestMethod.GET)
    public String toModify(@RequestParam String id, HttpServletRequest request) {
        request.setAttribute(JspAccessType.PAGE_TYPE, JspAccessType.MODIFY);
        request.setAttribute("id", id);
        return "stock/fn/fn4/fn4_edit";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public void update(HttpServletRequest request, HttpServletResponse response) {
        Fn4 fn4 = GsonUtils.wrapDataToEntity(request, Fn4.class);
        fn4Service.update(fn4);
        GsonUtils.printSuccess(response);
    }

    @RequestMapping(value = {"/detail"}, params = {"id"}, method = RequestMethod.GET)
    public String toDetail(@RequestParam String id, HttpServletRequest request) {
        request.setAttribute(JspAccessType.PAGE_TYPE, JspAccessType.DETAIL);
        request.setAttribute("id", id);
        return "stock/fn/fn4/fn4_edit";
    }

    @ResponseBody
    @RequestMapping(value = "/get", params = {"id"}, method = RequestMethod.GET)
    public void findById(@RequestParam String id, HttpServletResponse response) {
        Fn4Vo vo = fn4Service.findById(id);
        GsonUtils.printData(response, vo);
    }

    @ResponseBody
    @RequestMapping(value = "/pageQuery", method = RequestMethod.POST)
    public void pageQuery(HttpServletRequest request, HttpServletResponse response) {
        Fn4Bo bo = GsonUtils.wrapDataToEntity(request, Fn4Bo.class);
        PageVo pageVo = fn4Service.pageQuery(bo);
        GsonUtils.printData(response, pageVo);
    }

    @ResponseBody
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public void query(HttpServletRequest request, HttpServletResponse response) {
        Fn4Bo bo = GsonUtils.wrapDataToEntity(request, Fn4Bo.class);
        List<Fn4Vo> vos = fn4Service.query(bo);
        GsonUtils.printData(response, vos);
    }

    @ResponseBody
    @RequestMapping(value = "/delete", params = {"ids"}, method = RequestMethod.DELETE)
    public void deleteByIds(@RequestParam String ids, HttpServletResponse response) {
        String[] idArr = ids.split(",");
        fn4Service.deleteByIds(idArr);
        GsonUtils.printSuccess(response);
    }


    @ResponseBody
    @RequestMapping(value = "/reset", method = RequestMethod.POST)
    public void reset(HttpServletRequest request, HttpServletResponse response) {
        Fn4Bo bo = GsonUtils.wrapDataToEntity(request, Fn4Bo.class);
        fn4Service.reset(bo);
        GsonUtils.printSuccess(response);
    }

    @ResponseBody
    @RequestMapping(value = "/lastHandle", params = "type", method = RequestMethod.GET)
    public void lastHandle(int type, HttpServletRequest request, HttpServletResponse response) {
        Handle handle = fn4Service.lastHandle(type);
        GsonUtils.printData(response, handle);
    }


}
