package com.michael.base.msg.service.impl;

import com.michael.base.msg.bo.MessageBo;
import com.michael.base.msg.dao.MessageDao;
import com.michael.base.msg.domain.Message;
import com.michael.base.msg.service.MessageService;
import com.michael.base.msg.vo.MessageVo;
import com.michael.base.parameter.service.ParameterContainer;
import com.michael.core.beans.BeanWrapBuilder;
import com.michael.core.beans.BeanWrapCallback;
import com.michael.core.context.SecurityContext;
import com.michael.core.hibernate.validator.ValidatorUtils;
import com.michael.core.pager.PageVo;
import com.michael.utils.string.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author Michael
 */
@Service("messageService")
public class MessageServiceImpl implements MessageService, BeanWrapCallback<Message, MessageVo> {
    private Logger logger = Logger.getLogger(MessageServiceImpl.class);
    @Resource
    private MessageDao messageDao;

    @Override
    public String save(Message message) {
        message.setSenderId(SecurityContext.getEmpId());
        message.setSenderName(SecurityContext.getEmpName());
        message.setSendTime(new Date());
        message.setHasRead(false);
        message.setMark(false);
        String receiverId = message.getReceiverId();
        Assert.hasText(receiverId, "消息发送失败!接收人不能为空!");
        String receiverName = message.getReceiverName();
        Assert.hasText(receiverName, "消息发送失败!接收人不能为空!");
        String rids[] = receiverId.split(",");
        String rNames[] = receiverName.split(",");
        Assert.isTrue(rids.length == rNames.length, "消息发送失败!接收人ID的数量和接收人名称的数量不一致!");
        for (int i = 0; i < rids.length; i++) {
            if (StringUtils.isEmpty(rids[i])) {
                continue;
            }
            Message msg = new Message();
            BeanUtils.copyProperties(message, msg);
            msg.setReceiverId(rids[i].trim());
            msg.setReceiverName(rNames[i].trim());
            ValidatorUtils.validate(msg);
            String id = messageDao.save(msg);
            msg.setUrl("/base/message/view?_u=" + msg.getReceiverId() + "&id=" + id);
            // 回写URL
        }
        return null;
    }


    @Override
    public void update(Message message) {
        ValidatorUtils.validate(message);
        messageDao.update(message);
    }

    @Override
    public PageVo pageQuery(MessageBo bo) {
        PageVo vo = new PageVo();
        Long total = messageDao.getTotal(bo);
        vo.setTotal(total);
        if (total == null || total == 0) return vo;
        List<Message> messageList = messageDao.query(bo);
        List<MessageVo> vos = BeanWrapBuilder.newInstance()
                .setCallback(this)
                .wrapList(messageList, MessageVo.class);
        vo.setData(vos);
        return vo;
    }


    @Override
    public MessageVo findById(String id) {
        Message message = messageDao.findById(id);
        return BeanWrapBuilder.newInstance()
                .setCallback(this)
                .wrap(message, MessageVo.class);
    }

    @Override
    public void deleteByIds(String[] ids) {
        if (ids == null || ids.length == 0) return;
        for (String id : ids) {
            messageDao.deleteById(id);
        }
    }

    @Override
    public List<MessageVo> unread() {
        MessageBo bo = new MessageBo();
        bo.setHasRead(false);
        bo.setReceiverId(SecurityContext.getEmpId());
        List<Message> messages = messageDao.query(bo);
        return BeanWrapBuilder.newInstance()
                .wrapList(messages, MessageVo.class);
    }

    @Override
    public void read(String[] messageIds) {
        Assert.notEmpty(messageIds, "阅读消息失败!消息ID不能为空!");
        for (String id : messageIds) {
            Message message = messageDao.findById(id);
            Assert.notNull(message, "阅读消息失败!消息不存在，请刷新后重试!");
            message.setHasRead(true);
            message.setReadTime(new Date());
        }

    }

    @Override
    public void doCallback(Message message, MessageVo vo) {
        ParameterContainer container = ParameterContainer.getInstance();
        vo.setTypeName(container.getSystemName("MSG_TYPE", vo.getType()));
    }
}
