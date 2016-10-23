package com.michael.base.msg.service;

import com.michael.base.msg.bo.MessageBo;
import com.michael.base.msg.domain.Message;
import com.michael.base.msg.vo.MessageVo;
import com.michael.core.pager.PageVo;

import java.util.List;

/**
 * @author Michael
 */
public interface MessageService {

    /**
     * 保存
     */
    String save(Message message);

    /**
     * 更新
     */
    void update(Message message);

    /**
     * 分页查询
     */
    PageVo pageQuery(MessageBo bo);

    /**
     * 根据ID查询对象的信息
     */
    MessageVo findById(String id);

    /**
     * 批量删除
     */
    void deleteByIds(String[] ids);

    /**
     * 查询个人所有未读的消息列表
     */
    List<MessageVo> unread();

    /**
     * 读消息
     */
    void read(String messageIds[]);

}
