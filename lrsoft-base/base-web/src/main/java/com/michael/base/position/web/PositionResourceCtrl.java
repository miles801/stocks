package com.michael.base.position.web;

import com.michael.base.position.bo.PositionResourceBo;
import com.michael.base.position.domain.PositionResource;
import com.michael.base.position.service.PositionResourceService;
import com.michael.base.position.vo.PositionResourceVo;
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
@RequestMapping(value = {"/base/positionResource"})
public class PositionResourceCtrl extends BaseController {
    @Resource
    private PositionResourceService positionResourceService;

    @RequestMapping(value = {""}, method = RequestMethod.GET)
    public String toList() {
        return "base/positionResource/list/positionResource_list";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String toAdd(HttpServletRequest request) {
        request.setAttribute(JspAccessType.PAGE_TYPE, JspAccessType.ADD);
        return "base/positionResource/edit/positionResource_edit";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public void save(HttpServletRequest request, HttpServletResponse response) {
        PositionResource positionResource = GsonUtils.wrapDataToEntity(request, PositionResource.class);
        positionResourceService.save(positionResource);
        GsonUtils.printSuccess(response);
    }

    @RequestMapping(value = "/modify", params = {"id"}, method = RequestMethod.GET)
    public String toModify(@RequestParam String id, HttpServletRequest request) {
        request.setAttribute(JspAccessType.PAGE_TYPE, JspAccessType.MODIFY);
        request.setAttribute("id", id);
        return "base/positionResource/edit/positionResource_edit";
    }


    @RequestMapping(value = {"/detail"}, params = {"id"}, method = RequestMethod.GET)
    public String toDetail(@RequestParam String id, HttpServletRequest request) {
        request.setAttribute(JspAccessType.PAGE_TYPE, JspAccessType.DETAIL);
        request.setAttribute("id", id);
        return "base/positionResource/edit/positionResource_edit";
    }

    @ResponseBody
    @RequestMapping(value = "/get", params = {"id"}, method = RequestMethod.GET)
    public void findById(@RequestParam String id, HttpServletResponse response) {
        PositionResourceVo vo = positionResourceService.findById(id);
        GsonUtils.printData(response, vo);
    }

    @ResponseBody
    @RequestMapping(value = "/pageQuery", method = RequestMethod.POST)
    public void pageQuery(HttpServletRequest request, HttpServletResponse response) {
        PositionResourceBo bo = GsonUtils.wrapDataToEntity(request, PositionResourceBo.class);
        PageVo pageVo = positionResourceService.pageQuery(bo);
        GsonUtils.printData(response, pageVo);
    }

    @ResponseBody
    @RequestMapping(value = "/delete", params = {"ids"}, method = RequestMethod.DELETE)
    public void deleteByIds(@RequestParam String ids, HttpServletResponse response) {
        String[] idArr = ids.split(",");
        positionResourceService.deleteByIds(idArr);
        GsonUtils.printSuccess(response);
    }

}
