package com.michael.base.org.web;

import com.michael.base.emp.bo.EmpBo;
import com.michael.base.emp.service.EmpService;
import com.michael.base.org.bo.OrgBo;
import com.michael.base.org.domain.Org;
import com.michael.base.org.service.OrgEmpService;
import com.michael.base.org.service.OrgService;
import com.michael.base.org.vo.OrgDTO;
import com.michael.base.org.vo.OrgVo;
import com.michael.common.JspAccessType;
import com.michael.core.pager.PageVo;
import com.michael.core.web.BaseController;
import com.michael.docs.annotations.ApiParam;
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
@RequestMapping(value = {"/base/org"})
public class OrgCtrl extends BaseController {
    @Resource
    private OrgService orgService;

    @Resource
    private OrgEmpService orgEmpService;

    @Resource
    private EmpService empService;

    @RequestMapping(value = {""}, method = RequestMethod.GET)
    public String toList() {
        return "base/org/list/org_list";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String toAdd(HttpServletRequest request) {
        request.setAttribute(JspAccessType.PAGE_TYPE, JspAccessType.ADD);
        return "base/org/edit/org_edit";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public void save(HttpServletRequest request, HttpServletResponse response) {
        Org org = GsonUtils.wrapDataToEntity(request, Org.class);
        orgService.save(org);
        GsonUtils.printSuccess(response);
    }

    @RequestMapping(value = "/modify", params = {"id"}, method = RequestMethod.GET)
    public String toModify(@RequestParam String id, HttpServletRequest request) {
        request.setAttribute(JspAccessType.PAGE_TYPE, JspAccessType.MODIFY);
        request.setAttribute("id", id);
        return "base/org/edit/org_edit";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public void update(HttpServletRequest request, HttpServletResponse response) {
        Org org = GsonUtils.wrapDataToEntity(request, Org.class);
        orgService.update(org);
        GsonUtils.printSuccess(response);
    }

    @RequestMapping(value = {"/detail"}, params = {"id"}, method = RequestMethod.GET)
    public String toDetail(@RequestParam String id, HttpServletRequest request) {
        request.setAttribute(JspAccessType.PAGE_TYPE, JspAccessType.DETAIL);
        request.setAttribute("id", id);
        return "base/org/edit/org_edit";
    }

    @ResponseBody
    @RequestMapping(value = "/get", params = {"id"}, method = RequestMethod.GET)
    public void findById(@RequestParam String id, HttpServletResponse response) {
        OrgVo vo = orgService.findById(id);
        GsonUtils.printData(response, vo);
    }

    @ResponseBody
    @RequestMapping(value = "/pageQuery", method = RequestMethod.POST)
    public void pageQuery(HttpServletRequest request, HttpServletResponse response) {
        OrgBo bo = GsonUtils.wrapDataToEntity(request, OrgBo.class);
        PageVo pageVo = orgService.pageQuery(bo);
        GsonUtils.printData(response, pageVo);
    }

    /**
     * 一次性加载组织机构的所有有效数据
     */
    @ResponseBody
    @RequestMapping(value = "/tree", method = RequestMethod.POST)
    public void tree(HttpServletRequest request, HttpServletResponse response) {
        OrgBo bo = GsonUtils.wrapDataToEntity(request, OrgBo.class);
        List<OrgVo> data = orgService.tree(bo);
        GsonUtils.printData(response, data);
    }

    /**
     * 查询所有的子组织机构
     *
     * @param id ID 可选
     */
    @ResponseBody
    @RequestMapping(value = "/children", method = RequestMethod.GET)
    public void children(@RequestParam(required = false) String id, HttpServletRequest request, HttpServletResponse response) {
        List<OrgVo> data = orgService.children(id);
        GsonUtils.printData(response, data);
    }


    @ResponseBody
    @RequestMapping(value = "/childrenAndEmp", method = RequestMethod.GET)
    public void childrenAndEmp(@RequestParam(required = false) String id, HttpServletRequest request, HttpServletResponse response) {
        OrgDTO data = orgService.childrenAndEmp(id);
        GsonUtils.printData(response, data);
    }

    /**
     * 组织机构注销
     *
     * @param ids ID列表，多个值使用逗号分隔
     */
    @ResponseBody
    @RequestMapping(value = {"/delete", "/disable"}, params = {"ids"}, method = RequestMethod.DELETE)
    public void deleteByIds(@RequestParam String ids, HttpServletResponse response) {
        String[] idArr = ids.split(",");
        orgService.deleteByIds(idArr);
        GsonUtils.printSuccess(response);
    }

    /**
     * 启用组织机构
     *
     * @param ids ID列表，多个值使用逗号分隔
     */
    @ResponseBody
    @RequestMapping(value = {"/enable"}, params = {"ids"}, method = RequestMethod.POST)
    public void enable(@RequestParam @ApiParam(name = "ids", desc = "多个值使用逗号进行分隔") String ids,
                       HttpServletResponse response) {
        String[] idArr = ids.split(",");
        orgService.enable(idArr);
        GsonUtils.printSuccess(response);
    }

    /**
     * 添加员工
     *
     * @param orgId  机构ID
     * @param empIds 员工ID列表，多个值使用逗号分隔
     */
    @ResponseBody
    @RequestMapping(value = "/emp-add", params = {"orgId", "empIds"}, method = RequestMethod.POST)
    public void addEmp(@RequestParam String orgId, @RequestParam String empIds, HttpServletResponse response) {
        orgEmpService.addEmp(orgId, empIds.split(","));
        GsonUtils.printSuccess(response);
    }

    /**
     * 移除员工
     *
     * @param orgId  机构ID
     * @param empIds 员工ID列表，多个值使用逗号分隔
     */
    @ResponseBody
    @RequestMapping(value = "/emp-remove", params = {"orgId", "empIds"}, method = RequestMethod.POST)
    public void removeEmp(@RequestParam String orgId, @RequestParam String empIds, HttpServletResponse response) {
        orgEmpService.removeEmp(orgId, empIds.split(","));
        GsonUtils.printSuccess(response);
    }

    /**
     * 查询指定机构下的所有员工(可以附加员工的过滤条件)
     *
     * @param orgId 机构ID
     */
    @ResponseBody
    @RequestMapping(value = "/emp-query", params = {"orgId"}, method = RequestMethod.POST)
    public void addEmp(@RequestParam String orgId, HttpServletRequest request, HttpServletResponse response) {
        EmpBo bo = GsonUtils.wrapDataToEntity(request, EmpBo.class);
        PageVo data = empService.queryByOrg(orgId, bo);
        GsonUtils.printData(response, data);
    }

}
