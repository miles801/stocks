package com.michael.stock.stock.web;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.michael.common.JspAccessType;
import com.michael.core.SystemContainer;
import com.michael.core.pager.PageVo;
import com.michael.core.pager.Pager;
import com.michael.core.web.BaseController;
import com.michael.poi.exp.ExportEngine;
import com.michael.stock.stock.bo.StockDayBo;
import com.michael.stock.stock.domain.StockDay;
import com.michael.stock.stock.schedule.StockBusinessSchedule;
import com.michael.stock.stock.service.StockDayService;
import com.michael.stock.stock.vo.StockDayVo;
import com.michael.utils.gson.DateStringConverter;
import com.michael.utils.gson.DoubleConverter;
import com.michael.utils.gson.GsonUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Michael
 */
@Controller
@RequestMapping(value = {"/stock/stock/stockDay"})
public class StockDayCtrl extends BaseController {
    @Resource
    private StockDayService stockDayService;


    @RequestMapping(value = {""}, method = RequestMethod.GET)
    public String toList() {
        return "stock/stock/stockDay/stockDay_list";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String toAdd(HttpServletRequest request) {
        request.setAttribute(JspAccessType.PAGE_TYPE, JspAccessType.ADD);
        return "stock/stock/stockDay/stockDay_edit";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public void save(HttpServletRequest request, HttpServletResponse response) {
        StockDay stockDay = GsonUtils.wrapDataToEntity(request, StockDay.class);
        stockDayService.save(stockDay);
        GsonUtils.printSuccess(response);
    }

    @RequestMapping(value = "/modify", params = {"id"}, method = RequestMethod.GET)
    public String toModify(@RequestParam String id, HttpServletRequest request) {
        request.setAttribute(JspAccessType.PAGE_TYPE, JspAccessType.MODIFY);
        request.setAttribute("id", id);
        return "stock/stock/stockDay/stockDay_edit";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public void update(HttpServletRequest request, HttpServletResponse response) {
        StockDay stockDay = GsonUtils.wrapDataToEntity(request, StockDay.class);
        stockDayService.update(stockDay);
        GsonUtils.printSuccess(response);
    }

    @RequestMapping(value = {"/detail"}, params = {"id"}, method = RequestMethod.GET)
    public String toDetail(@RequestParam String id, HttpServletRequest request) {
        request.setAttribute(JspAccessType.PAGE_TYPE, JspAccessType.DETAIL);
        request.setAttribute("id", id);
        return "stock/stock/stockDay/stockDay_edit";
    }

    @ResponseBody
    @RequestMapping(value = "/get", params = {"id"}, method = RequestMethod.GET)
    public void findById(@RequestParam String id, HttpServletResponse response) {
        StockDayVo vo = stockDayService.findById(id);
        GsonUtils.printData(response, vo);
    }

    @ResponseBody
    @RequestMapping(value = "/pageQuery", method = RequestMethod.POST)
    public void pageQuery(HttpServletRequest request, HttpServletResponse response) {
        StockDayBo bo = GsonUtils.wrapDataToEntity(request, StockDayBo.class);
        PageVo pageVo = stockDayService.pageQuery(bo);
        GsonUtils.printData(response, pageVo);
    }

    @ResponseBody
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public void query(HttpServletRequest request, HttpServletResponse response) {
        StockDayBo bo = GsonUtils.wrapDataToEntity(request, StockDayBo.class);
        List<StockDayVo> vos = stockDayService.query(bo);
        GsonUtils.printData(response, vos);
    }

    @ResponseBody
    @RequestMapping(value = "/delete", params = {"ids"}, method = RequestMethod.DELETE)
    public void deleteByIds(@RequestParam String ids, HttpServletResponse response) {
        String[] idArr = ids.split(",");
        stockDayService.deleteByIds(idArr);
        GsonUtils.printSuccess(response);
    }

    // 导出数据
    @RequestMapping(value = "/export", method = RequestMethod.GET)
    public String export(HttpServletRequest request, HttpServletResponse response) {
        Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new DateStringConverter("yyyy-MM-dd HH:mm:ss"))
                .create();
        StockDayBo bo = GsonUtils.wrapDataToEntity(request, StockDayBo.class);
        List<StockDayVo> data = stockDayService.query(bo);
        String json = gson.toJson(data);
        JsonElement element = gson.fromJson(json, JsonElement.class);
        JsonObject o = new JsonObject();
        o.add("c", element);
        String disposition = null;//
        try {
            disposition = "attachment;filename=" + URLEncoder.encode("日K数据" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".xlsx", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", disposition);
        try {
            InputStream inputStream = StockDayCtrl.class.getClassLoader().getResourceAsStream("export_stockDay.xlsx");
            Assert.notNull(inputStream, "数据导出失败!模板文件不存在，请与管理员联系!");
            new ExportEngine().export(response.getOutputStream(), inputStream, o);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    // 下载模板
    @ResponseBody
    @RequestMapping(value = "/template", method = RequestMethod.GET)
    public void downloadTemplate(HttpServletResponse response) {
        InputStream input = StockDayCtrl.class.getClassLoader().getResourceAsStream("import_stockDay.xlsx");
        Assert.notNull(input, "模板下载失败!日K数据导入模板不存在!");
        response.setContentType("application/vnd.ms-excel");
        String disposition = null;//
        try {
            disposition = "attachment;filename=" + URLEncoder.encode("日K数据导入模板.xlsx", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.setHeader("Content-disposition", disposition);
        try {
            IOUtils.copy(input, response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 跳转到导入页面
    @RequestMapping(value = "/import", method = RequestMethod.GET)
    public String toImport(HttpServletRequest request) {
        return "stock/stock/stockDay/stockDay_import";
    }

    // 执行导入
    @ResponseBody
    @RequestMapping(value = "/import", params = {"attachmentIds"}, method = RequestMethod.POST)
    public void importData(@RequestParam String attachmentIds, HttpServletResponse response) {
        stockDayService.importData(attachmentIds.split(","));
        GsonUtils.printSuccess(response);
    }

    // 3线分析报告
    @ResponseBody
    @RequestMapping(value = "/report3", method = RequestMethod.POST)
    public void report3(HttpServletRequest request, HttpServletResponse response) {
        StockDayBo bo = GsonUtils.wrapDataToEntity(request, StockDayBo.class);
        List<Map<String, Object>> data = stockDayService.report3(bo);
        GsonUtils.printData(response, data);
    }

    // 6线分析报告
    @ResponseBody
    @RequestMapping(value = "/report6", method = RequestMethod.POST)
    public void report6(HttpServletRequest request, HttpServletResponse response) {
        StockDayBo bo = GsonUtils.wrapDataToEntity(request, StockDayBo.class);
        List<Map<String, Object>> data = stockDayService.report6(bo);
        GsonUtils.printData(response, data);
    }

    // 3线估值结果
    @ResponseBody
    @RequestMapping(value = "/result3", method = RequestMethod.POST)
    public void result3(HttpServletRequest request, HttpServletResponse response) {
        StockDayBo bo = GsonUtils.wrapDataToEntity(request, StockDayBo.class);
        PageVo data = stockDayService.result3(bo);
        GsonUtils.printData(response, data);
    }

    // 6线估值结果
    @ResponseBody
    @RequestMapping(value = "/result6", method = RequestMethod.POST)
    public void result6(HttpServletRequest request, HttpServletResponse response) {
        StockDayBo bo = GsonUtils.wrapDataToEntity(request, StockDayBo.class);
        PageVo data = stockDayService.result6(bo);
        GsonUtils.printData(response, data);
    }

    // 查询最后一个交易日
    @ResponseBody
    @RequestMapping(value = "/lastDay", method = RequestMethod.GET)
    public void lastDay(HttpServletRequest request, HttpServletResponse response) {
        Date data = stockDayService.lastDay();
        GsonUtils.printData(response, data);
    }

    // 导出数据
    @RequestMapping(value = "/export-result3", method = RequestMethod.GET)
    public String exportResult3(HttpServletRequest request, HttpServletResponse response) {
        final Gson gson = new GsonBuilder().registerTypeAdapter(Double.class, new DoubleConverter()).create();
        final StockDayBo bo = GsonUtils.wrapDataToEntity(request, StockDayBo.class);
        Pager.setStart(0);
        Pager.setLimit(Integer.MAX_VALUE);
        final PageVo data = stockDayService.result3(bo);
        final DecimalFormat decimalFormat = new DecimalFormat("##.## %");
        final DecimalFormat decimalFormat2 = new DecimalFormat("##0.###");
        List<Map<String, Object>> list = data.getData();
        for (Map<String, Object> map : list) {
            BigDecimal yang = (BigDecimal) map.get("yang");
            int counts = ((BigInteger) map.get("counts")).intValue();
            Double nextHigh = (Double) map.get("nextHigh");
            Double nextLow = (Double) map.get("nextLow");
            map.put("percent", decimalFormat.format(yang.intValue() * 1.0 / counts));
            map.put("avgHigh", decimalFormat2.format(nextHigh / counts));
            map.put("avgLow", decimalFormat2.format(nextLow / counts));
        }
        String json = gson.toJson(list);
        JsonElement element = gson.fromJson(json, JsonElement.class);
        JsonObject o = new JsonObject();
        o.add("c", element);
        String disposition = null;//
        try {
            disposition = "attachment;filename=" + URLEncoder.encode("3日风险估值结果" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".xlsx", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", disposition);
        try {
            InputStream inputStream = StockDayCtrl.class.getClassLoader().getResourceAsStream("export_stockDayResult.xlsx");
            Assert.notNull(inputStream, "数据导出失败!模板文件不存在，请与管理员联系!");
            new ExportEngine().export(response.getOutputStream(), inputStream, o);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 导出数据
    @RequestMapping(value = "/export-result6", method = RequestMethod.GET)
    public String exportResult6(HttpServletRequest request, HttpServletResponse response) {
        final Gson gson = new GsonBuilder().registerTypeAdapter(Double.class, new DoubleConverter()).create();
        final StockDayBo bo = GsonUtils.wrapDataToEntity(request, StockDayBo.class);
        Pager.setStart(0);
        Pager.setLimit(Integer.MAX_VALUE);
        final PageVo data = stockDayService.result6(bo);
        final DecimalFormat decimalFormat = new DecimalFormat("##.## %");
        final DecimalFormat decimalFormat2 = new DecimalFormat("##0.###");
        List<Map<String, Object>> list = data.getData();
        for (Map<String, Object> map : list) {
            BigDecimal yang = (BigDecimal) map.get("yang");
            int counts = ((BigInteger) map.get("counts")).intValue();
            Double nextHigh = (Double) map.get("nextHigh");
            Double nextLow = (Double) map.get("nextLow");
            map.put("percent", decimalFormat.format(yang.intValue() * 1.0 / counts));
            map.put("avgHigh", decimalFormat2.format(nextHigh / counts));
            map.put("avgLow", decimalFormat2.format(nextLow / counts));
        }
        String json = gson.toJson(list);
        JsonElement element = gson.fromJson(json, JsonElement.class);
        JsonObject o = new JsonObject();
        o.add("c", element);
        String disposition = null;//
        try {
            disposition = "attachment;filename=" + URLEncoder.encode("6日风险估值结果" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".xlsx", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", disposition);
        try {
            InputStream inputStream = StockDayCtrl.class.getClassLoader().getResourceAsStream("export_stockDayResult.xlsx");
            Assert.notNull(inputStream, "数据导出失败!模板文件不存在，请与管理员联系!");
            new ExportEngine().export(response.getOutputStream(), inputStream, o);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    // 手动触发股票交易历史的同步
    @ResponseBody
    @RequestMapping(value = "/sync", method = RequestMethod.POST)
    public void sync(HttpServletRequest request, HttpServletResponse response) {
        StockBusinessSchedule schedule = SystemContainer.getInstance().getBean(StockBusinessSchedule.class);
        schedule.execute();
        GsonUtils.printSuccess(response);
    }

    // 重置第7日数据
    @ResponseBody
    @RequestMapping(value = "/reset7", method = RequestMethod.POST)
    public void reset7(HttpServletRequest request, HttpServletResponse response) {
        StockBusinessSchedule schedule = SystemContainer.getInstance().getBean(StockBusinessSchedule.class);
        schedule.reset7DayInfo();
        GsonUtils.printSuccess(response);
    }
}
