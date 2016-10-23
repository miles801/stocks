package com.michael.base.parameter.web;

import com.michael.base.parameter.bo.SysParamItemBo;
import com.michael.base.parameter.domain.SysParamItem;
import com.michael.base.parameter.service.ParameterContainer;
import com.michael.base.parameter.service.SysParamItemService;
import com.michael.base.parameter.vo.SysParamItemVo;
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
 * @datetime: 2014-06-20
 */

@Controller
@RequestMapping(value = {"/base/parameter/item/system"})
public class SysParamItemCtrl extends BaseController {
    @Resource
    private SysParamItemService service;

    @RequestMapping(value = {""}, method = RequestMethod.GET)
    public String toList() {
        return "base/parameter/system/sysParamItem_list";
    }


    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public void save(HttpServletRequest request, HttpServletResponse response) {
        SysParamItem sysParamItem = GsonUtils.wrapDataToEntity(request, SysParamItem.class);
        service.save(sysParamItem);
        GsonUtils.printSuccess(response);
    }


    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    @ResponseBody
    public void update(HttpServletRequest request, HttpServletResponse response) {
        SysParamItem sysParamItem = GsonUtils.wrapDataToEntity(request, SysParamItem.class);
        service.update(sysParamItem);
        GsonUtils.printSuccess(response);
    }


    @ResponseBody
    @RequestMapping(value = "/get", params = {"id"}, method = RequestMethod.GET)
    public void findById(@RequestParam String id, HttpServletResponse response) {
        SysParamItemVo vo = service.findById(id);
        GsonUtils.printData(response, vo);
    }


    @ResponseBody
    @RequestMapping(value = "/query")
    public void query(HttpServletRequest request, HttpServletResponse response) {
        SysParamItemBo bo = GsonUtils.wrapDataToEntity(request, SysParamItemBo.class);
        PageVo pageVo = service.query(bo);
        GsonUtils.printData(response, pageVo);
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
    @RequestMapping(value = "/hasValue", params = {"type", "value"}, method = RequestMethod.GET)
    public void hasCode(@RequestParam String type,
                        @RequestParam String value, HttpServletResponse response) {
        boolean has = service.hasValue(StringUtils.decodeByUTF8(type), StringUtils.decodeByUTF8(value));
        GsonUtils.printData(response, has);
    }

    @ResponseBody
    @RequestMapping(value = "/hasName", params = {"type", "name"}, method = RequestMethod.GET)
    public void hasName(@RequestParam String type,
                        @RequestParam String name, HttpServletResponse response) {
        boolean has = service.hasName(StringUtils.decodeByUTF8(type), StringUtils.decodeByUTF8(name));
        GsonUtils.printData(response, has);
    }

    /**
     * 根据基础参数类型的编号查询有效的基础参数
     *
     * @param type 基础参数类型的编号
     */
    @ResponseBody
    @RequestMapping(value = "/queryValid", method = RequestMethod.GET, params = "type")
    public void queryValid(@RequestParam String type, HttpServletResponse response) {
        List<SysParamItemVo> vos = ParameterContainer.getInstance().getSystemItems(StringUtils.decodeByUTF8(type));
        GsonUtils.printData(response, vos);
    }

    @ResponseBody
    @RequestMapping(value = "/fetchCascade", params = {"type", "value"}, method = RequestMethod.GET)
    public void queryCascade(@RequestParam String type,
                             @RequestParam String value, HttpServletResponse response) {
        List<SysParamItemVo> vos = service.fetchCascade(type, value);
        GsonUtils.printData(response, vos);
    }

}
