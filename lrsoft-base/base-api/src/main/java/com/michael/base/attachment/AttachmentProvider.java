package com.michael.base.attachment;

import com.michael.base.attachment.domain.Attachment;
import com.michael.base.attachment.service.AttachmentService;
import com.michael.base.attachment.utils.AttachmentHolder;
import com.michael.base.attachment.vo.AttachmentVo;
import com.michael.core.SystemContainer;
import org.apache.commons.io.IOUtils;
import org.springframework.util.Assert;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 附件对外接口提供类
 *
 * @author miles
 * @datetime 2014/7/1 11:10
 */
public class AttachmentProvider {

    /**
     * 根据附件id获取附件流，然后进行处理
     *
     * @param attachmentId 附件id
     * @param handler      附件的处理方式
     */
    public static void handle(String attachmentId, AttachmentHandler handler) {
        Assert.hasText(attachmentId, "获取附件流进行处理时,附件ID不能为空!");
        Assert.notNull(handler, "获取附件流进行处理时,AttachmentHandler不能为空!");
        InputStream stream = null;
        try {
            File file = getFile(attachmentId);
            if (file != null) {
                stream = new FileInputStream(file);
                handler.handle(stream);
                IOUtils.closeQuietly(stream);
            } else {
                throw new FileNotFoundException(String.format("ID为[%s]的附件不存在!", attachmentId));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取附件信息
     *
     * @param attachmentId 附件id
     */
    public static AttachmentVo getInfo(String attachmentId) {
        AttachmentService service = SystemContainer.getInstance().getBean(AttachmentService.class);
        return service.findById(attachmentId);
    }

    /**
     * 获取附件列表
     *
     * @param businessId 业务id
     */
    public static List<AttachmentVo> getAttachments(String businessId) {
        AttachmentService service = SystemContainer.getInstance().getBean(AttachmentService.class);
        return service.queryByBusiness(businessId);
    }

    /**
     * 获得指定业务ID下的所有的文件
     * 警告：不要操作该文件！
     *
     * @param businessId 业务ID
     * @return 附件列表
     */
    public static List<File> getFiles(String businessId, String btype, String bclass) {
        Assert.hasText(businessId, "获取附件:业务ID不能为空!");
        AttachmentService service = SystemContainer.getInstance().getBean(AttachmentService.class);
        List<AttachmentVo> attachmentVos = service.queryByBusiness(businessId, btype, bclass);
        List<File> files = new ArrayList<File>();
        if (attachmentVos != null && !attachmentVos.isEmpty()) {
            for (AttachmentVo vo : attachmentVos) {
                File file = getFile(vo.getId());
                if (file != null) {
                    files.add(file);
                }
            }
        }
        return files;
    }

    /**
     * 处理临时附件
     * 可以从AttachmentContext中获取当前附件的相关信息
     *
     * @param attachmentId 附件id/附件名称
     * @param handler      处理方式
     */
    public static void handleTmp(String attachmentId, AttachmentHandler handler) {
        Assert.hasText(attachmentId, "获取临时附件流时,附件的ID不能为空!");
        Assert.notNull(handler, "获取附件流进行处理时,AttachmentHandler不能为空!");
        InputStream stream = null;
        try {
            File file = AttachmentHolder.newInstance().getTempFile(attachmentId);
            stream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        handler.handle(stream);
        AttachmentContext.clear();
        IOUtils.closeQuietly(stream);
    }


    public static File getFile(String filename) {
        Assert.hasText(filename, "文件名不能为空!");
        AttachmentService service = SystemContainer.getInstance().getBean(AttachmentService.class);
        Assert.notNull(service, String.format("没有获得附件服务接口的实例!%s", AttachmentService.class.getName()));
        AttachmentVo vo = service.findById(filename);
        if (vo == null) {
            return null;
        }
        AttachmentContext.set(vo);
        String status = vo.getStatus();
        String path = null;
        if (Attachment.STATUS_TEMP.equals(status)) {
            path = AttachmentHolder.newInstance().getTempFolder() + "/" + filename;
        } else if (Attachment.STATUS_ACTIVE.equals(status)) {
            path = vo.getPath();
        } else {
            throw new RuntimeException(String.format("不合法的附件状态[%s]!", status));
        }
        File file = new File(path);
        if (file.exists()) {
            return file;
        }
        return null;
    }
}