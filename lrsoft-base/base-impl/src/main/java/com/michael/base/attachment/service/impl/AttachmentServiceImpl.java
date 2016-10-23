package com.michael.base.attachment.service.impl;

import com.michael.base.attachment.bo.AttachmentBo;
import com.michael.base.attachment.dao.AttachmentDao;
import com.michael.base.attachment.domain.Attachment;
import com.michael.base.attachment.service.AttachmentService;
import com.michael.base.attachment.utils.AttachmentHolder;
import com.michael.base.attachment.vo.AttachmentVo;
import com.michael.core.beans.BeanWrapBuilder;
import com.michael.core.beans.BeanWrapCallback;
import com.michael.core.pager.PageVo;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

/**
 * @author miles
 * @datetime 14-2-28 下午11:03
 */
@Service("attachmentService")
public class AttachmentServiceImpl implements AttachmentService, BeanWrapCallback<Attachment, AttachmentVo> {
    Logger logger = Logger.getLogger(AttachmentService.class);
    @Resource
    private AttachmentDao attachmentDao;


    @Override
    public void save(Attachment attachment) {
        // 设置path
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String path = AttachmentHolder.newInstance().getActiveFolder() + year + "/" + month + "/" + day + "/" + attachment.getId();
        attachment.setPath(path);
        // 设置为临时状态
        attachment.setStatus(Attachment.STATUS_TEMP);
        attachmentDao.save(attachment);

    }

    @Override
    public PageVo pageQuery(AttachmentBo bo) {
        PageVo vo = new PageVo();
        Long total = attachmentDao.getTotal(bo);
        if (total != null && total > 0) {
            vo.setTotal(total);
            List<Attachment> attachments = attachmentDao.pageQuery(bo);
            List<AttachmentVo> vos = convertVos(attachments);
            vo.setData(vos);
        }
        return vo;
    }

    @Override
    public void bind(String businessId, String[] ids) {
        Assert.hasText(businessId, "业务ID不能为空!");
        Assert.notEmpty(ids);
        List<Attachment> attachments = attachmentDao.batchLoad(ids);
        AttachmentHolder holder = AttachmentHolder.newInstance();
        if (attachments != null && !attachments.isEmpty()) {
            for (Attachment attachment : attachments) {
                attachment.setStatus(Attachment.STATUS_ACTIVE);
                attachment.setBusinessId(businessId);
                // 将附件从临时目录转移到正式目录
                String path = attachment.getPath();
                try {
                    FileUtils.moveFile(AttachmentHolder.newInstance().getTempFile(attachment.getId()), new File(path));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    private String hasSame(List<String> attachments, String id) {
        if (attachments == null || attachments.size() < 1) return null;
        for (String attachment : attachments) {
            if (attachment.equals(id)) {
                return attachment;
            }
        }
        return null;
    }

    @Override
    public AttachmentVo findById(String id) {
        return BeanWrapBuilder.newInstance()
                .wrap(attachmentDao.findById(id), AttachmentVo.class);
    }

    @Override
    public List<AttachmentVo> queryByBusiness(String businessId) {
        return convertVos(attachmentDao.queryByBusiness(businessId, null, null));
    }

    @Override
    public List<AttachmentVo> queryByBusiness(String businessId, String businessType) {
        return convertVos(attachmentDao.queryByBusiness(businessId, businessType, null));
    }

    @Override
    public List<AttachmentVo> queryByBusiness(String businessId, String businessType, String businessClass) {
        return convertVos(attachmentDao.queryByBusiness(businessId, businessType, businessClass));
    }

    @Override
    public void deleteByBusiness(String businessId) {
        deleteByBusiness(businessId, null, null);
    }

    @Override
    public void deleteByBusiness(String businessId, String businessType) {
        deleteByBusiness(businessId, businessType, null);
    }

    @Override
    public void deleteByBusiness(String businessId, String businessType, String businessClass) {
        List<Attachment> attachments = attachmentDao.queryByBusiness(businessId, businessType, businessClass);
        if (attachments != null && !attachments.isEmpty()) {
            for (Attachment attachment : attachments) {
                delete(attachment);
            }
        }
    }


    @Override
    public List<AttachmentVo> queryByIds(String[] ids) {
        return convertVos(attachmentDao.queryByIds(ids));
    }

    @Override
    public boolean hasFile(String businessId, String attachmentId) {
        return attachmentDao.hasFile(businessId, attachmentId);
    }


    @Override
    public void deleteById(String id) {
        Assert.hasText(id);
        Attachment attachment = attachmentDao.findById(id);
        delete(attachment);
    }

    @Override
    public void clearTemp() {
        List<String> tempIdList = attachmentDao.queryHistoryTemp(60 * 1000);
        if (tempIdList != null && !tempIdList.isEmpty()) {
            for (String id : tempIdList) {
                deleteById(id);
            }
        }
    }

    @Override
    public void deleteByIds(String[] ids) {
        Assert.notEmpty(ids);
        List<Attachment> attachments = attachmentDao.batchLoad(ids);
        if (attachments != null && !attachments.isEmpty()) {
            for (Attachment attachment : attachments) {
                delete(attachment);
            }
        }
    }

    /**
     * 根据附件的状态分别删除附件文件（临时或正式目录中的文件），并删除该附件在数据库中的记录
     * PS：如果附件的状态错误，则会抛出异常
     *
     * @param attachment 附件对象
     */
    private void delete(Attachment attachment) {
        String id = attachment.getId();
        Assert.notNull(attachment, "删除附件：附件[" + id + "]不存在或者已经被删除!");
        String status = attachment.getStatus();
        if (Attachment.STATUS_TEMP.equals(status)) {
            boolean result = FileUtils.deleteQuietly(AttachmentHolder.newInstance().getTempFile(id));
            if (!result) {
                logger.error("(临时)附件删除失败: " + id);
            }
            attachmentDao.delete(attachment);
        } else if (Attachment.STATUS_ACTIVE.equals(status)) {
            File file = new File(attachment.getPath());
            if (file.exists()) {
                boolean result = FileUtils.deleteQuietly(file);
                Assert.isTrue(result, String.format("(正式)附件删除失败: %s", attachment.getPath()));
            }
            attachmentDao.delete(attachment);
        } else {
            throw new RuntimeException("附件[" + id + "]的状态不合法!只支持'临时'和'正式'两种状态!");
        }
    }


    /**
     * 将指定数据集合转换成VO集合
     *
     * @param data 源数据
     * @return 目标数据
     */
    private List<AttachmentVo> convertVos(List<Attachment> data) {
        return BeanWrapBuilder.newInstance()
                .setCallback(this)
                .wrapList(data, AttachmentVo.class);
    }

    @Override
    public void doCallback(Attachment attachment, AttachmentVo vo) {
        String status = attachment.getStatus();
        if (Attachment.STATUS_TEMP.equals(status)) {
            vo.setStatusName("临时");
        } else if (Attachment.STATUS_ACTIVE.equals(status)) {
            vo.setStatusName("永久");
        } else {
            vo.setStatusName("无效");
        }
    }
}
