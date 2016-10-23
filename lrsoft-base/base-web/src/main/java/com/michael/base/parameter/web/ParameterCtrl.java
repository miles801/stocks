package com.michael.base.parameter.web;

import com.michael.base.parameter.service.BusinessParamTypeService;
import com.michael.base.parameter.service.ParameterContainer;
import com.michael.base.parameter.service.SysParamTypeService;
import com.michael.base.parameter.vo.BusinessParamItemVo;
import com.michael.base.parameter.vo.SysParamItemVo;
import com.michael.utils.gson.GsonUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 提供参数的快捷访问接口
 * Created by Michael on 2014/9/4.
 */

@Controller
@Scope("prototype")
@RequestMapping("/base/parameter")
public class ParameterCtrl {

    @Resource
    private SysParamTypeService sysParamTypeService;
    @Resource
    private BusinessParamTypeService businessParamTypeService;

    /**
     * 根据系统参数类型的编号查询对应的有效的系统参数
     *
     * @param type 基础参数类型的编号
     */
    @ResponseBody
    @RequestMapping(value = "/systemItems", params = "type", method = RequestMethod.GET)
    public void fetchSystemItems(@RequestParam String type, HttpServletResponse response) {
        ParameterContainer container = ParameterContainer.getInstance();
        List<SysParamItemVo> vos = container.getSystemItems(type);
        GsonUtils.printData(response, vos);
    }


    /**
     * 根据业务参数类型的编号，查询对应的有效的业务参数
     *
     * @param type 业务参数类型的编号
     */
    @ResponseBody
    @RequestMapping(value = "/businessItems", params = "type", method = RequestMethod.GET)
    public void fetchBusinessItems(@RequestParam String type, HttpServletResponse response) {
        ParameterContainer container = ParameterContainer.getInstance();
        List<BusinessParamItemVo> vos = container.getBusinessItems(type);
        GsonUtils.printData(response, vos);
    }

    /**
     * 根据系统参数类型的编号获取对应的名称
     *
     * @param code 基础参数类型的编号
     */
    @ResponseBody
    @RequestMapping(value = "/systemName", params = "code", method = RequestMethod.GET)
    public void getSystemName(@RequestParam String code, HttpServletResponse response) {
        String name = sysParamTypeService.getName(code);
        GsonUtils.printData(response, name);
    }

    /**
     * 根据业务参数类型的编号获取对应的名称
     *
     * @param code 业务参数类型的编号
     */
    @ResponseBody
    @RequestMapping(value = "/businessName", params = "code", method = RequestMethod.GET)
    public void getBusinessName(@RequestParam String code, HttpServletResponse response) {
        String name = businessParamTypeService.getName(code);
        GsonUtils.printData(response, name);
    }
}
