package com.michael.base.attachment.service;

import com.michael.base.attachment.bo.AttachmentBo;
import com.michael.base.attachment.domain.Attachment;
import com.michael.base.attachment.vo.AttachmentVo;
import com.michael.core.pager.PageVo;

import java.util.List;

/**
 * 附件服务
 *
 * @author miles
 * @datetime 14-2-28 下午11:01
 */
public interface AttachmentService {
    /**
     * 保存单个附件（附件id是外部指定）
     * 1. 被保存的附件的状态会被强制设置为"临时"
     * 2. 如果附件是文件，则会按照年月日的方式设置访问path
     */
    void save(Attachment attachment);


    /**
     * 将一组附件ID绑定到指定业务对象，并设置附件的状态为正式，且将真实附件文件从临时目录转移到正式目录
     *
     * @param businessId 业务ID
     * @param ids        附件列表
     */
    void bind(String businessId, String[] ids);

    /**
     * 高级分页查询
     */
    PageVo pageQuery(AttachmentBo bo);

    /**
     * 根据ID删除文件
     *
     * @param id 附件ID
     */
    void deleteById(String id);

    /**
     * 根据附件的id列表批量删除附件（同时会删除附件目录中的文件）
     *
     * @param ids 附件id列表
     */
    void deleteByIds(String[] ids);

    /**
     * 清除所有的临时文件、状态为临时的附件
     */
    void clearTemp();

    /**
     * 根据id查询附件信息
     */
    AttachmentVo findById(String id);

    /**
     * 根据业务id查询所有的附件信息
     *
     * @param businessId 业务id
     */
    List<AttachmentVo> queryByBusiness(String businessId);

    /**
     * 根据业务id、业务类型联合查询所有的附件信息
     *
     * @param businessId   业务id
     * @param businessType 业务类型
     */
    List<AttachmentVo> queryByBusiness(String businessId, String businessType);

    /**
     * 根据业务id、业务类型、业务类联合查询所有的附件信息
     *
     * @param businessId    业务id
     * @param businessType  业务类型
     * @param businessClass 业务类
     */
    List<AttachmentVo> queryByBusiness(String businessId, String businessType, String businessClass);


    /**
     * 根据业务id批量删除所有的附件（会同时删除附件目录中的附件）
     *
     * @param businessId 业务id
     */
    void deleteByBusiness(String businessId);

    /**
     * 根据业务id和业务类型批量删除所有的附件（会同时删除附件目录中的附件）
     *
     * @param businessId   业务id
     * @param businessType 业务类型
     */
    void deleteByBusiness(String businessId, String businessType);

    /**
     * 根据业务id、业务类型和业务类批量删除所有的附件（会同时删除附件目录中的附件）
     *
     * @param businessId    业务id
     * @param businessType  业务类型
     * @param businessClass 业务类
     */
    void deleteByBusiness(String businessId, String businessType, String businessClass);

    /**
     * 判断某个附件是否属于指定的业务对象
     *
     * @param businessId   业务id
     * @param attachmentId 附件id
     */
    boolean hasFile(String businessId, String attachmentId);

    /**
     * 根据附件的ID数组查询对应的附件信息
     *
     * @param ids 附件的id数组
     */
    List<AttachmentVo> queryByIds(String[] ids);
}
