package com.michael.base.attachment.vo;


import com.michael.common.CommonVo;

/**
 * @author miles
 * @datetime 2014/7/1 12:09
 */
public class AttachmentVo extends CommonVo {
    /**
     * 文件的真实名称
     */
    private String fileName;

    private String fileType;

    private String contentType;

    private Long size;

    private Long uploadTime;
    /**
     * 业务id
     */
    private String businessId;

    /**
     * 业务类型
     */
    private String businessType;

    /**
     * 业务类
     */
    private String businessClass;

    /**
     * 附件类型：本地附件、外部链接
     */
    private Integer attachmentType;
    /**
     * 访问路径
     */
    private String path;
    /**
     * 状态
     */
    private String statusName;

    public Integer getAttachmentType() {
        return attachmentType;
    }

    public void setAttachmentType(Integer attachmentType) {
        this.attachmentType = attachmentType;
    }

    public String getBusinessClass() {
        return businessClass;
    }

    public void setBusinessClass(String businessClass) {
        this.businessClass = businessClass;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }


    public Long getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Long uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
}
