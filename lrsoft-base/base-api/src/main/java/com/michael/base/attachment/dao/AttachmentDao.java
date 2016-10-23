package com.michael.base.attachment.dao;


import com.michael.base.attachment.bo.AttachmentBo;
import com.michael.base.attachment.domain.Attachment;

import java.util.List;

/**
 * @author miles
 * @datetime 14-2-28 下午10:48
 */
public interface AttachmentDao {
    void save(Attachment attachment);

    Attachment findById(String id);

    List<Attachment> queryByIds(String[] ids);

    void deleteById(String id);

    /**
     * 根据条件查询附件集合
     *
     * @param businessId    业务id（必须）
     * @param businessType  业务类型
     * @param businessClass 业务类
     */
    List<Attachment> queryByBusiness(String businessId, String businessType, String businessClass);

    public boolean hasFile(String businessId, String attachmentId);

    /**
     * 查询某个业务对象下的所有附件id
     *
     * @param businessId 业务对象id
     * @return 附件id列表
     */
    List<String> queryAllId(String businessId);

    /**
     * 查询某个业务对象下的所有附件id
     *
     * @param businessId 业务对象id
     * @return 附件id列表
     */
    List<String> queryAllId(String businessId, String businessType, String businessClass);


    /**
     * 查询指定时间之前的临时附件的ID集合
     */
    List<String> queryHistoryTemp(long history);

    /**
     * 批量加载
     */
    List<Attachment> batchLoad(String[] ids);

    /**
     * 根据实体删除
     */
    void delete(Attachment attachment);

    /**
     * 获得符合条件的总记录条数
     *
     * @param bo 高级查询对象
     */
    Long getTotal(AttachmentBo bo);

    /**
     * 获得符合条件的记录，带分页查询
     *
     * @param bo 高级查询对象
     */
    List<Attachment> pageQuery(AttachmentBo bo);
}
