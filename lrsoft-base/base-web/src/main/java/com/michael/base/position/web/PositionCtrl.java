package com.michael.base.position.web;

import com.michael.base.emp.bo.EmpBo;
import com.michael.base.emp.service.EmpService;
import com.michael.base.position.bo.PositionBo;
import com.michael.base.position.domain.Position;
import com.michael.base.position.service.PositionEmpService;
import com.michael.base.position.service.PositionService;
import com.michael.base.position.vo.PositionVo;
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
import java.util.List;

/**
 * @author Michael
 */
@Controller
@RequestMapping(value = {"/base/position"})
public class PositionCtrl extends BaseController {
    @Resource
    private PositionService positionService;

    @Resource
    private PositionEmpService positionEmpService;

    @Resource
    private EmpService empService;

    @RequestMapping(value = {""}, method = RequestMethod.GET)
    public String toList() {
        return "base/position/list/position_list";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String toAdd(HttpServletRequest request) {
        request.setAttribute(JspAccessType.PAGE_TYPE, JspAccessType.ADD);
        return "base/position/edit/position_edit";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public void save(HttpServletRequest request, HttpServletResponse response) {
        Position position = GsonUtils.wrapDataToEntity(request, Position.class);
        positionService.save(position);
        GsonUtils.printSuccess(response);
    }

    @RequestMapping(value = "/modify", params = {"id"}, method = RequestMethod.GET)
    public String toModify(@RequestParam String id, HttpServletRequest request) {
        request.setAttribute(JspAccessType.PAGE_TYPE, JspAccessType.MODIFY);
        request.setAttribute("id", id);
        return "base/position/edit/position_edit";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public void update(HttpServletRequest request, HttpServletResponse response) {
        Position position = GsonUtils.wrapDataToEntity(request, Position.class);
        positionService.update(position);
        GsonUtils.printSuccess(response);
    }

    @RequestMapping(value = {"/detail"}, params = {"id"}, method = RequestMethod.GET)
    public String toDetail(@RequestParam String id, HttpServletRequest request) {
        request.setAttribute(JspAccessType.PAGE_TYPE, JspAccessType.DETAIL);
        request.setAttribute("id", id);
        return "base/position/edit/position_edit";
    }

    @ResponseBody
    @RequestMapping(value = "/get", params = {"id"}, method = RequestMethod.GET)
    public void findById(@RequestParam String id, HttpServletResponse response) {
        PositionVo vo = positionService.findById(id);
        GsonUtils.printData(response, vo);
    }


    @ResponseBody
    @RequestMapping(value = "/tree", method = RequestMethod.GET)
    public void tree(HttpServletRequest request, HttpServletResponse response) {
        List<PositionVo> data = positionService.tree();
        GsonUtils.printData(response, data);
    }

    @ResponseBody
    @RequestMapping(value = "/tree-valid", method = RequestMethod.GET)
    public void validTree(HttpServletRequest request, HttpServletResponse response) {
        List<PositionVo> data = positionService.validTree();
        GsonUtils.printData(response, data);
    }

    @ResponseBody
    @RequestMapping(value = "/pageQuery", method = RequestMethod.POST)
    public void pageQuery(HttpServletRequest request, HttpServletResponse response) {
        PositionBo bo = GsonUtils.wrapDataToEntity(request, PositionBo.class);
        PageVo pageVo = positionService.pageQuery(bo);
        GsonUtils.printData(response, pageVo);
    }


    @ResponseBody
    @RequestMapping(value = "/disable", params = {"ids"}, method = RequestMethod.POST)
    public void disable(@RequestParam String ids, HttpServletResponse response) {
        String[] idArr = ids.split(",");
        positionService.disable(idArr);
        GsonUtils.printSuccess(response);
    }

    @ResponseBody
    @RequestMapping(value = "/enable", params = {"ids"}, method = RequestMethod.POST)
    public void enable(@RequestParam String ids, HttpServletResponse response) {
        String[] idArr = ids.split(",");
        positionService.enable(idArr);
        GsonUtils.printSuccess(response);
    }


    /**
     * 添加员工
     *
     * @param positionId 岗位ID
     * @param empIds     员工ID列表，多个值使用逗号分隔
     */
    @ResponseBody
    @RequestMapping(value = "/emp-add", params = {"positionId", "empIds"}, method = RequestMethod.POST)
    public void addEmp(@RequestParam String positionId, @RequestParam String empIds, HttpServletResponse response) {
        positionEmpService.addEmp(positionId, empIds.split(","));
        GsonUtils.printSuccess(response);
    }

    /**
     * 移除员工
     *
     * @param positionId 机构ID
     * @param empIds     员工ID列表，多个值使用逗号分隔
     */
    @ResponseBody
    @RequestMapping(value = "/emp-remove", params = {"positionId", "empIds"}, method = RequestMethod.POST)
    public void removeEmp(@RequestParam String positionId, @RequestParam String empIds, HttpServletResponse response) {
        positionEmpService.removeEmp(positionId, empIds.split(","));
        GsonUtils.printSuccess(response);
    }

    /**
     * 查询指定机构下的所有员工(可以附加员工的过滤条件)
     *
     * @param positionId 机构ID
     */
    @ResponseBody
    @RequestMapping(value = "/emp-query", params = {"positionId"}, method = RequestMethod.POST)
    public void queryEmp(@RequestParam String positionId, HttpServletRequest request, HttpServletResponse response) {
        EmpBo bo = GsonUtils.wrapDataToEntity(request, EmpBo.class);
        PageVo data = empService.queryByPosition(positionId, bo);
        GsonUtils.printData(response, data);
    }
}
