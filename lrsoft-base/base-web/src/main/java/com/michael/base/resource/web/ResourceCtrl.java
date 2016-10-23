package com.michael.base.resource.web;


import com.michael.base.position.service.PositionResourceService;
import com.michael.base.resource.bo.ResourceBo;
import com.michael.base.resource.domain.Resource;
import com.michael.base.resource.service.ResourceService;
import com.michael.base.resource.vo.ResourceVo;
import com.michael.common.JspAccessType;
import com.michael.core.pager.PageVo;
import com.michael.core.web.BaseController;
import com.michael.docs.annotations.ApiOperate;
import com.michael.docs.annotations.ApiParam;
import com.michael.utils.gson.GsonUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static com.michael.utils.gson.GsonUtils.wrapDataToEntity;

/**
 * @author Michael
 */
@Controller
@RequestMapping(value = {"/base/resource"})
public class ResourceCtrl extends BaseController {

    @javax.annotation.Resource
    private ResourceService resourceService;

    @javax.annotation.Resource
    private PositionResourceService positionResourceService;

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String toAdd(HttpServletRequest request) {
        request.setAttribute(JspAccessType.PAGE_TYPE, JspAccessType.ADD);
        request.setAttribute("type", request.getParameter("type"));
        return "base/resource/resource_edit";
    }

    @RequestMapping(value = "/menu", method = RequestMethod.GET)
    public String toMenu(HttpServletRequest request) {
        request.setAttribute("type", "MENU");
        return "base/resource/resource_list";
    }

    @RequestMapping(value = "/element", method = RequestMethod.GET)
    public String toElement(HttpServletRequest request) {
        request.setAttribute("type", "ELEMENT");
        return "base/resource/resource_list";
    }

    @RequestMapping(value = "/data", method = RequestMethod.GET)
    public String toData(HttpServletRequest request) {
        request.setAttribute("type", "DATA");
        return "base/resource/resource_list";
    }

    @RequestMapping(value = "/modify", params = "id", method = RequestMethod.GET)
    public String toEdit(@RequestParam String id, HttpServletRequest request) {
        request.setAttribute("id", id);
        request.setAttribute(JspAccessType.PAGE_TYPE, JspAccessType.MODIFY);
        return "base/resource/resource_edit";
    }

    @RequestMapping(value = "/detail", params = "id", method = RequestMethod.GET)
    public String toDetail(@RequestParam String id, HttpServletRequest request) {
        request.setAttribute("id", id);
        request.setAttribute(JspAccessType.PAGE_TYPE, JspAccessType.DETAIL);
        return "base/resource/resource_edit";
    }


    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public void save(HttpServletRequest request, HttpServletResponse response) {
        Resource resource = wrapDataToEntity(request, Resource.class);
        resourceService.save(resource);
        GsonUtils.printSuccess(response);
    }


    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public void update(HttpServletRequest request, HttpServletResponse response) {
        Resource resource = wrapDataToEntity(request, Resource.class);
        resourceService.update(resource);
        GsonUtils.printSuccess(response);
    }


    @ResponseBody
    @RequestMapping(value = "/get", params = {"id"}, method = RequestMethod.GET)
    public void findById(@RequestParam String id, HttpServletResponse response) {
        ResourceVo vo = resourceService.findById(id);
        GsonUtils.printData(response, vo);
    }

    @ResponseBody
    @RequestMapping(value = "/pageQuery", method = RequestMethod.POST)
    public void pageQuery(HttpServletRequest request, HttpServletResponse response) {
        ResourceBo bo = wrapDataToEntity(request, ResourceBo.class);
        PageVo pageVo = resourceService.pageQuery(bo);
        GsonUtils.printData(response, pageVo);
    }

    @ApiOperate(value = "菜单的高级查询，返回列表", request = ResourceBo.class, response = ResourceVo.class)
    @ResponseBody
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public void query(HttpServletRequest request, HttpServletResponse response) {
        ResourceBo bo = wrapDataToEntity(request, ResourceBo.class);
        List<ResourceVo> data = resourceService.query(bo);
        GsonUtils.printData(response, data);
    }

    @ApiOperate(value = "批量启用资源")
    @ResponseBody
    @RequestMapping(value = "/enable", params = {"ids"}, method = RequestMethod.POST)
    public void enable(@RequestParam @ApiParam(name = "ids", value = "多个ID使用逗号进行分隔") String ids, HttpServletResponse response) {
        String[] idArr = ids.split(",");
        resourceService.enable(idArr);
        GsonUtils.printSuccess(response);
    }

    @ApiOperate(value = "批量禁用资源")
    @ResponseBody
    @RequestMapping(value = "/disable", params = {"ids"}, method = RequestMethod.POST)
    public void disable(@RequestParam @ApiParam(name = "ids", value = "多个ID使用逗号进行分隔") String ids, HttpServletResponse response) {
        String[] idArr = ids.split(",");
        resourceService.disable(idArr);
        GsonUtils.printSuccess(response);
    }


    @ApiOperate(value = "查询所有有效的菜单资源")
    @ResponseBody
    @RequestMapping(value = "/menu-valid", method = RequestMethod.GET)
    public void queryMenu(HttpServletRequest request, HttpServletResponse response) {
        List<ResourceVo> data = resourceService.validMenuResource();
        GsonUtils.printData(response, data);
    }


    @ApiOperate(value = "查询有效的孩子节点")
    @ResponseBody
    @RequestMapping(value = "/children", params = "id", method = RequestMethod.GET)
    public void queryValidChildren(@RequestParam String id, HttpServletRequest request, HttpServletResponse response) {
        List<ResourceVo> data = resourceService.children(id);
        GsonUtils.printData(response, data);
    }

    @ApiOperate(value = "查询有效的孩子节点")
    @ResponseBody
    @RequestMapping(value = "/children-valid", params = "id", method = RequestMethod.GET)
    public void queryChildren(@RequestParam String id, HttpServletRequest request, HttpServletResponse response) {
        List<ResourceVo> data = resourceService.validChildren(id);
        GsonUtils.printData(response, data);
    }


    @ApiOperate(value = "查询所有有效的操作资源")
    @ResponseBody
    @RequestMapping(value = "/element-valid", method = RequestMethod.GET)
    public void queryElement(HttpServletRequest request, HttpServletResponse response) {
        List<ResourceVo> data = resourceService.validElementResource();
        GsonUtils.printData(response, data);
    }

    @ApiOperate(value = "查询所有有效的数据资源")
    @ResponseBody
    @RequestMapping(value = "/data-valid", method = RequestMethod.GET)
    public void queryData(HttpServletRequest request, HttpServletResponse response) {
        List<ResourceVo> data = resourceService.validDataResource();
        GsonUtils.printData(response, data);
    }


    @ApiOperate(value = "查询指定岗位下的所有资源")
    @ResponseBody
    @RequestMapping(value = "/position", params = "positionId", method = RequestMethod.GET)
    public void queryData(@RequestParam @ApiParam(name = "positionId", value = "岗位ID") String positionId,
                          HttpServletRequest request, HttpServletResponse response) {
        List<String> data = positionResourceService.queryByPosition(positionId);
        GsonUtils.printData(response, data);
    }

    @ApiOperate(value = "给指定岗位授权")
    @ResponseBody
    @RequestMapping(value = "/grantMenu", method = RequestMethod.POST)
    public void grant(HttpServletRequest request, HttpServletResponse response) {
        PositionResourceTemp temp = GsonUtils.wrapDataToEntity(request, PositionResourceTemp.class);
        Assert.notNull(temp, "授权失败!没有获得授权信息!");
        positionResourceService.grantMenu(temp.getPositionId(), temp.getResourceIds());
        GsonUtils.printSuccess(response);
    }

    @ApiOperate(value = "给指定岗位授权")
    @ResponseBody
    @RequestMapping(value = "/grantElement", method = RequestMethod.POST)
    public void grantElement(HttpServletRequest request, HttpServletResponse response) {
        PositionResourceTemp temp = GsonUtils.wrapDataToEntity(request, PositionResourceTemp.class);
        Assert.notNull(temp, "授权失败!没有获得授权信息!");
        positionResourceService.grantElement(temp.getPositionId(), temp.getResourceIds());
        GsonUtils.printSuccess(response);
    }

    @ApiOperate(value = "给指定岗位授权")
    @ResponseBody
    @RequestMapping(value = "/grantData", method = RequestMethod.POST)
    public void grantData(HttpServletRequest request, HttpServletResponse response) {
        PositionResourceTemp temp = GsonUtils.wrapDataToEntity(request, PositionResourceTemp.class);
        Assert.notNull(temp, "授权失败!没有获得授权信息!");
        positionResourceService.grantData(temp.getPositionId(), temp.getResourceIds());
        GsonUtils.printSuccess(response);
    }


    @ApiOperate(value = "查询个人被授权的菜单资源")
    @ResponseBody
    @RequestMapping(value = "/mine/menu", method = RequestMethod.GET)
    public void myMenu(HttpServletRequest request, HttpServletResponse response) {
        List<ResourceVo> data = resourceService.myMenu();
        GsonUtils.printData(response, data);
    }
}
