package com.michael.base.parameter.web;

import com.michael.base.parameter.bo.BusinessParamItemBo;
import com.michael.base.parameter.domain.BusinessParamItem;
import com.michael.base.parameter.service.BusinessParamItemService;
import com.michael.base.parameter.service.ParameterContainer;
import com.michael.base.parameter.vo.BusinessParamItemVo;
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
@RequestMapping(value = {"/base/parameter/item/business"})
public class BusinessParamItemCtrl extends BaseController {
    @Resource
    private BusinessParamItemService service;

    @RequestMapping(value = {""}, method = RequestMethod.GET)
    public String toList() {
        return "base/parameter/business/businessParamItem_list";
    }


    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public void save(HttpServletRequest request, HttpServletResponse response) {
        BusinessParamItem sysParamItem = GsonUtils.wrapDataToEntity(request, BusinessParamItem.class);
        service.save(sysParamItem);
        GsonUtils.printSuccess(response);
    }


    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    @ResponseBody
    public void update(HttpServletRequest request, HttpServletResponse response) {
        BusinessParamItem sysParamItem = GsonUtils.wrapDataToEntity(request, BusinessParamItem.class);
        service.update(sysParamItem);
        GsonUtils.printSuccess(response);
    }


    @ResponseBody
    @RequestMapping(value = "/get", params = "id", method = RequestMethod.GET)
    public void findById(@RequestParam String id, HttpServletResponse response) {
        BusinessParamItemVo vo = service.findById(id);
        GsonUtils.printData(response, vo);
    }


    @ResponseBody
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public void query(HttpServletRequest request, HttpServletResponse response) {
        BusinessParamItemBo bo = GsonUtils.wrapDataToEntity(request, BusinessParamItemBo.class);
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
        boolean has = service.hasValue(StringUtils.decodeByUTF8(type), StringUtils.decodeByUTF8(name));
        GsonUtils.printData(response, has);
    }

    /**
     * 根据业务参数类型的编号查询对应的有效的基础参数
     *
     * @param type 业务参数类型的编号
     */
    @ResponseBody
    @RequestMapping(value = "/queryValid", method = RequestMethod.GET, params = "type")
    public void queryValid(@RequestParam String type, HttpServletResponse response) {
        List<BusinessParamItemVo> vos = ParameterContainer.getInstance().getBusinessItems(StringUtils.decodeByUTF8(type));
        GsonUtils.printData(response, vos);
    }

    @ResponseBody
    @RequestMapping(value = "/fetchCascade", params = {"type", "value"}, method = RequestMethod.GET)
    public void queryCascade(@RequestParam String type,
                             @RequestParam String value, HttpServletResponse response) {
        List<BusinessParamItemVo> vos = service.fetchCascade(type, value);
        GsonUtils.printData(response, vos);
    }


    /**
     * 查询指定业务参数类型下被级联的参数
     *
     * @param type 业务参数类型
     */
    @ResponseBody
    @RequestMapping(value = "/queryCascadeItem", params = {"type"}, method = RequestMethod.GET)
    public void fetchCascadeItem(@RequestParam String type, HttpServletResponse response) {
        List<BusinessParamItemVo> vos = service.queryCascadeItem(type);
        GsonUtils.printData(response, vos);
    }


}
