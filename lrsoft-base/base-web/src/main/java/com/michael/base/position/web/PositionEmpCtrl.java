package com.michael.base.position.web;

import com.michael.base.position.bo.PositionEmpBo;
import com.michael.base.position.domain.PositionEmp;
import com.michael.base.position.service.PositionEmpService;
import com.michael.base.position.vo.PositionEmpVo;
import com.michael.common.JspAccessType;
import com.michael.core.pager.PageVo;
import com.michael.core.web.BaseController;
import com.michael.utils.gson.GsonUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Michael
 */
@Controller
@RequestMapping(value = {"/base/position/emp"})
public class PositionEmpCtrl extends BaseController {
    @Resource
    private PositionEmpService positionEmpService;

    @RequestMapping(value = {""}, method = RequestMethod.GET)
    public String toList() {
        return "base/positionEmp/list/positionEmp_list";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String toAdd(HttpServletRequest request) {
        request.setAttribute(JspAccessType.PAGE_TYPE, JspAccessType.ADD);
        return "base/positionEmp/edit/positionEmp_edit";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public void save(HttpServletRequest request, HttpServletResponse response) {
        PositionEmp positionEmp = GsonUtils.wrapDataToEntity(request, PositionEmp.class);
        positionEmpService.save(positionEmp);
        GsonUtils.printSuccess(response);
    }

    @RequestMapping(value = "/modify", params = {"id"}, method = RequestMethod.GET)
    public String toModify(@RequestParam String id, HttpServletRequest request) {
        request.setAttribute(JspAccessType.PAGE_TYPE, JspAccessType.MODIFY);
        request.setAttribute("id", id);
        return "base/positionEmp/edit/positionEmp_edit";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public void update(HttpServletRequest request, HttpServletResponse response) {
        PositionEmp positionEmp = GsonUtils.wrapDataToEntity(request, PositionEmp.class);
        positionEmpService.update(positionEmp);
        GsonUtils.printSuccess(response);
    }

    @RequestMapping(value = {"/detail"}, params = {"id"}, method = RequestMethod.GET)
    public String toDetail(@RequestParam String id, HttpServletRequest request) {
        request.setAttribute(JspAccessType.PAGE_TYPE, JspAccessType.DETAIL);
        request.setAttribute("id", id);
        return "base/positionEmp/edit/positionEmp_edit";
    }

    @ResponseBody
    @RequestMapping(value = "/get", params = {"id"}, method = RequestMethod.GET)
    public void findById(@RequestParam String id, HttpServletResponse response) {
        PositionEmpVo vo = positionEmpService.findById(id);
        GsonUtils.printData(response, vo);
    }

    @ResponseBody
    @RequestMapping(value = "/pageQuery", method = RequestMethod.POST)
    public void pageQuery(HttpServletRequest request, HttpServletResponse response) {
        PositionEmpBo bo = GsonUtils.wrapDataToEntity(request, PositionEmpBo.class);
        PageVo pageVo = positionEmpService.pageQuery(bo);
        GsonUtils.printData(response, pageVo);
    }

    @ResponseBody
    @RequestMapping(value = "/delete", params = {"ids"}, method = RequestMethod.DELETE)
    public void deleteByIds(@RequestParam String ids, HttpServletResponse response) {
        String[] idArr = ids.split(",");
        positionEmpService.deleteByIds(idArr);
        GsonUtils.printSuccess(response);
    }

}
