package com.michael.base.region.web;

import com.michael.base.region.bo.RegionBo;
import com.michael.base.region.domain.Region;
import com.michael.base.region.service.RegionService;
import com.michael.base.region.vo.RegionVo;
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
 * @author: miles
 * @datetime: 2014-03-25
 */

@Controller
@RequestMapping(value = {"/base/region"})
public class RegionCtrl extends BaseController {
    @Resource
    private RegionService regionService;

    @RequestMapping(value = {""}, method = RequestMethod.GET)
    public String toList() {
        return "base/region/list/region_list";
    }


    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public String save(HttpServletRequest request, HttpServletResponse response) {
        Region region = GsonUtils.wrapDataToEntity(request, Region.class);
        regionService.save(region);
        GsonUtils.printSuccess(response);
        return null;
    }


    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public String update(HttpServletRequest request, HttpServletResponse response) {
        Region region = GsonUtils.wrapDataToEntity(request, Region.class);
        regionService.update(region);
        GsonUtils.printSuccess(response);
        return null;
    }

    @RequestMapping(value = "/mine", method = RequestMethod.GET)
    @ResponseBody
    public void queryMyArea(HttpServletRequest request, HttpServletResponse response) {
        List<RegionVo> data = regionService.queryMine();
        GsonUtils.printData(response, data);
    }

    @RequestMapping(value = "/master-set", method = RequestMethod.POST)
    @ResponseBody
    public void setMaster(HttpServletRequest request, HttpServletResponse response) {
        Region region = GsonUtils.wrapDataToEntity(request, Region.class);
        regionService.setMaster(region.getId(), region.getMasterId());
        GsonUtils.printSuccess(response);
    }

    @RequestMapping(value = "/master-clear", params = "id", method = RequestMethod.POST)
    @ResponseBody
    public void clearMaster(String id, HttpServletResponse response) {
        regionService.clearMaster(id);
        GsonUtils.printSuccess(response);
    }


    @ResponseBody
    @RequestMapping(value = "/get", params = {"id"}, method = RequestMethod.GET)
    public String findById(@RequestParam String id, HttpServletResponse response) {
        RegionVo vo = regionService.findById(id);
        GsonUtils.printData(response, vo);
        return null;
    }


    @ResponseBody
    @RequestMapping(value = "/query")
    public String query(HttpServletRequest request, HttpServletResponse response) {
        RegionBo bo = GsonUtils.wrapDataToEntity(request, RegionBo.class);
        PageVo pageVo = regionService.query(bo);
        GsonUtils.printData(response, pageVo);
        return null;
    }

    @ResponseBody
    @RequestMapping(value = "/delete", params = {"ids"})
    public String deleteByIds(String ids, HttpServletResponse response) {
        if (ids == null || "".equals(ids.trim())) {
            throw new RuntimeException("ids");
        }
        String[] idArr = ids.split(",");
        regionService.deleteByIds(idArr);
        GsonUtils.printSuccess(response);
        return null;
    }

    /**
     * 树的查询，支持的参数：
     * root：查询根节点
     * parentId:查询指定上级节点的所有子节点
     * 返回：[{id:'',name:'',path:'',isParent:[]},{}]
     */
    @ResponseBody
    @RequestMapping(value = "/tree", method = RequestMethod.POST)
    public String tree(HttpServletRequest request, HttpServletResponse response) {
        RegionBo bo = GsonUtils.wrapDataToEntity(request, RegionBo.class);
        List<RegionVo> vos = regionService.tree(bo);
        GsonUtils.printData(response, vos);
        return null;
    }

}
