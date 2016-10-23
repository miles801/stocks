package com.michael.base.msg.web;

import com.michael.base.msg.bo.MessageBo;
import com.michael.base.msg.domain.Message;
import com.michael.base.msg.service.MessageService;
import com.michael.base.msg.vo.MessageVo;
import com.michael.common.JspAccessType;
import com.michael.docs.annotations.Api;
import com.michael.docs.annotations.ApiOperate;
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
@RequestMapping(value = {"/base/message"})
@Api("消息通知")
public class MessageCtrl extends com.michael.core.web.BaseController {
    @Resource
    private MessageService messageService;

    @RequestMapping(value = {""}, method = RequestMethod.GET)
    public String toList() {
        return "base/message/list/message_list";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String toAdd(HttpServletRequest request) {
        request.setAttribute(JspAccessType.PAGE_TYPE, JspAccessType.ADD);
        return "base/message/edit/message_edit";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public void save(HttpServletRequest request, HttpServletResponse response) {
        Message message = GsonUtils.wrapDataToEntity(request, Message.class);
        messageService.save(message);
        GsonUtils.printSuccess(response);
    }

    @RequestMapping(value = "/modify", params = {"id"}, method = RequestMethod.GET)
    public String toModify(@RequestParam String id, HttpServletRequest request) {
        request.setAttribute(JspAccessType.PAGE_TYPE, JspAccessType.MODIFY);
        request.setAttribute("id", id);
        return "base/message/edit/message_edit";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public void update(HttpServletRequest request, HttpServletResponse response) {
        Message message = GsonUtils.wrapDataToEntity(request, Message.class);
        messageService.update(message);
        GsonUtils.printSuccess(response);
    }

    @RequestMapping(value = {"/detail"}, params = {"id"}, method = RequestMethod.GET)
    public String toDetail(@RequestParam String id, HttpServletRequest request) {
        request.setAttribute(JspAccessType.PAGE_TYPE, JspAccessType.DETAIL);
        request.setAttribute("id", id);
        return "base/message/edit/message_edit";
    }

    @RequestMapping(value = {"/view"}, params = {"id"}, method = RequestMethod.GET)
    public String toView(@RequestParam String id, HttpServletRequest request) {
        request.setAttribute("id", id);
        messageService.read(new String[]{id});  // 阅读消息
        MessageVo vo = messageService.findById(id);
        request.setAttribute("data", vo);
        return "base/message/edit/message_view";
    }

    @ResponseBody
    @RequestMapping(value = "/get", params = {"id"}, method = RequestMethod.GET)
    public void findById(@RequestParam String id, HttpServletResponse response) {
        MessageVo vo = messageService.findById(id);
        GsonUtils.printData(response, vo);
    }

    @ResponseBody
    @RequestMapping(value = "/pageQuery", method = RequestMethod.POST)
    public void pageQuery(HttpServletRequest request, HttpServletResponse response) {
        MessageBo bo = GsonUtils.wrapDataToEntity(request, MessageBo.class);
        com.michael.core.pager.PageVo pageVo = messageService.pageQuery(bo);
        GsonUtils.printData(response, pageVo);
    }

    @ApiOperate(value = "查询所有个人未读消息列表", response = Message.class)
    @ResponseBody
    @RequestMapping(value = "/unread", method = RequestMethod.GET)
    public void unread(HttpServletRequest request, HttpServletResponse response) {
        List<MessageVo> data = messageService.unread();
        GsonUtils.printData(response, data);
    }

    @ApiOperate(value = "阅读消息")
    @ResponseBody
    @RequestMapping(value = "/read", params = {"ids"}, method = RequestMethod.GET)
    public void read(@RequestParam
                     @ApiParam(name = "ids", desc = "消息ID,多个消息使用英文逗号进行分隔", required = true) String ids, HttpServletResponse response) {
        messageService.read(ids.split(","));
        GsonUtils.printSuccess(response);
    }

    @ApiOperate(value = "我的消息", desc = "查询所有我接收到的消息，采用分页查询", response = Message.class)
    @ResponseBody
    @RequestMapping(value = "/mine", method = RequestMethod.GET)
    public void mine(@ApiParam(name = "start", desc = "从0开始,查询开始的记录数索引") Integer start,
                     @ApiParam(name = "limit", desc = "要查询的记录条数") Integer limit,
                     HttpServletResponse response) {
        MessageBo bo = new MessageBo();
        bo.setReceiverId(com.michael.core.context.SecurityContext.getEmpId());
        com.michael.core.pager.PageVo vo = messageService.pageQuery(bo);
        GsonUtils.printData(response, vo);
    }

    @ApiOperate(value = "删除消息", desc = "删除具体的某一条消息")
    @ResponseBody
    @RequestMapping(value = "/delete", params = {"ids"}, method = RequestMethod.DELETE)
    public void deleteByIds(@RequestParam @ApiParam(name = "ids", desc = "消息ID，多条消息使用英文逗号进行分隔") String ids, HttpServletResponse response) {
        String[] idArr = ids.split(",");
        messageService.deleteByIds(idArr);
        GsonUtils.printSuccess(response);
    }

}
