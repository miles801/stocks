package com.michael.base.msg.dao;

import com.michael.base.msg.bo.MessageBo;
import com.michael.base.msg.domain.Message;

import java.util.List;

/**
 * @author Michael
 */
public interface MessageDao {

    String save(Message message);

    void update(Message message);

    /**
     * 高级查询接口
     */
    List<Message> query(MessageBo bo);

    /**
     * 查询总记录数
     */
    Long getTotal(MessageBo bo);

    Message findById(String id);

    void deleteById(String id);

    /**
     * 根据实体对象删除
     * 必须保证该实体是存在的（一般是get或者load得到的对象）
     */
    void delete(Message message);
}
