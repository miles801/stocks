package com.michael.base.parameter.web;

import com.michael.base.parameter.bo.BusinessParamTypeBo;
import com.michael.base.parameter.domain.BusinessParamType;
import com.michael.base.parameter.service.BusinessParamTypeService;
import com.michael.base.parameter.vo.BusinessParamTypeVo;
import com.michael.core.pager.PageVo;
import com.michael.core.web.BaseController;
import com.michael.utils.gson.GsonUtils;
import com.michael.utils.string.StringUtils;
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
 * @author: miles
 * @datetime: 2014-07-02
 */

@Controller
@RequestMapping(value = {"/base/parameter/type/business"})
public class BusinessParamTypeCtrl extends BaseController {
    @Resource
    private BusinessParamTypeService service;

    @RequestMapping(value = {""}, method = RequestMethod.GET)
    public String toList() {
        return "base/parameter/business/businessParamType_list";
    }


    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public void save(HttpServletRequest request, HttpServletResponse response) {
        BusinessParamType sysParamType = GsonUtils.wrapDataToEntity(request, BusinessParamType.class);
        service.save(sysParamType);
        GsonUtils.printSuccess(response);
    }


    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public void update(HttpServletRequest request, HttpServletResponse response) {
        BusinessParamType sysParamType = GsonUtils.wrapDataToEntity(request, BusinessParamType.class);
        service.update(sysParamType);
        GsonUtils.printSuccess(response);
    }


    @ResponseBody
    @RequestMapping(value = "/get", params = {"id"}, method = RequestMethod.GET)
    public void findById(@RequestParam String id, HttpServletResponse response) {
        BusinessParamTypeVo vo = service.findById(id);
        GsonUtils.printData(response, vo);
    }


    @ResponseBody
    @RequestMapping(value = "/query")
    public void query(HttpServletRequest request, HttpServletResponse response) {
        BusinessParamTypeBo bo = GsonUtils.wrapDataToEntity(request, BusinessParamTypeBo.class);
        PageVo pageVo = service.query(bo);
        GsonUtils.printData(response, pageVo);
    }

    @ResponseBody
    @RequestMapping(value = "/queryUsing", method = RequestMethod.GET)
    public void queryUsing(HttpServletResponse response) {
        List<BusinessParamTypeVo> vos = service.queryUsingTree();
        GsonUtils.printData(response, vos);
    }

    @ResponseBody
    @RequestMapping(value = "/queryValid", method = RequestMethod.GET)
    public void queryValid(HttpServletResponse response) {
        List<BusinessParamTypeVo> vos = service.queryValidTree();
        GsonUtils.printData(response, vos);
    }

    @ResponseBody
    @RequestMapping(value = "/tree", method = RequestMethod.GET)
    public void allForTree(HttpServletRequest request, HttpServletResponse response) {
        List<BusinessParamTypeVo> tree = service.allForTree();
        GsonUtils.printData(response, tree);
    }

    @ResponseBody
    @RequestMapping(value = "/children", params = {"id"}, method = RequestMethod.GET)
    public void children(@RequestParam String id, HttpServletResponse response) {
        List<BusinessParamTypeVo> tree = service.queryChildren(id, true);
        GsonUtils.printData(response, tree);
    }

    @ResponseBody
    @RequestMapping(value = "/delete", params = {"ids"}, method = RequestMethod.DELETE)
    public void deleteByIds(@RequestParam String ids, HttpServletResponse response) {
        String[] idArr = ids.split(",");
        service.deleteByIds(idArr);
        GsonUtils.printSuccess(response);
    }

    @ResponseBody
    @RequestMapping(value = "/enable", params = {"ids"}, method = RequestMethod.POST)
    public void enable(@RequestParam String ids, HttpServletResponse response) {
        String[] idArr = ids.split(",");
        service.enable(idArr);
        GsonUtils.printSuccess(response);
    }

    @ResponseBody
    @RequestMapping(value = "/disable", params = {"ids"}, method = RequestMethod.POST)
    public void disable(@RequestParam String ids, HttpServletResponse response) {
        String[] idArr = ids.split(",");
        service.disable(idArr);
        GsonUtils.printSuccess(response);
    }


    @ResponseBody
    @RequestMapping(value = "/hasCode", method = RequestMethod.GET, params = "code")
    public void hasCode(@RequestParam String code, HttpServletResponse response) {
        boolean has = service.hasCode(StringUtils.decodeByUTF8(code));
        GsonUtils.printData(response, has);
    }

    /**
     * 查询指定层级下的名称是否重复
     * 必须参数：name
     * 可选参数：id
     */
    @ResponseBody
    @RequestMapping(value = "/hasName", params = {"name"}, method = RequestMethod.GET)
    public void hasName(@RequestParam(required = false) String id,
                        @RequestParam String name, HttpServletResponse response) {
        boolean has = service.hasName(id, StringUtils.decodeByUTF8(name));
        GsonUtils.printData(response, has);
    }

    @ResponseBody
    @RequestMapping(value = "/other", method = RequestMethod.GET)
    public void queryOther(HttpServletRequest request, HttpServletResponse response) {
        String cid = request.getParameter("cid");
        List<BusinessParamTypeVo> vos = service.queryOther(cid);
        GsonUtils.printData(response, vos);
    }


    /**
     * 根据业务参数类型的编号获取对应的名称
     *
     * @param code 业务参数类型的编号
     */
    @ResponseBody
    @RequestMapping(value = "/name", params = "code", method = RequestMethod.GET)
    public void getSystemName(@RequestParam String code, HttpServletResponse response) {
        String name = service.getName(StringUtils.decodeByUTF8(code));
        GsonUtils.printData(response, name);
    }
}
