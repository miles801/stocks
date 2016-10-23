package com.michael.base.emp.web;

import com.michael.base.emp.bo.EmpBo;
import com.michael.base.emp.domain.Emp;
import com.michael.base.emp.service.EmpService;
import com.michael.base.emp.vo.EmpVo;
import com.michael.common.JspAccessType;
import com.michael.core.pager.PageVo;
import com.michael.core.web.BaseController;
import com.michael.docs.annotations.Api;
import com.michael.docs.annotations.ApiOperate;
import com.michael.docs.annotations.ApiParam;
import com.michael.pinyin.SimplePinYin;
import com.michael.pinyin.StandardStrategy;
import com.michael.utils.gson.GsonUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
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
@RequestMapping(value = {"/base/emp"})
@Api("员工")
public class EmpCtrl extends BaseController {
    @Resource
    private EmpService empService;

    @RequestMapping(value = {""}, method = RequestMethod.GET)
    public String toList() {
        return "base/emp/list/emp_list";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String toAdd(HttpServletRequest request) {
        request.setAttribute(JspAccessType.PAGE_TYPE, JspAccessType.ADD);
        return "base/emp/edit/emp_edit";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public void save(HttpServletRequest request, HttpServletResponse response) {
        Emp emp = GsonUtils.wrapDataToEntity(request, Emp.class);
        empService.save(emp);
        GsonUtils.printSuccess(response);
    }

    @RequestMapping(value = "/modify", params = {"id"}, method = RequestMethod.GET)
    public String toModify(@RequestParam String id, HttpServletRequest request) {
        request.setAttribute(JspAccessType.PAGE_TYPE, JspAccessType.MODIFY);
        request.setAttribute("id", id);
        return "base/emp/edit/emp_edit";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public void update(HttpServletRequest request, HttpServletResponse response) {
        Emp emp = GsonUtils.wrapDataToEntity(request, Emp.class);
        empService.update(emp);
        GsonUtils.printSuccess(response);
    }

    @RequestMapping(value = {"/detail"}, params = {"id"}, method = RequestMethod.GET)
    public String toDetail(@RequestParam String id, HttpServletRequest request) {
        request.setAttribute(JspAccessType.PAGE_TYPE, JspAccessType.DETAIL);
        request.setAttribute("id", id);
        return "base/emp/edit/emp_edit";
    }

    @ApiOperate(value = "查看员工明细", response = Emp.class)
    @ResponseBody
    @RequestMapping(value = "/get", params = {"id"}, method = RequestMethod.GET)
    public void findById(@RequestParam
                         @ApiParam(name = "id", value = "员工ID", required = true) String id, HttpServletResponse response) {
        EmpVo vo = empService.findById(id);
        GsonUtils.printData(response, vo);
    }

    @ApiOperate(value = "员工查询（分页）", request = EmpBo.class, response = EmpVo.class)
    @ResponseBody
    @RequestMapping(value = "/pageQuery", method = RequestMethod.POST)
    public void pageQuery(HttpServletRequest request, HttpServletResponse response) {
        EmpBo bo = GsonUtils.wrapDataToEntity(request, EmpBo.class);
        PageVo pageVo = empService.pageQuery(bo);
        GsonUtils.printData(response, pageVo);
    }


    @ApiOperate(value = "修改密码", request = PwdTemp.class)
    @ResponseBody
    @RequestMapping(value = "/update-pwd", method = RequestMethod.POST)
    public void updatePwd(HttpServletRequest request,
                          HttpServletResponse response) {
        PwdTemp temp = GsonUtils.wrapDataToEntity(request, PwdTemp.class);
        Assert.notNull(temp, "修改密码失败!没有获取到相关的请求信息!");
        empService.updatePwd(temp.getOldPwd(), temp.getNewPwd());
        GsonUtils.printSuccess(response);
    }


    @ResponseBody
    @RequestMapping(value = "/delete", params = {"ids"}, method = RequestMethod.DELETE)
    public void deleteByIds(@RequestParam String ids, HttpServletResponse response) {
        String[] idArr = ids.split(",");
        empService.deleteByIds(idArr);
        GsonUtils.printSuccess(response);
    }

    @ApiOperate(value = "启用员工")
    @ResponseBody
    @RequestMapping(value = "/start", params = {"ids"}, method = RequestMethod.POST)
    public void start(@RequestParam @ApiParam(name = "ids", value = "员工ID", desc = "多个值使用逗号分隔") String ids,
                      HttpServletResponse response) {
        String[] idArr = ids.split(",");
        empService.start(idArr);
        GsonUtils.printSuccess(response);
    }

    /**
     * 将指定的字符转换成拼音返回
     */
    @ResponseBody
    @RequestMapping(value = "/pinyin", params = "name", method = RequestMethod.POST)
    public void pinyin(String name, HttpServletResponse response) {
        String pinyin = new SimplePinYin().toPinYin(name, new StandardStrategy());
        GsonUtils.printData(response, pinyin);
    }
}
